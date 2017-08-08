package com.android.summer.csula.foodvoter.pushNotifications;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyFirebasePreference {

    private static final String PREF_TOKEN = "token";

    public static void setToken(Context context, String token) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(PREF_TOKEN, token);
        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_TOKEN, null);
    }
}
