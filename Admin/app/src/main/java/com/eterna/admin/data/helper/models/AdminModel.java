package com.eterna.admin.data.helper.models;

import com.google.firebase.database.DatabaseReference;
import com.eterna.admin.data.index.Constants;

import static com.eterna.admin.data.index.Constants.DATABASE_ROOT;

public class AdminModel {
    public String id;
    public String key;

    public AdminModel() {
    }

    public static DatabaseReference getUserDb() {
        return DATABASE_ROOT.child(Constants.Child.PROJECT_ROOT).child(Constants.Child.ADMIN_ROOT);
    }

}
