package com.rakib.moviewer.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Rakib on 03/07/2018.
 */


public class SharedPref extends Activity {
    private static SharedPreferences mSharedPref;

    public static final String DriveId = "DriveId";
    public static final String NAME = "NAME";
    public static final String PREFS_NAME = "MyPrefsFile";

    private SharedPref() {

    }

    public static void init(Context context) {
        if (mSharedPref == null) {
            mSharedPref = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
            int number = mSharedPref.getAll().size();
            Log.i("Information", "Information: " + mSharedPref.getAll().size());
            Log.i("Information", "init: shared file created");
            if (number > 0) {
                Log.i("information", mSharedPref.getString(SharedPref.DriveId, ""));
            }
        }

    }

    public static boolean check_if_key_exists(String key) {
        if(mSharedPref.contains(key))
        {
            return  true;
        }
        else
        {
            return  false;
        }
    }

    public static String read(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public static boolean read(String key, boolean defValue) {
        return mSharedPref.getBoolean(key, defValue);
    }

    public static void write(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.apply();
    }

    public static Integer read(String key, int defValue) {
        return mSharedPref.getInt(key, defValue);
    }

    public static void write(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putInt(key, value).apply();
    }
    public static Float read(String key, Float defValue) {
        return mSharedPref.getFloat(key, defValue);
    }

    public static void write(String key, Float value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putFloat(key, value).apply();
    }
}