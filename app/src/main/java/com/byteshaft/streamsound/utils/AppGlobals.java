package com.byteshaft.streamsound.utils;

import android.app.Application;
import android.content.Context;

public class AppGlobals extends Application {

    private static Context sContext;
    public static final String USER_URL = "http://api.soundcloud.com/users/197638516/tracks.json?client_id=";
    public static final String CLIENT_KEY = "d15e89ac63aed800d452231a67207696";

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
