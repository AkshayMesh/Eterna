package com.eterna.admin.ui.quote;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eterna.admin.R;
import com.eterna.admin.data.index.Constants;

public class AllQuoteActivity extends AppCompatActivity {

    private RecyclerView vehicleList;
    private LinearLayout noContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);
        ImageView backIv = findViewById(R.id.back_iv);
        noContent = findViewById(R.id.no_content);
        vehicleList = findViewById(R.id.user_list);
        backIv.setOnClickListener(v -> onBackPressed());
        vehicleList.setLayoutManager(new LinearLayoutManager(this
                , LinearLayoutManager.VERTICAL, false));
        loadVehicles();
    }

    private void loadVehicles() {
        if (Constants.getInstance(this).isOnline()){

        }else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}