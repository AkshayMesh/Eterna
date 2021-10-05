package com.eterna.admin.data.index;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Constants {
    private static final Constants instance = new Constants();
    private boolean connected = false;
    private static Context context;

    public static final DatabaseReference DATABASE_ROOT = FirebaseDatabase.getInstance().getReference();
    public static final StorageReference STORAGE_ROOT = FirebaseStorage.getInstance().getReference();

    public static Constants getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    public boolean isOnline() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network nw = connectivityManager.getActiveNetwork();
                connected = nw != null ;
                NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
                connected =  actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
            } else {
                NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
                connected =  nwInfo != null && nwInfo.isAvailable() &&nwInfo.isConnected();
            }
            return connected;
        } catch (Exception e) {
            Log.v("connectivity", e.toString());
        }
        return connected;
    }

    public interface ServerUrl {

    }

    public interface StringConstants{
        String APP_KEY          = "eterna_terms";
        String REGISTRATION     = "entered";
        String EMPTY_KEY        = "";
        String BASE             = "DATA";
        String USER             = "customer";
        String SMILE            = " Smiling";
        String SAD              = " Sad";
        String HAPPY            = " Happy";
        String NORMAL           = " Normal";
    }

    public interface IntentKeys{
        String EMOTION = "emotion";
    }

    public interface Child{
        String PROJECT_ROOT = "Database-Root";
        String USER_ROOT = "Users";
        String ADMIN_ROOT = "Admin";
        String KEY_ROOT = "secret";
    }

    public interface ParentPath{
        String WAY = "notifications/";
    }

    public interface PostFile {
        String NOTIFY = ParentPath.WAY + "NotificationAPI";
    }


    public interface Extension{
        String EXE = ".php";
        String PROTOCOL = "http://";
    }
}
