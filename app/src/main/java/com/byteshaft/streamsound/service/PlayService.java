package com.byteshaft.streamsound.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.byteshaft.streamsound.UpdateUiHelpers;
import com.byteshaft.streamsound.utils.AppGlobals;
import com.byteshaft.streamsound.utils.CustomMediaPlayer;

import java.io.IOException;

public class PlayService extends Service {

    public static CustomMediaPlayer sMediaPlayer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra(AppGlobals.SOUND_URL);
        sMediaPlayer = new CustomMediaPlayer();
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
            }
        });
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
        } else {
            sMediaPlayer.start();
            MediaObserver mediaObserver = new MediaObserver();
            new Thread(mediaObserver).start();
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
    }

    private static class MediaObserver implements Runnable {

        @Override
        public void run() {
            while (!AppGlobals.isSongCompleted()) {
                AudioManager ar = (AudioManager) AppGlobals.getContext().getSystemService(AUDIO_SERVICE);
                if (ar.isMusicActive()) {
                    UpdateUiHelpers.updateSeekBarOnProgress();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
