package com.youtubevideos;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Calendar;


public class Singleton extends Application {
    private static Singleton _instance;
    private static String SHARED_PREFERENCES_NAME = "Mapprr";

    public Singleton() {
        super();
    }

    // For lazy initialisation
    public static synchronized Singleton getInstance() {
        if (_instance == null) {
            _instance = new Singleton();
        }
        return _instance;
    }

    public static void clear() {
        _instance = null;
    }

    //for saving in pref
    public static void setPref(String key, String value, Context context) {
        SharedPreferences myPrefs = context.getSharedPreferences(
                SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    //for saving in pref
    public static void setPrefBoolean(String key, Boolean value, Context context) {
        SharedPreferences myPrefs = context.getSharedPreferences(
                SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.apply();
    }


    public static String getPref(String key, Context context) {
        SharedPreferences myPrefs = context.getSharedPreferences(
                SHARED_PREFERENCES_NAME, MODE_PRIVATE);
//        String value = myPrefs.getString(key, null);
        return myPrefs.getString(key, "");
    }

    public static Boolean getPrefBoolean(String key, Context context) {
        SharedPreferences myPrefs = context.getSharedPreferences(
                SHARED_PREFERENCES_NAME, MODE_PRIVATE);
//        String value = myPrefs.getString(key, null);
        return myPrefs.getBoolean(key, false);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
    }

    public static int getAge(Calendar dob) throws Exception {
        Calendar today = Calendar.getInstance();
        int curYear = today.get(Calendar.YEAR);
        int dobYear = dob.get(Calendar.YEAR);
        int age = curYear - dobYear;

        // if dob is month or day is behind today's month or day
        // reduce age by 1
        int curMonth = today.get(Calendar.MONTH);
        int dobMonth = dob.get(Calendar.MONTH);
        if (dobMonth > curMonth) { // this year can't be counted!
            age--;
        } else if (dobMonth == curMonth) { // same month? check for day
            int curDay = today.get(Calendar.DAY_OF_MONTH);
            int dobDay = dob.get(Calendar.DAY_OF_MONTH);
            if (dobDay > curDay) { // this year can't be counted!
                age--;
            }
        }
        return age;
    }
}
