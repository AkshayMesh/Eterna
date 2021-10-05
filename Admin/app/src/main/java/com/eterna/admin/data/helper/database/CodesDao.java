package com.eterna.admin.data.helper.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.eterna.admin.data.helper.models.VideosTable;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CodesDao {

    @Query("SELECT * FROM " + TableNames.CODES)
    List<VideosTable> getScrollableCodes();

    @Query("DELETE FROM " + TableNames.CODES)
    void nukeTable();

    @Query("DELETE FROM " + TableNames.CODES + " WHERE video_Category =:cat")
    void killCat(String cat);

    @Query("SELECT COUNT(id) FROM " + TableNames.CODES)
    int getRowCount();

    @Query("SELECT * FROM " + TableNames.CODES + " WHERE video_Category =:prodCat" )
    List<VideosTable> getCodeByCategory(String prodCat);

    @Insert
    void insertAll(VideosTable... codes);

}
