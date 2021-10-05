package com.eterna.admin.data.helper.database;


import android.content.Context;

import androidx.room.Database;

import com.eterna.admin.R;
import com.eterna.admin.data.helper.models.QuotesTable;
import com.eterna.admin.data.helper.models.VideosTable;

@Database(entities = {VideosTable.class, QuotesTable.class},
        version = 1, exportSchema = false)
public abstract class FoldDatabase extends AppDatabase {

    private static volatile FoldDatabase sInstance;

    public static synchronized FoldDatabase on(){ return sInstance; }

    public static synchronized void init(Context context){
        if (sInstance == null){
            synchronized (FoldDatabase.class){
                sInstance = createDb(context, context.getString(R.string.app_name), FoldDatabase.class);
            }
        }
    }

    public abstract CodesDao codeDao();
    public abstract NamesDao namesDao();

}
