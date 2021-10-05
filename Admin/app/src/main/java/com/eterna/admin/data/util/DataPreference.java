package com.eterna.admin.data.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.eterna.admin.data.index.Constants;

import static com.eterna.admin.data.index.Constants.Child.ADMIN_ROOT;
import static com.eterna.admin.data.index.Constants.StringConstants.APP_KEY;
import static com.eterna.admin.data.index.Constants.StringConstants.BASE;
import static com.eterna.admin.data.index.Constants.StringConstants.REGISTRATION;

public class DataPreference {

    public static void setLocaleString(Context context, String key, String value){
        SharedPreferences sPref = context.getSharedPreferences(APP_KEY
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor sPrefEditor = sPref.edit();
        sPrefEditor.putString(key, value);
        sPrefEditor.apply();
    }

    public static String getLocaleString(Context context, String key){
        SharedPreferences sPref = context.getSharedPreferences(APP_KEY
                , Context.MODE_PRIVATE);
        return sPref.getString(key, Constants.StringConstants.EMPTY_KEY);
    }

    public static void setLocaleFlag(Context context, String key, boolean value){
        SharedPreferences sPref = context.getSharedPreferences(Constants.StringConstants.APP_KEY
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor sPrefEditor = sPref.edit();
        sPrefEditor.putBoolean(key, value);
        sPrefEditor.apply();
    }

    public static boolean getLocaleFlag(Context context, String key){
        SharedPreferences sPref = context.getSharedPreferences(Constants.StringConstants.APP_KEY
                , Context.MODE_PRIVATE);
        return sPref.getBoolean(key, false);
    }

    public static void setUserLog(Context context){
        try{
            FirebaseMessaging.getInstance().subscribeToTopic(ADMIN_ROOT);
        }catch (Exception e){
            e.printStackTrace();
        }
        SharedPreferences.Editor editor1 = context.getSharedPreferences(BASE, Context.MODE_PRIVATE).edit();
        editor1.putBoolean(REGISTRATION, true);
        editor1.apply();
    }

    public static boolean getLogStatus(Context context){
        SharedPreferences myPref = context.getSharedPreferences(BASE, Context.MODE_PRIVATE);
        return myPref.getBoolean(REGISTRATION,false);
    }

    public static void dropUserLog(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(BASE, Context.MODE_PRIVATE).edit();
        editor.putBoolean(REGISTRATION, false);
        editor.apply();
    }

    public static String ObjectToString(Class<?> model){
        Gson gson = new Gson();
        return gson.toJson(model);
    }

    public static Object StringToObject(String json, Class<?> cls){
        Gson gson = new Gson();
        return gson.fromJson(json, cls);
    }
}
