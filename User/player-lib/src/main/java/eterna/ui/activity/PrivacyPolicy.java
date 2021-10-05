package eterna.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import eterna.R;

public class PrivacyPolicy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        setActionBarHead();
    }

    private void setActionBarHead() {
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Privacy Policy");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}