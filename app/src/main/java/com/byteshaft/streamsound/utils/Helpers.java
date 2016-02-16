package com.byteshaft.streamsound.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Helpers {

    private static String sParsedString;

    public static int getRequest(String link) throws IOException {
        URL url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        System.out.println(connection.getResponseCode());
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK ||
                connection.getResponseCode() == 302) {
            InputStream is = connection.getInputStream();
            setParsedString(convertInputStreamToString(is));
        }
        return connection.getResponseCode();
    }

    private static void setParsedString(String string) {
        sParsedString = string;
    }

    public static String getParsedString() {
        return sParsedString;
    }

    public static String convertInputStreamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader r1 = new BufferedReader(new InputStreamReader(
                        is, "UTF-8"));
                while ((line = r1.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    // Check if network is available
    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                AppGlobals.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    // ping the google server to check if internet is really working or not
    public static boolean isInternetWorking() {
        boolean success = false;
        try {
            URL url = new URL("https://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static void userIdAcquired(boolean value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putBoolean(AppGlobals.KEY_USER_ID_STATUS, value).apply();
    }

    // get user login status and manipulate app functions by its returned boolean value
    public static boolean userIdStatus() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getBoolean(AppGlobals.KEY_USER_ID_STATUS, false);
    }

    private static SharedPreferences getPreferenceManager() {
        return PreferenceManager.getDefaultSharedPreferences(AppGlobals.getContext());
    }

    // get user login status and manipulate app functions by its returned boolean value
    public static String getUserId() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getString(AppGlobals.KEY_ID, "");
    }

    public static void userId(String value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putString(AppGlobals.KEY_ID, value).commit();
    }

    public static Bitmap downloadImage(String link) {
        Bitmap myBitmap = null;
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            try {
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);

            } catch (Exception e) {
                e.fillInStackTrace();
                Log.v("ERROR", "Errorchence : " + e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myBitmap;
    }

}
