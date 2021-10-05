package eterna.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;
import eterna.R;
import eterna.data.DataPreference;
import eterna.data.schema.UserTable;
import eterna.data.service.PermissionService;

import static eterna.ui.activity.DemoMain.Emotion;

public class Main extends AppCompatActivity {

    private TextView userNameTv;
    private ImageView userDpView;

    private Button big_button;
    private CardView happyBtn;
    private CardView sadBtn;
    private CardView crazyBtn;
    private CardView nmlBtn;

    private FaceDetector detector;
    private static final int CAMERA_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        initViews();
        initAds();
        initDetector();
        LoadMood.picDesc = "";
        registerForContextMenu(findViewById(R.id.more));
    }

    private void initAds() {
        MobileAds.initialize(this, initializationStatus -> {});
        AdView adView = findViewById(R.id.adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                System.out.println(loadAdError.getMessage());
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        if (requestCode == PermissionService.REQUEST_CODE_PERMISSION_DEFAULT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (grantResults.length == 2 && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }else if (grantResults.length == 1){
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            if (data.getExtras()!=null){
                try {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    processCameraPicture(photo);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed to load Image"
                            , Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void initViews(){
        big_button = findViewById(R.id.big_button);
        happyBtn = findViewById(R.id.happyBtn);
        sadBtn = findViewById(R.id.sadBtn);
        nmlBtn = findViewById(R.id.normalBtn);
        crazyBtn = findViewById(R.id.crazyBtn);
        userDpView = findViewById(R.id.user_dp);
        userNameTv = findViewById(R.id.user_name);

        assignValues();
        onClicks();
    }

    private void assignValues() {
        UserTable ut = DataPreference.getUserLog(this);
        if (ut!=null){
            try {
                Glide.with(this).load(ut.dpUrl).placeholder(R.drawable.ic_face).into(userDpView);
            }catch (Exception e){
                e.printStackTrace();
            }
            userNameTv.setText(ut.name);
        }else {
            Toast.makeText(this, "Unable to get account details", Toast.LENGTH_SHORT).show();
            DataPreference.dropUserLog(this);
            finish();
        }
    }

    private void onClicks(){
        big_button.setOnClickListener(v -> askPermission());
        sadBtn.setOnClickListener(v -> {
            sadMood();
            hideDetectors();
        });
        happyBtn.setOnClickListener(v -> {
            smilingMood();
            hideDetectors();
        });
        crazyBtn.setOnClickListener(v -> {
            happyMood();
            hideDetectors();
        });
        nmlBtn.setOnClickListener(v -> {
            normalMood();
            hideDetectors();
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu.setHeaderTitle("Do you want to logout?");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getItemId()==R.id.logout){
            DataPreference.dropUserLog(this);
            startActivity(new Intent(this, Login.class));
            finish();
        }else {
            return false;
        }
        return true;
    }

    private void initDetector(){
        try{
            detector = new FaceDetector.Builder(getApplicationContext())
                    .setTrackingEnabled(false)
                    .setLandmarkType(FaceDetector.ALL_CLASSIFICATIONS)
                    .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                    .setMode(FaceDetector.FAST_MODE)
                    .build();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionService.on(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }else{
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }


    private void processCameraPicture(Bitmap bmp) {
        if (detector.isOperational() &&
                bmp != null) {
            Bitmap editedBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp
                    .getHeight(), bmp.getConfig());
            float scale = getResources().getDisplayMetrics().density;
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.GREEN);
            paint.setTextSize((int) (16 * scale));
            paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(6f);
            Canvas canvas = new Canvas(editedBitmap);
            canvas.drawBitmap(bmp, 0, 0, paint);
            Frame frame = new Frame.Builder().setBitmap(editedBitmap).build();
            SparseArray<Face> faces = detector.detect(frame);
            for (int index = 0; index < faces.size(); ++index) {
                Face face = faces.valueAt(index);
                canvas.drawRect(
                        face.getPosition().x,
                        face.getPosition().y,
                        face.getPosition().x + face.getWidth(),
                        face.getPosition().y + face.getHeight(), paint);

                canvas.drawText("Face " + (index + 1), face.getPosition().x + face.getWidth()
                        , face.getPosition().y + face.getHeight(), paint);

//                SubActivity.picDesc = "FACE DESCRIPTION : " + (index + 1) + "\n";
//                txtTakenPicDesc.setText(txtTakenPicDesc.getText() + "Smile probability:" + " " + face.getIsSmilingProbability() + "\n");
//                txtTakenPicDesc.setText(txtTakenPicDesc.getText() + "Left Eye Is Open Probability: " + " " + face.getIsLeftEyeOpenProbability() + "\n");
//                txtTakenPicDesc.setText(txtTakenPicDesc.getText() + "Right Eye Is Open Probability: " + " " + face.getIsRightEyeOpenProbability() + "\n\n");

                if (face.getIsSmilingProbability()>0.0&&face.getIsSmilingProbability()<=0.1){
                    sadMood();
                }else if (face.getIsSmilingProbability()>0.1&&face.getIsSmilingProbability()<=0.3){
                    normalMood();
                }else if (face.getIsSmilingProbability()>0.3&&face.getIsSmilingProbability()<0.8){
                    smilingMood();
                }else if (face.getIsSmilingProbability()>=0.8&&face.getIsSmilingProbability()<=1){
                    happyMood();
                }

                for (Landmark landmark : face.getLandmarks()) {
                    int cx = (int) (landmark.getPosition().x);
                    int cy = (int) (landmark.getPosition().y);
                    canvas.drawCircle(cx, cy, 8, paint);
                }
            }
            if (faces.size() == 0) {
                Toast.makeText(this, "No face found", Toast.LENGTH_SHORT).show();
            } else {
                LoadMood.picDesc = "No of Faces Detected: "+ faces.size();
                hideDetectors();
            }
        } else {
            Toast.makeText(this, "unable to process image ", Toast.LENGTH_SHORT).show();
        }
    }


    private void hideDetectors(){
        Intent gotoSub = new Intent(this, LoadMood.class);
        startActivity(gotoSub);
    }

    private void smilingMood(){
        Emotion = " Smiling";
    }

    private void sadMood(){
        Emotion = " Sad";
    }

    private void happyMood(){
        Emotion = " Happy";
    }

    private void normalMood(){
        Emotion = " Normal";
    }

    public void moreClicked(View view) {
        openContextMenu(view);
    }
}