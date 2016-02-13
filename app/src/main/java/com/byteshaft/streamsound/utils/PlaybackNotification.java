package com.byteshaft.streamsound.utils;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.byteshaft.streamsound.R;

public class PlaybackNotification extends ContextWrapper {

    private NotificationManager mNotificationManager;
    private static PlaybackNotification sInstance;

    static PlaybackNotification getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PlaybackNotification(context);
            return sInstance;
        } else {
            return sInstance;
        }
    }

    private PlaybackNotification(Context base) {
        super(base);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    void show() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext());
        setupVisuals(builder);
        setOnTapIntentAction(builder);
        mNotificationManager.notify(AppGlobals.NOTIFICATION_ID, builder.build());
    }

    void remove() {
        if (mNotificationManager != null) {
            mNotificationManager.cancel(AppGlobals.NOTIFICATION_ID);
        }
    }

    private void setupVisuals(NotificationCompat.Builder builder) {
        builder.setContentTitle("Shoutcast");
        builder.setContentText("Tap to open app");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        // dismiss notification when its tapped.
        builder.setAutoCancel(false);
        // disable slide to remove for the notification.
        builder.setOngoing(false);
    }

    private void setOnTapIntentAction(NotificationCompat.Builder builder) {
        Intent intent = new Intent("byteshaft.com.shoutcast.OPEN_ACTIVITY");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
    }
}
