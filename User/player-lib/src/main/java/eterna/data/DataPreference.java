package eterna.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import eterna.R;
import eterna.data.Constants.StringConstants;
import eterna.data.schema.UserTable;

public class DataPreference {

    public static void setLocaleString(Context context, String key, String value){
        SharedPreferences sPref = context.getSharedPreferences(StringConstants.APP_KEY
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor sPrefEditor = sPref.edit();
        sPrefEditor.putString(key, value);
        sPrefEditor.apply();
    }

    public static String getLocaleString(Context context, String key){
        SharedPreferences sPref = context.getSharedPreferences(StringConstants.APP_KEY
                , Context.MODE_PRIVATE);
        return sPref.getString(key,StringConstants.EMPTY_KEY);
    }

    public static void setLocaleFlag(Context context, String key, boolean value){
        SharedPreferences sPref = context.getSharedPreferences(StringConstants.APP_KEY
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor sPrefEditor = sPref.edit();
        sPrefEditor.putBoolean(key, value);
        sPrefEditor.apply();
    }

    public static boolean getLocaleFlag(Context context, String key){
        SharedPreferences sPref = context.getSharedPreferences(StringConstants.APP_KEY
                , Context.MODE_PRIVATE);
        return sPref.getBoolean(key, false);
    }

    public static void setUserLog(Context context, UserTable user){
        try{
            if (user!=null){
                FirebaseMessaging.getInstance().subscribeToTopic(user.id);
                FirebaseMessaging.getInstance().subscribeToTopic(context.getString(R.string.app_name));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(StringConstants.BASE, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(StringConstants.USER, json);
        editor.apply();
        SharedPreferences.Editor editor1 = context.getSharedPreferences(StringConstants.BASE, Context.MODE_PRIVATE).edit();
        editor1.putBoolean(StringConstants.REGISTRATION, true);
        editor1.apply();
        setLocaleFlag(context, StringConstants.RATE_AVAILABILITY, true);
    }

    public static UserTable getUserLog(Context context){
        SharedPreferences myPref = context.getSharedPreferences(StringConstants.BASE,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = myPref.getString(StringConstants.USER,"");
        return gson.fromJson(json, UserTable.class);
    }

    public static String getLogId(Context context){
        try {
            return getUserLog(context).id;
        }catch ( NullPointerException e){
            return "";
        }
    }

    public static boolean getLogStatus(Context context){
        SharedPreferences myPref = context.getSharedPreferences(StringConstants.BASE, Context.MODE_PRIVATE);
        return myPref.getBoolean(StringConstants.REGISTRATION,false);
    }

    public static void dropUserLog(Context context){
        FirebaseAuth.getInstance().signOut();
        setLocaleFlag(context, StringConstants.RATE_AVAILABILITY, false);
        SharedPreferences.Editor editor = context.getSharedPreferences(StringConstants.BASE, Context.MODE_PRIVATE).edit();
        editor.putString(StringConstants.USER, "");
        editor.apply();
        editor.putBoolean(StringConstants.REGISTRATION, false);
        setProfileUrl(context,"");
        editor.apply();
    }

    public static void setProfileUrl(Context context, String profileUrl){
        SharedPreferences.Editor editor = context.getSharedPreferences(StringConstants.BASE, Context.MODE_PRIVATE).edit();
        editor.putString(StringConstants.PROFILE, profileUrl);
        editor.apply();
    }

    public static String getProfileUrl(Context context){
        String url;
        SharedPreferences myPref = context.getSharedPreferences(StringConstants.BASE, Context.MODE_PRIVATE);
        url = myPref.getString(StringConstants.PROFILE,"");
        if (url == null || url.equals("")){
            if (getUserLog(context).dpUrl!=null){
                url = getUserLog(context).dpUrl;
            }else {
                url = "";
            }
        }
        return url;
    }
}
