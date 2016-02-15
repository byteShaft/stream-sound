package com.byteshaft.streamsound;


import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.byteshaft.streamsound.fragments.PlayerListFragment;
import com.byteshaft.streamsound.utils.AppGlobals;

public class UpdateUiHelpers extends ContextWrapper{

    public UpdateUiHelpers(Context base) {
        super(base);
    }

    public static void updateUiOnCompletion() {
        PlayerListFragment playerFragment = com.byteshaft.streamsound.fragments.PlayerListFragment.getInstance();
        if (playerFragment != null) {
            playerFragment.mPlayerControl.setImageResource(R.drawable.play_light);
            Animation bottomDown = AnimationUtils.loadAnimation(AppGlobals.getContext(),
                    R.anim.bottom_down);
            playerFragment.controls_layout.startAnimation(bottomDown);
            playerFragment.controls_layout.setVisibility(View.GONE);
            AppGlobals.setControlsVisible(false);
        }
    }

    public static void updateUiOnPause() {
        PlayerListFragment PlayerListFragment = com.byteshaft.streamsound.fragments.PlayerListFragment.getInstance();
        if (PlayerListFragment != null) {
            PlayerListFragment.mPlayerControl.setImageResource(R.drawable.play_light);
        }
    }

    public static void updateUiOnPlayerStart() {
        PlayerListFragment PlayerListFragment = com.byteshaft.streamsound.fragments.PlayerListFragment.getInstance();
        if (PlayerListFragment != null) {
            PlayerListFragment.mPlayerControl.setImageResource(R.drawable.pause_light);
        }
    }

    public static void updateSeekBarOnProgress() {
        PlayerListFragment PlayerListFragment = com.byteshaft.streamsound.fragments.PlayerListFragment.getInstance();
        if (PlayerListFragment != null) {
            int progressValue = PlayerListFragment.seekBar.getProgress();
            PlayerListFragment.seekBar.setProgress(progressValue
                    + (PlayerListFragment.updateValue / 2));
            PlayerListFragment.timeTextView.setText(secondToMinutes(progressValue
                    + (PlayerListFragment.updateValue / 2)));
        }
    }

    private static String secondToMinutes(int second) {
        int minutes = second / 60;
        int seconds = second % 60;
        if (seconds < 10) {
            return minutes + ":0" + seconds;
        } else {
            return minutes + ":" + seconds;
        }

    }

    public static void setSeekBarIndeterminate() {
        PlayerListFragment PlayerListFragment = com.byteshaft.streamsound.fragments.PlayerListFragment.getInstance();
        if (PlayerListFragment != null) {
            PlayerListFragment.seekBar.setIndeterminate(true);
            PlayerListFragment.bufferingTextView.setText("Buffering...");
        }
    }

    public static void removeSeekBarIndeterminate() {
        PlayerListFragment PlayerListFragment = com.byteshaft.streamsound.fragments.PlayerListFragment.getInstance();
        if (PlayerListFragment != null) {
            PlayerListFragment.seekBar.setIndeterminate(false);
            PlayerListFragment.bufferingTextView.setText(
                    AppGlobals.getTitlesHashMap().get(AppGlobals.getCurrentPlayingSong()));
        }
    }

    public static boolean getSeekbarIndeterminateStatus() {
        PlayerListFragment PlayerListFragment = com.byteshaft.streamsound.fragments.PlayerListFragment.getInstance();
        if (PlayerListFragment != null) {
            return PlayerListFragment.seekBar.isIndeterminate();
        }
        return false;
    }
}
