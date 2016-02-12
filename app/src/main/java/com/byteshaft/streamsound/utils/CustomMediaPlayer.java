package com.byteshaft.streamsound.utils;

import android.media.MediaPlayer;

import com.byteshaft.streamsound.UpdateUiHelpers;

public class CustomMediaPlayer extends MediaPlayer {

    @Override
    public void start() throws IllegalStateException {
        if (UpdateUiHelpers.getSeekbarIndeterminateStatus()) {
            UpdateUiHelpers.removeSeekBarIndeterminate();
        }
        super.start();
        UpdateUiHelpers.updateUiOnPlayerStart();
    }


    @Override
    public void stop() throws IllegalStateException {
        super.stop();
    }

    @Override
    public void pause() throws IllegalStateException {
        super.pause();
        UpdateUiHelpers.updateUiOnPause();
    }
}
