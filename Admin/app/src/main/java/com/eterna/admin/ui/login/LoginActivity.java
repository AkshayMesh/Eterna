package com.eterna.admin.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eterna.admin.data.helper.database.DatabaseUtil;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.eterna.admin.R;
import com.eterna.admin.data.index.Constants;
import com.eterna.admin.data.helper.models.AdminModel;
import com.eterna.admin.data.util.DataPreference;
import com.eterna.admin.ui.main.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText idEt, passwordEt;
    private AdminModel adminModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        loadCredentials();
    }

    private void loadCredentials() {
        DatabaseUtil.init(this);
        if (DataPreference.getLogStatus(this)){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }else{
            if (Constants.getInstance(this).isOnline()){
                AdminModel.getUserDb().child(Constants.Child.KEY_ROOT).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue()!=null){
                            try {
                                adminModel = (AdminModel) DataPreference.StringToObject(snapshot.getValue().toString(), AdminModel.class);
                            }catch (ClassCastException classCastException){
                                classCastException.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "unable to connect to server", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(this, "Noo Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initViews() {
        idEt = ((TextInputLayout)findViewById(R.id.user_id_et)).getEditText();
        passwordEt = ((TextInputLayout)findViewById(R.id.user_pass_et)).getEditText();
        TextView loginTv = findViewById(R.id.login_tv);
        adminModel = new AdminModel();
        loginTv.setOnClickListener(v -> OnClick());
    }

    private void OnClick() {
        if(Constants.getInstance(this).isOnline()){
            if (idEt.getText().toString().trim().length()>0){
                if (passwordEt.getText().toString().trim().length()>0){
                    if (idEt.getText().toString().trim().equals(adminModel.id)){
                        if (passwordEt.getText().toString().trim().equals(adminModel.key)){
                            DataPreference.setUserLog(this);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }else {
                            passwordEt.setError("Password is incorrect");
                        }
                    }else {
                        passwordEt.setError("Password is incorrect");
                    }
                }else {
                    passwordEt.setError("Field can not be empty");
                }
            }else {
                idEt.setError("Field can not be empty");
            }
        }else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}