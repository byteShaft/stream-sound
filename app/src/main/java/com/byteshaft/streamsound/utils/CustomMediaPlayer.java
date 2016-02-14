package com.byteshaft.streamsound.utils;

import android.media.MediaPlayer;

import com.byteshaft.streamsound.UpdateUiHelpers;
import com.byteshaft.streamsound.service.PlayService;

import java.io.IOException;

public class CustomMediaPlayer extends MediaPlayer {

    @Override
    public void start() throws IllegalStateException {
        try {
            PlayService.getInstance().showNotification();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (UpdateUiHelpers.getSeekbarIndeterminateStatus()) {
            UpdateUiHelpers.removeSeekBarIndeterminate();
        }
        super.start();
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
    }
}
