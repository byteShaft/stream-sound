package com.byteshaft.streamsound.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.byteshaft.streamsound.MainActivity;
import com.byteshaft.streamsound.R;
import com.byteshaft.streamsound.utils.AppGlobals;
import com.byteshaft.streamsound.utils.Constants;

public class NotificationService extends Service {

    private Notification status;
    private final String LOG_TAG = "NotificationService";
    private RemoteViews views;
    private RemoteViews bigViews;
    private static NotificationService sInstance;

    public static NotificationService getsInstance() {
        return sInstance;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sInstance = this;
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            showNotification();

        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
            Log.i(LOG_TAG, "Clicked Previous");
            com.byteshaft.streamsound.fragments.PlayerListFragment.getInstance().previousSong();
            showNotification();
        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            PlayService.togglePlayPause();
            showNotification();
            Log.i(LOG_TAG, "Clicked Play");
        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
            Log.i(LOG_TAG, "Clicked Next");
            com.byteshaft.streamsound.fragments.PlayerListFragment.getInstance().nextSong();
            showNotification();
        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    public void showNotification() {
        // Using RemoteViews to bind custom layouts into Notification
        views = new RemoteViews(getPackageName(),
                R.layout.status_bar);
        bigViews = new RemoteViews(getPackageName(),
                R.layout.status_bar_expanded);
        // showing default album image
        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
        bigViews.setImageViewBitmap(R.id.status_bar_album_art, AppGlobals.getCurrentPlayingSongBitMap());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, NotificationService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, NotificationService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, NotificationService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, NotificationService.class);
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

        if (!PlayService.sMediaPlayer.isPlaying()) {
            views.setImageViewResource(R.id.status_bar_play,
                    R.drawable.apollo_holo_dark_play);
            bigViews.setImageViewResource(R.id.status_bar_play,
                    R.drawable.apollo_holo_dark_play);
        } else {
            views.setImageViewResource(R.id.status_bar_play,
                    R.drawable.apollo_holo_dark_pause);
            bigViews.setImageViewResource(R.id.status_bar_play,
                    R.drawable.apollo_holo_dark_pause);
        }

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
        status.icon = R.drawable.ic_launcher;
        status.contentIntent = pendingIntent;
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
    }
}
