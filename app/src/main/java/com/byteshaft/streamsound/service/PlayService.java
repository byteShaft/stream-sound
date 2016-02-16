package com.byteshaft.streamsound.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;

import com.byteshaft.streamsound.UpdateUiHelpers;
import com.byteshaft.streamsound.fragments.PlayerListFragment;
import com.byteshaft.streamsound.utils.AppGlobals;
import com.byteshaft.streamsound.utils.Constants;
import com.byteshaft.streamsound.utils.CustomMediaPlayer;

import java.io.IOException;

public class PlayService extends Service {

    public static CustomMediaPlayer sMediaPlayer;
    public static boolean songPlaying = false;
    public static Handler updateHandler;
    public static Runnable timerRunnable;
    private static PlayService sInstance;

    public static PlayService getInstance() {
        return sInstance;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(AppGlobals.getContext(), NotificationService.class);
        notificationIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        AppGlobals.getContext().startService(notificationIntent);
        sInstance = this;
        String url = intent.getStringExtra(AppGlobals.SOUND_URL);
        sMediaPlayer = new CustomMediaPlayer();
        updateHandler = new Handler();
        sMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        playSong(url);
        sMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                togglePlayPause();
            }
        });

        sMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mp.release();
                return false;
            }
        });

        sMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                UpdateUiHelpers.updateUiOnCompletion();
                PlayerListFragment.getInstance().nextSong();
            }
        });
        timerRunnable = new Runnable() {
            public void run() {
                if (!AppGlobals.isSongCompleted()) {
                    AudioManager ar = (AudioManager) AppGlobals.getContext().getSystemService(AUDIO_SERVICE);
                    if (ar.isMusicActive() && songPlaying) {
                        UpdateUiHelpers.updateSeekBarOnProgress();
                    }
                }
                updateHandler.postDelayed(this, 1000);
            }
        };
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void playSong(String formattedUrl) {
        if (sMediaPlayer.isPlaying()) {
            sMediaPlayer.stop();
        }
        sMediaPlayer.reset();
        try {
            sMediaPlayer.setDataSource(formattedUrl);
            sMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void togglePlayPause() {
        if (sMediaPlayer.isPlaying()) {
            sMediaPlayer.pause();
            songPlaying = false;
            updateHandler.removeCallbacks(timerRunnable);
        } else {
            sMediaPlayer.start();
            songPlaying = true;
            updateHandler.postDelayed(timerRunnable, 10);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sMediaPlayer != null) {
            if (sMediaPlayer.isPlaying()) {
                sMediaPlayer.stop();
            }
            sMediaPlayer.release();
            sMediaPlayer = null;
        }
        NotificationService.getsInstance().stopSelf();
    }


}
