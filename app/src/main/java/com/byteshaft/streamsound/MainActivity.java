package com.byteshaft.streamsound;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.byteshaft.streamsound.utils.AppGlobals;
import com.byteshaft.streamsound.utils.Helpers;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Integer> songsIdsArray;
    private HashMap<Integer, String> songsTitleHashMap;
    private HashMap<Integer, String> streamUrls;
    private HashMap<Integer, String> songDurationMap;
    private MediaPlayer mMediaPlayer;
    private ImageView mPlayerControl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeAllDataSets();
        new GetSoundDetailsTask().execute();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                togglePlayPause();
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayerControl.setImageResource(R.drawable.ic_play);
            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (mMediaPlayer.isPlaying()) {
//                    mMediaPlayer.stop();
//                    mMediaPlayer.reset();
//                }
//                try {
//                    mMediaPlayer.setDataSource(streamUrls.get(songsIdsArray.get(position)));
//                    mMediaPlayer.prepareAsync();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });

    }

    private void togglePlayPause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPlayerControl.setImageResource(R.drawable.ic_play);
        } else {
            mMediaPlayer.start();
            mPlayerControl.setImageResource(R.drawable.ic_pause);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void initializeAllDataSets() {
        songsIdsArray = new ArrayList<>();
        songsTitleHashMap = new HashMap<>();
        streamUrls = new HashMap<>();
        songDurationMap = new HashMap<>();
    }

    class GetSoundDetailsTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            int responseCode = 0;
            if (Helpers.isNetworkAvailable() && Helpers.isInternetWorking()) {
                try {
                    responseCode = Helpers.getRequest(AppGlobals.USER_URL + AppGlobals.CLIENT_KEY);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    JsonParser jsonParser = new JsonParser();
                    JsonArray jsonArray = jsonParser.parse(Helpers.getParsedString())
                            .getAsJsonArray();
                    System.out.println(jsonArray);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                        if (!songsIdsArray.contains(jsonObject.get("id").getAsInt())) {
                            int currentSongId = jsonObject.get("id").getAsInt();
                            songsIdsArray.add(currentSongId);
                            songsTitleHashMap.put(currentSongId, jsonObject.get("title")
                                    .getAsString());
                            streamUrls.put(currentSongId, jsonObject.get("stream_url")
                                    .getAsString());
                            songDurationMap.put(currentSongId, jsonObject.get("duration")
                                    .getAsString());
                        }

                    }
                    System.out.println(songsIdsArray);
                    System.out.println(songsTitleHashMap);
                    System.out.println(streamUrls);
                    System.out.println(songDurationMap);
                }


            }
            return null;
        }
    }
}

