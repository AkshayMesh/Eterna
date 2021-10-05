package com.eterna.admin.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eterna.admin.data.helper.models.UserTable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.eterna.admin.R;
import com.eterna.admin.data.index.Constants;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView userList;
    private ArrayList<UserTable> userModels;
    private ImageView searchIv, backIv;
    private EditText searchView;
    private ImageView closeSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        initView();
        setListeners();
        loadUsers();
    }

    private void showSearchBar(){
        searchView.setVisibility(View.VISIBLE);
        closeSearch.setVisibility(View.VISIBLE);
    }

    private void hideSearchBar(){
        searchView.setVisibility(View.GONE);
        closeSearch.setVisibility(View.GONE);
        if (userModels.size()>=1){
            userList.setAdapter(new UserDetailsAdapter(userModels));
        }
    }

    private void setListeners() {
        backIv.setOnClickListener(v -> onBackPressed());
        userList.setLayoutManager(new LinearLayoutManager(this
                , LinearLayoutManager.VERTICAL, false));
        searchIv.setOnClickListener(v -> showSearchBar());
        closeSearch.setOnClickListener(v -> hideSearchBar());
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>1){
                    searchUser(s.toString());
                }else {
                    if (userModels.size()>=1){
                        userList.setAdapter(new UserDetailsAdapter(userModels));
                    }
                }
            }
        });
    }

    private void searchUser(String s) {
        ArrayList<UserTable> sortList = new ArrayList<>();
        for (UserTable model : userModels){
            if (model.name.toLowerCase().contains(s.toLowerCase())){
                sortList.add(model);
            }
        }
        loadSearchResult(sortList);
    }

    private void loadSearchResult(ArrayList<UserTable> sortList) {
        if (sortList.size()>=1){
            userList.setAdapter(new UserDetailsAdapter(sortList));
        }
    }

    private void initView() {
        backIv = findViewById(R.id.back_iv);
        userList = findViewById(R.id.user_list);
        searchIv = findViewById(R.id.iv_search);
        searchView = findViewById(R.id.search_bar);
        closeSearch = findViewById(R.id.close_search);
        userModels = new ArrayList<>();
    }

    private void loadUsers() {
        if (Constants.getInstance(this).isOnline()){
            UserTable.getReference().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue()!=null){
                        initUserList(snapshot.getChildren());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(UsersActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void initUserList(Iterable<DataSnapshot> children) {
        userModels.clear();
        for (DataSnapshot postSnap : children){
            UserTable uModel = postSnap.getValue(UserTable.class);
            userModels.add(uModel);
        }
        if (userModels.size()>=1){
            userList.setAdapter(new UserDetailsAdapter(userModels));
        }
    }

    private class UserDetailsAdapter extends RecyclerView.Adapter<UserHolder>{
        private final ArrayList<UserTable> ut;
        public UserDetailsAdapter(ArrayList<UserTable> ut) {
            this.ut = ut;
        }

        @NonNull
        @Override
        public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new UserHolder(LayoutInflater.from(UsersActivity.this).inflate(R.layout.item_user,parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull UserHolder holder, int position) {
            UserTable model = ut.get(position);
            if (model!=null){
                if (model.dpUrl!=null && !model.dpUrl.equals("")){
                    Glide.with(UsersActivity.this).load(model.dpUrl)
                            .placeholder(R.drawable.ic_person).into(holder.userDpIv);
                }
                holder.loginType.setText(model.loginType);
                holder.dateTv.setText(model.dateLogin);
                if (model.dateCreated!=null && !model.dateCreated.equals(""))
                holder.phoneTv.setText(model.dateCreated);
                holder.emailTv.setText(model.email);
                holder.nameTv.setText(model.name);
                holder.idTv.setText(model.id);
            }
        }

        @Override
        public int getItemCount() {
            return ut.size();
        }
    }

    private static class UserHolder extends RecyclerView.ViewHolder {
        final TextView idTv, nameTv, emailTv, phoneTv, dateTv, loginType;
        final ImageView userDpIv;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            idTv = itemView.findViewById(R.id.user_id_tv);
            nameTv = itemView.findViewById(R.id.user_name_tv);
            emailTv = itemView.findViewById(R.id.user_email);
            phoneTv = itemView.findViewById(R.id.user_phone_tv);
            dateTv = itemView.findViewById(R.id.user_date_tv);
            loginType = itemView.findViewById(R.id.login_tv);
            userDpIv = itemView.findViewById(R.id.user_dp);
        }
    }
}