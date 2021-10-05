package eterna.data.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("StaticFieldLeak")
public class PermissionService {

    public static final int REQUEST_CODE_PERMISSION_DEFAULT = 1;

    private static Context mContext;
    private static PermissionService invokePermission;

    public PermissionService() {
    }

    public static synchronized PermissionService on(Context context) {
        if (invokePermission == null) {
            invokePermission = new PermissionService();
        }
        mContext = context;
        return invokePermission;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean request(int requestCode, String... str) {
        return request(null, requestCode, str);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean request(Fragment fragment, int requestCode, String... str) {
        List<String> finalArgs = new ArrayList<>();

        for (String aStr : str) {
            if (mContext.checkSelfPermission(aStr) != PackageManager.PERMISSION_GRANTED) {
                finalArgs.add(aStr);
            }
        }

        if (finalArgs.isEmpty()) {
            return true;
        }

        if(fragment == null) {
            ((Activity) mContext).requestPermissions(finalArgs.toArray(new String[0]), requestCode);
        } else {
            fragment.requestPermissions(finalArgs.toArray(new String[0]), requestCode);
        }

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean request(String... str) {
        if (mContext == null) return false;
        List<String> finalArgs = new ArrayList<>();
        for (String s : str) {
            if (mContext.checkSelfPermission(s) != PackageManager.PERMISSION_GRANTED) {
                finalArgs.add(s);
            }
        }
        if (finalArgs.isEmpty()) {
            return true;
        }
        ((Activity) mContext).requestPermissions(finalArgs.toArray(new String[0]), REQUEST_CODE_PERMISSION_DEFAULT);

        return false;
    }

}
