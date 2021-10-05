package com.eterna.admin.data.helper.database;

import android.content.Context;

import com.eterna.admin.data.helper.models.QuotesTable;
import com.eterna.admin.data.helper.models.VideosTable;

import java.util.ArrayList;

public class DatabaseUtil {
    /**
     * Fields
     */
    private static DatabaseUtil sInstance;
    private CodesDao mCodeDao;
    private NamesDao mNamesDao;

    private DatabaseUtil() {
        setCodeDao(FoldDatabase.on().codeDao());
        setNamesDao(FoldDatabase.on().namesDao());
    }

    /**
     * This method builds an instance
     */
    public static void init(Context context) {
        FoldDatabase.init(context);

        if (sInstance == null) {
            sInstance = new DatabaseUtil();
        }
    }

    public static DatabaseUtil on() {
        if (sInstance == null) {
            sInstance = new DatabaseUtil();
        }

        return sInstance;
    }

    public CodesDao getCodeDao() {
        return mCodeDao;
    }

    public NamesDao getNamesDao() {
        return mNamesDao;
    }

    public void setCodeDao(CodesDao mCodeDao) {
        this.mCodeDao = mCodeDao;
    }

    public void setNamesDao(NamesDao mNamesDao) {
        this.mNamesDao = mNamesDao;
    }

    public void insertItem(VideosTable... items){
        getCodeDao().insertAll(items);
    }

    public void killCatCode(String cat){
        getCodeDao().killCat(cat);
    }

    public ArrayList<VideosTable> getAllCodes(){
        return new ArrayList<>(getCodeDao().getScrollableCodes());
    }

    public void deleteAllCodes(){
        getCodeDao().nukeTable();
    }

    public ArrayList<VideosTable> getSelectedCodes(String opt){
        return new ArrayList<>(getCodeDao().getCodeByCategory(opt));
    }

    public void addItems(QuotesTable... items){
        getNamesDao().insertAll(items);
    }

    public ArrayList<QuotesTable> getAllNames(){
        return  new ArrayList<>(getNamesDao().getScrollableCodes());
    }

    public ArrayList<QuotesTable> getSelectedNames(String opt){
        return new ArrayList<>(getNamesDao().getCodeByCategory(opt));
    }

    public void killCatNames(String cat){
        getNamesDao().killCat(cat);
    }

    public void deleteAllNames(){
        getNamesDao().nukeTable();
    }
}
