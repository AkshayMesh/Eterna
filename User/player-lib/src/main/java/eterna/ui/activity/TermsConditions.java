package eterna.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import eterna.R;


public class TermsConditions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        setActionBarHead();
    }

    private void setActionBarHead() {
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Terms and Conditions");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}