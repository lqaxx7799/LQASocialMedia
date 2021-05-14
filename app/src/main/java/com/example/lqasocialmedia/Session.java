package com.example.lqasocialmedia;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences prefs;

    public Session(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setToken(String token) {
        prefs.edit().putString("token", token).commit();
    }

    public String getToken() {
        String token = prefs.getString("token", "");
        return token;
    }
}
