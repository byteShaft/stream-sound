package com.byteshaft.streamsound;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.byteshaft.streamsound.adapter.SongsAdapter;
import com.byteshaft.streamsound.service.PlayService;
import com.byteshaft.streamsound.utils.AppGlobals;
import com.byteshaft.streamsound.utils.Helpers;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog mProgressDialog;
    private ListView mListView;
    ImageView mPlayerControl;
    private ImageView buttonNext;
    private ImageView buttonPrevious;
    RelativeLayout controls_layout;
    SeekBar seekBar;
    private int songLength;
    double updateValue;
    private int songLengthInSeconds;
    private static MainActivity sInstance;
    TextView bufferingTextView;

    public static MainActivity getInstance() {
        return sInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sInstance = this;
        mListView = (ListView) findViewById(R.id.song_list);
        controls_layout = (RelativeLayout) findViewById(R.id.now_playing_controls_header);
        /// Media Controls
        mPlayerControl = (ImageView) findViewById(R.id.play_pause_button);
        buttonNext = (ImageView) findViewById(R.id.next_button);
        buttonPrevious = (ImageView) findViewById(R.id.previous_button);
        seekBar = (SeekBar) findViewById(R.id.nowPlayingSeekBar);
        bufferingTextView = (TextView) findViewById(R.id.buffering);
        AppGlobals.initializeAllDataSets();
        new GetSoundDetailsTask().execute();
         seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
             boolean seek = false;

             @Override
             public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 seek = fromUser;
             }

             @Override
             public void onStartTrackingTouch(SeekBar seekBar) {
             }

             @Override
             public void onStopTrackingTouch(SeekBar seekBar) {
                 if (seek) {
                     PlayService.sMediaPlayer.seekTo((int) TimeUnit.SECONDS.toMillis(seekBar.getProgress()));
                     PlayService.sMediaPlayer.start();
                 }
             }
         });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = AppGlobals.getStreamUrlsHashMap().
                        get(Integer.valueOf(String.valueOf(parent.getItemAtPosition(position))));
                String formattedUrl = String.format("%s%s%s", url,
                        AppGlobals.ADD_CLIENT_ID, AppGlobals.CLIENT_KEY);
                songLength = Integer.valueOf(AppGlobals.getDurationHashMap()
                        .get(Integer.valueOf(String.valueOf(parent.getItemAtPosition(position)))));
                songLengthInSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(songLength);
                System.out.println(songLengthInSeconds);
                updateValue = songLengthInSeconds / 100.00;
                seekBar.setMax(songLengthInSeconds);
                animateBottomUp();
                UpdateUiHelpers.setSeekBarIndeterminate();
                seekBar.setProgress(0);
                AppGlobals.setSongCompleteStatus(false);
                Intent intent = new Intent(getApplicationContext(), PlayService.class);
                intent.putExtra(AppGlobals.SOUND_URL, formattedUrl);
                startService(intent);
            }
        });
        mPlayerControl.setOnClickListener(this);
    }

    public void animateBottomUp() {
        if (!AppGlobals.getControlsVisibility()) {
            Animation bottomUp = AnimationUtils.loadAnimation(MainActivity.this,
                    R.anim.bottom_up);
            controls_layout.startAnimation(bottomUp);
            controls_layout.setVisibility(View.VISIBLE);
            AppGlobals.setControlsVisible(true);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_pause_button:
                PlayService.togglePlayPause();
                break;
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
