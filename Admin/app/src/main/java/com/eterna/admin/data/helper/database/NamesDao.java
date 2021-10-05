package com.eterna.admin.data.helper.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.eterna.admin.data.helper.models.QuotesTable;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface NamesDao {

    @Query("SELECT * FROM " + TableNames.NAMES)
    List<QuotesTable> getScrollableCodes();

    @Query("DELETE FROM " + TableNames.NAMES)
    void nukeTable();

    @Query("DELETE FROM " + TableNames.NAMES + " WHERE quote_Category =:cat")
    void killCat(String cat);

    @Query("SELECT COUNT(id) FROM " + TableNames.NAMES)
    int getRowCount();

    @Query("SELECT * FROM " + TableNames.NAMES + " WHERE quote_Category =:prodCat" )
    List<QuotesTable> getCodeByCategory(String prodCat);

    @Insert
    void insertAll(QuotesTable... codes);

}
