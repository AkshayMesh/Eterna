package com.eterna.admin.data.helper.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.eterna.admin.data.helper.database.TableNames;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@Entity(tableName = TableNames.CODES)
public class VideosTable {

    @PrimaryKey(autoGenerate = true)
    public int vid;

    @ColumnInfo(name = "video_Category")
    public String vidCat;
    public int id;
    public String likes;
    public String name;
    public String link;
    public String description;
    public String title;

    public VideosTable() {}

    public static DatabaseReference geReference(){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        return dbRef.child("Videos");
    }

}
