package com.byteshaft.streamsound.utils;

import android.media.MediaPlayer;

import com.byteshaft.streamsound.UpdateUiHelpers;
import com.byteshaft.streamsound.service.NotificationService;
import com.byteshaft.streamsound.service.PlayService;

public class CustomMediaPlayer extends MediaPlayer {

    @Override
    public void start() throws IllegalStateException {
        if (UpdateUiHelpers.getSeekbarIndeterminateStatus()) {
            UpdateUiHelpers.removeSeekBarIndeterminate();
        }
        super.start();
        NotificationService.getsInstance().showNotification();
        UpdateUiHelpers.updateUiOnPlayerStart();
    }


    @Override
    public void stop() throws IllegalStateException {
        super.stop();
        PlayService.getInstance().stopSelf();
    }

    @Override
    public void pause() throws IllegalStateException {
        super.pause();
        UpdateUiHelpers.updateUiOnPause();
        NotificationService.getsInstance().showNotification();
    }
}
