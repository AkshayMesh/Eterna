package eterna.ui.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.sql.Date;

import eterna.R;
import eterna.data.DataPreference;
import eterna.data.schema.UserTable;

import static eterna.data.Constants.IntentKeys.RC_SIGN_IN;
import static eterna.data.Constants.NumberConstants.ID_MAX;
import static eterna.data.Constants.NumberConstants.ID_MIN;
import static eterna.data.Constants.StringConstants.EMAIL;
import static eterna.data.Constants.StringConstants.GOOGLE_LOGIN;
import static eterna.data.Constants.StringConstants.LOGIN_BG;
import static eterna.data.Constants.StringConstants.NEW_USER;

public class Login extends AppCompatActivity {

    private TextView skipText;
    private TextView signInText;
    private VideoView videoBg;
    private ProgressBar loadingView;
    private CheckBox termsCheck;
    private TextView termsTv;
    private TextView policyTv;

    private GoogleSignInClient mClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        initViews();
        initAds();
        if (DataPreference.getLogStatus(this)){
            loginUser(false);
        }else {
            initSignInOptions();
        }
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


    private void initViews() {
        skipText = findViewById(R.id.skip_txt);
        signInText = findViewById(R.id.sign_in_txt);
        videoBg = findViewById(R.id.intro_vid);
        loadingView = findViewById(R.id.loading);
        termsCheck = findViewById(R.id.terms_check);
        termsTv = findViewById(R.id.terms_tv);
        policyTv = findViewById(R.id.privacy_policy_tv);
        setEvents();
    }

    private void setEvents() {
        skipText.setOnClickListener(v-> startWithoutLogin());
        signInText.setOnClickListener(v -> startLogin());
        termsCheck.setOnCheckedChangeListener((buttonView, isChecked) -> signInText.setEnabled(isChecked));
        termsTv.setOnClickListener(v -> startActivity(new Intent(Login.this, TermsConditions.class)));
        policyTv.setOnClickListener(v -> startActivity(new Intent(Login.this, PrivacyPolicy.class)));

        try{
            videoBg.setVideoPath(LOGIN_BG);
            videoBg.setOnPreparedListener(mp ->{
                loadingView.setVisibility(View.GONE);
                mp.start();
            });
            videoBg.setOnCompletionListener(MediaPlayer::start);
            videoBg.setOnErrorListener((mp, what, extra) -> {
                loadingView.setVisibility(View.VISIBLE);
                return false;
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loginUser(boolean once){
        if (once)
        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Login.this, Main.class));
        finish();
    }

    private void startWithoutLogin() {
        Intent intent = new Intent(this, DemoMain.class);
        startActivity(intent);
    }

    @SuppressWarnings("deprecation")
    private void startLogin() {
        Intent signInIntent = mClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void initSignInOptions() {
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_client_token))
                .requestEmail()
                .build();
        mClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            onGoogleSignInResult(data);
        }
    }

    private void onGoogleSignInResult(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(this, "authenticating...", Toast.LENGTH_SHORT).show();
            firebaseAuthWithGoogleAccount(account);
        }catch (ApiException e){
            Toast.makeText(this, "Unable to log in", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        createUserIfNotExist(account);
                    }else {
                        Toast.makeText(this, "Please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createUserIfNotExist(GoogleSignInAccount account) {
        String photoUrl, date, mailAddress;
        mailAddress = account.getEmail();
        date = getCurrentDate();
        if (account.getPhotoUrl()!=null && !account.getPhotoUrl().toString().equals("")){
            photoUrl = account.getPhotoUrl().toString();
        }else{
            photoUrl = "";
        }
        UserTable user = new UserTable(account.getDisplayName(),mailAddress, photoUrl, date
                                    , NEW_USER, GOOGLE_LOGIN);

        UserTable.getReference().orderByChild(EMAIL).equalTo(mailAddress).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount()>0){
                            getExistingUserData(snapshot);
                        }else {
                            createNewUser(user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Login.this, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getCurrentDate() {
        return (new Date(System.currentTimeMillis())).toString();
    }

    private void createNewUser(UserTable user) {
        int idNumber = (int) (Math.random() * (ID_MAX - ID_MIN + 1) + ID_MIN);
        user.id = "user_"+idNumber;
        user.dateCreated = getCurrentDate();
        DataPreference.setUserLog(this, user);
        UserTable.getReference().child(user.id).setValue(user);
    }

    private void getExistingUserData(DataSnapshot snapshot) {
        Gson gson = new Gson();
        String jString = gson.toJson(snapshot.getValue());
        UserTable user = gson.fromJson(jString.substring(14, jString.length()-1), UserTable.class);
        if (user!=null){
            DataPreference.setUserLog(Login.this, user);
            user.dateLogin = getCurrentDate();
            UserTable.getReference().child(user.id).setValue(user);
            loginUser(true);
        }else {
            Toast.makeText(this, "Unable to get login data", Toast.LENGTH_SHORT).show();
        }
    }
}