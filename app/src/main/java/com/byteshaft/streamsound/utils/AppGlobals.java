package com.byteshaft.streamsound.utils;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

public class AppGlobals extends Application {

    private static ArrayList<Integer> sSongsIdsArray;
    private static HashMap<Integer, String> sSongsTitleHashMap;
    private static HashMap<Integer, String> sStreamUrls;
    private static HashMap<Integer, String> sSongDurationMap;
    private static HashMap<Integer, String> sGenreHashMap;
    private static HashMap<Integer, String> sSongImageUrls;
    private static HashMap<Integer, String > sArtistHashMap;

    private static Context sContext;
    public static final String USER_URL = "http://api.soundcloud.com/users/197638516/tracks.json?client_id=";
    public static final String CLIENT_KEY = "d15e89ac63aed800d452231a67207696";
    public static final String ADD_CLIENT_ID = "?client_id=";

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }

    public static void initializeAllDataSets() {
        sSongsIdsArray = new ArrayList<>();
        sSongsTitleHashMap = new HashMap<>();
        sStreamUrls = new HashMap<>();
        sSongDurationMap = new HashMap<>();
        sGenreHashMap = new HashMap<>();
        sSongImageUrls = new HashMap<>();
        sArtistHashMap = new HashMap<>();
    }

    public static void addSongId(int id) {
        sSongsIdsArray.add(id);
    }

    public static ArrayList<Integer> getsSongsIdsArray() {
        return sSongsIdsArray;
    }

    public static void addTitleToHashMap(int id, String value) {
        sSongsTitleHashMap.put(id, value);
    }

    public static HashMap<Integer, String> getTitlesHashMap() {
        return sSongsTitleHashMap;
    }

    public static void addStreamUrlsToHashMap(int id, String value) {
        sStreamUrls.put(id, value);
    }

    public static HashMap<Integer, String> getStreamUrlsHashMap() {
        return sStreamUrls;
    }

    public static void addDurationHashMap(int id, String value) {
        sSongDurationMap.put(id, value);
    }

    public static HashMap<Integer, String> getDurationHashMap() {
        return sSongDurationMap;
    }

    public static void addGenreHashMap(int id, String value) {
        sGenreHashMap.put(id, value);
    }

    public static HashMap<Integer, String> getGenreHashMap() {
        return sGenreHashMap;
    }

    public static void addSongImageUrlHashMap(int id, String value) {
        sSongImageUrls.put(id, value);
    }

    public static HashMap<Integer, String> getSongImageUrlHashMap() {
        return sSongImageUrls;
    }

    public static void addSongArtistHashMap(int id, String value) {
        sArtistHashMap.put(id, value);
    }

    public static HashMap<Integer, String> getSongArtistHashMap() {
        return sArtistHashMap;
    }
}
