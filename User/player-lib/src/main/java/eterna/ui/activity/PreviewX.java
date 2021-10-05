package eterna.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;

import eterna.R;

public class PreviewX extends AppCompatActivity {

    private PreviewView preView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_x);
        initVars();
    }

    private void initVars() {
        preView = findViewById(R.id.preview_view);
    }
}