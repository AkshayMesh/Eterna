package com.eterna.admin.data.helper.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.eterna.admin.data.helper.database.TableNames;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@Entity(tableName = TableNames.NAMES)
public class QuotesTable {

    @PrimaryKey(autoGenerate = true)
    public int qid;

    @ColumnInfo(name = "quote_Category")
    public String qotCat;

    public int id;
    public String quoteText;
    public String likes;
    public String shares;

    public QuotesTable() {
    }

    public static DatabaseReference geReference(){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        return dbRef.child("Quotes");
    }

}
