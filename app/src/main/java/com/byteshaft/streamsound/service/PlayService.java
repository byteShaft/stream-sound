package com.byteshaft.streamsound.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.byteshaft.streamsound.MainActivity;
import com.byteshaft.streamsound.R;
import com.byteshaft.streamsound.UpdateUiHelpers;
import com.byteshaft.streamsound.utils.AppGlobals;
import com.byteshaft.streamsound.utils.Constants;
import com.byteshaft.streamsound.utils.CustomMediaPlayer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

public class PlayService extends Service {

    public static CustomMediaPlayer sMediaPlayer;
    public static boolean songPlaying = false;
    public static Handler updateHandler;
    public static Runnable timerRunnable;
    private Notification status;
    private final String LOG_TAG = "PlayService";
    private static PlayService sInstance;

    public static PlayService getInstance() {
        return sInstance;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sInstance = this;
        registerIntentsForNotification(intent);
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

    private void registerIntentsForNotification(Intent intent) {
        if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
            Log.i(LOG_TAG, "Clicked Previous");
            MainActivity.getInstance().previousSong();
        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            Log.i(LOG_TAG, "Clicked Play");
            togglePlayPause();
        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
            Log.i(LOG_TAG, "Clicked Next");
            MainActivity.getInstance().nextSong();
        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelf();
        }
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
            updateHandler.postDelayed(timerRunnable, 100);
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

    public void showNotification() throws IOException {
// Using RemoteViews to bind custom layouts into Notification
        RemoteViews views = new RemoteViews(getPackageName(),
                R.layout.status_bar);
        final RemoteViews bigViews = new RemoteViews(getPackageName(),
                R.layout.status_bar_expanded);

// showing default album image
        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
        Picasso.with(this).load(AppGlobals.getSongImageUrlHashMap()
                .get(AppGlobals.getCurrentPlayingSong())).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                bigViews.setImageViewBitmap(R.id.status_bar_album_art, bitmap);

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, PlayService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, PlayService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, PlayService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, PlayService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);

        views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);

        views.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);

        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        views.setImageViewResource(R.id.status_bar_play,
                R.drawable.apollo_holo_dark_pause);
        bigViews.setImageViewResource(R.id.status_bar_play,
                R.drawable.apollo_holo_dark_pause);

        views.setTextViewText(R.id.status_bar_track_name, AppGlobals.getTitlesHashMap()
                .get(AppGlobals.getCurrentPlayingSong()));
        bigViews.setTextViewText(R.id.status_bar_track_name, AppGlobals.getTitlesHashMap()
                .get(AppGlobals.getCurrentPlayingSong()));

        views.setTextViewText(R.id.status_bar_artist_name, AppGlobals.getSongArtistHashMap()
                .get(AppGlobals.getCurrentPlayingSong()));
        bigViews.setTextViewText(R.id.status_bar_artist_name, AppGlobals.getSongArtistHashMap()
                .get(AppGlobals.getCurrentPlayingSong()));

        bigViews.setTextViewText(R.id.status_bar_album_name, "Album Name");

        status = new Notification.Builder(this).build();
        status.contentView = views;
        status.bigContentView = bigViews;
        status.flags = Notification.FLAG_AUTO_CANCEL;
        status.icon = R.drawable.ic_notify;
        status.contentIntent = pendingIntent;
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
    }
}
