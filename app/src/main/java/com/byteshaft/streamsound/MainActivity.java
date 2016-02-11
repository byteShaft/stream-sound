package com.byteshaft.streamsound;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;

import com.byteshaft.streamsound.adapter.SongsAdapter;
import com.byteshaft.streamsound.utils.AppGlobals;
import com.byteshaft.streamsound.utils.Helpers;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    private ProgressDialog mProgressDialog;
    private ListView mListView;

    // media controls
    private ImageView mPlayerControl;
    private ImageView buttonNext;
    private ImageView buttonPrevious;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.song_list);

        /// Media Controls
        mPlayerControl = (ImageView) findViewById(R.id.btnPlay);
        buttonNext = (ImageView) findViewById(R.id.btnNext);
        buttonPrevious = (ImageView) findViewById(R.id.btnPrevious);

        AppGlobals.initializeAllDataSets();
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

    class GetSoundDetailsTask extends AsyncTask<String, String, ArrayList<Integer>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setMessage("loading ...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected ArrayList<Integer> doInBackground(String... params) {
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
                        if (!AppGlobals.getsSongsIdsArray().contains(jsonObject.get("id").getAsInt())) {
                            int currentSongId = jsonObject.get("id").getAsInt();
                            AppGlobals.addSongId(currentSongId);
                            if (!jsonObject.get("title").isJsonNull()) {
                                AppGlobals.addTitleToHashMap(currentSongId, jsonObject.get("title")
                                        .getAsString());
                            }
                            if (!jsonObject.get("stream_url").isJsonNull()) {
                                AppGlobals.addStreamUrlsToHashMap(currentSongId, jsonObject.get("stream_url")
                                        .getAsString());
                            }
                            if (!jsonObject.get("duration").isJsonNull()) {
                                AppGlobals.addDurationHashMap(currentSongId, jsonObject.get("duration")
                                        .getAsString());
                            }
                            if (!jsonObject.get("genre").isJsonNull()) {
                                AppGlobals.addGenreHashMap(currentSongId, jsonObject.get("genre")
                                        .getAsString());
                            }
                            if (!jsonObject.get("artwork_url").isJsonNull()) {
                                AppGlobals.addSongImageUrlHashMap(currentSongId,
                                        jsonObject.get("artwork_url").getAsString());
                            }
                            JsonObject jsonElements = jsonObject.get("user").getAsJsonObject();
                            System.out.println(jsonElements.get("username").getAsString());
                            if (!jsonElements.get("username").isJsonNull()) {
                                AppGlobals.addSongArtistHashMap(currentSongId,
                                        jsonElements.get("username").getAsString());
                            }
                        }

                    }

                }
            }
            return AppGlobals.getsSongsIdsArray();
        }

        @Override
        protected void onPostExecute(ArrayList<Integer> songIdsArray) {
            super.onPostExecute(songIdsArray);
            mProgressDialog.dismiss();
            SongsAdapter songsAdapter = new SongsAdapter(getApplicationContext(),R.layout.single_row,
                    songIdsArray, MainActivity.this);
            mListView.setAdapter(songsAdapter);
        }
    }
}

