package com.example.haili.btl.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private static final String CONFIGURATION_NAME = "my_data";
    private SharedPreferences sharedPreferences = null;

    public SharedPreferencesUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(CONFIGURATION_NAME, Activity.MODE_PRIVATE);
    }

    public SharedPreferencesUtil(Context context,String config){
        sharedPreferences = context.getSharedPreferences(config,Activity.MODE_PRIVATE);
    }

    public void saveBoolean(String key,boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    public boolean getBoolean(String key,boolean defVa){
        return sharedPreferences.getBoolean(key,defVa);
    }

    public void saveString(String key,String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public String getString(String key,String defVa){
        return sharedPreferences.getString(key,defVa);
    }

    public void saveInt(String key,int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.commit();
    }

    public int getInt(String key,int defVa){
        return sharedPreferences.getInt(key,defVa);
    }

    public void clearAll(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void remove(String key){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }
}

