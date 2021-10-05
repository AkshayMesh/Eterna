package eterna.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import eterna.R;
import eterna.data.schema.VideosTable;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 8080;
    private static final int REQUEST_READ = 9090;

    EditText titleEt, subTitleEt;
    TextView titleTv;
    Button uploadBtn;
    ProgressBar uploadProgress;
    Spinner moodSpinner;
    Uri pickedVideoUri;
    VideoView videoPreView;
    String title, subTitle, moodStr;
    int category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideWindowBars();
        setContentView(R.layout.activity_splash);
        titleTv = findViewById(R.id.app_name_tv);
        askLogin();
//        codeForUpload();
    }

    @SuppressWarnings("deprecation")
    private void hideWindowBars() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
    }

    private void askLogin() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(()-> {
                    Intent intent = new Intent(Splash.this, Login.class);
                    startActivity(intent);
                    finish();
                });
            }
        }, 4200);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickVideo();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void codeForUpload() {

        pickedVideoUri = null;
        titleEt = findViewById(R.id.title_et);
        subTitleEt = findViewById(R.id.description_et);
        uploadBtn = findViewById(R.id.upload_btn);
        uploadProgress = findViewById(R.id.upload_progress);
        moodSpinner = findViewById(R.id.spin_select);
        videoPreView = findViewById(R.id.video_preview);

        uploadBtn.setOnClickListener(v ->{
            if (pickedVideoUri!=null){
                uploadToStorage();
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED){
                        requestPermissions();
                    }else {
                        pickVideo();
                    }
                }else {
                    pickVideo();
                }
            }
        });
    }

    private void pickVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video")
                , REQUEST_TAKE_GALLERY_VIDEO);
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this
                ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                , REQUEST_READ);
    }

    private void uploadToStorage() {
        if (isTitleProvided()){
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            storageRef.child("videos").child(moodStr).child(""+System.currentTimeMillis()).putFile(pickedVideoUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        saveToDatabase(taskSnapshot.getStorage().getDownloadUrl());
                        uploadProgress.setProgress(100);
                        Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        titleEt.setText("");
                        subTitleEt.setText("");
                        pickedVideoUri = null;
                    }).addOnFailureListener(e ->
                        Toast.makeText(this, "Try again later", Toast.LENGTH_SHORT).show())
                    .addOnProgressListener(snapshot -> {
                        Toast.makeText(this, "Uploading Video...", Toast.LENGTH_SHORT).show();
                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        uploadProgress.setProgress((int)progress);
            });
        }
    }

    private void saveToDatabase(Task<Uri> downloadUrl) {
        while (!downloadUrl.isSuccessful());
        Uri downloadUri = downloadUrl.getResult();
        if (downloadUrl.isSuccessful()){
            VideosTable songRow = new VideosTable();
            songRow.description = subTitle;
            songRow.title = title;
            songRow.link = downloadUri.toString();
            if (moodStr.equals(""))
                moodStr = "Normal";
            DatabaseReference dbRef = VideosTable.geReference();
            final String key = dbRef.push().getKey();
            assert key!=null;
            dbRef.child(moodStr).child(key).setValue(songRow);
        }
    }

    private boolean isTitleProvided() {
        title = titleEt.getText().toString().trim();
        subTitle = subTitleEt.getText().toString().trim();
        category = moodSpinner.getSelectedItemPosition();

        switch (category){
            case 0:
            default:
                moodStr = "";
                break;
            case 1:
            case 3:
                moodStr = "Smiling";
                break;
            case 2:
            case 6:
                moodStr = "Sad";
                break;
            case 4:
                moodStr = "Normal";
                break;
            case 5:
                moodStr = "Happy";
                break;
        }

        if (title.equals("")){
            titleEt.setError("Field can not be empty");
            return false;
        }else if (subTitle.equals("")){
            subTitleEt.setError("Field can not be empty");
            return false;
        }else if (category == 0){
            Toast.makeText(this, "Please Select Emotion", Toast.LENGTH_SHORT).show();
            return false;
        }else if (pickedVideoUri == null){
            Toast.makeText(this, "Please select video for uploading..", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO){
                pickedVideoUri = data.getData();
                setVideoForPreview();
            }
        }
    }

    private void setVideoForPreview() {
        MediaController mController = new MediaController(this);
        mController.setAnchorView(videoPreView);
        videoPreView.setMediaController(mController);
        videoPreView.setVideoURI(pickedVideoUri);
        videoPreView.requestFocus();
        videoPreView.setOnPreparedListener(MediaPlayer::start);
    }

}