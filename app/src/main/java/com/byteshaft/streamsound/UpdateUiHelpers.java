package com.byteshaft.streamsound;


import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.byteshaft.streamsound.fragments.PlayerFragment;
import com.byteshaft.streamsound.fragments.PlayerListFragment;
import com.byteshaft.streamsound.utils.AppGlobals;

public class UpdateUiHelpers extends ContextWrapper{

    public UpdateUiHelpers(Context base) {
        super(base);
    }

    public static void updateUiOnCompletion() {
        PlayerListFragment playerListFragment = com.byteshaft.streamsound.fragments.
                PlayerListFragment.getInstance();
        PlayerFragment fragment = com.byteshaft.streamsound.fragments.PlayerFragment.getsInstance();
        if (playerListFragment != null) {
            playerListFragment.mPlayerControl.setImageResource(R.drawable.play_light);
            Animation bottomDown = AnimationUtils.loadAnimation(AppGlobals.getContext(),
                    R.anim.bottom_down);
            playerListFragment.controls_layout.startAnimation(bottomDown);
            playerListFragment.controls_layout.setVisibility(View.GONE);
            AppGlobals.setControlsVisible(false);
        }
        if (fragment != null) {
            fragment.play_pause.setImageResource(R.drawable.play_light);
        }
    }

    public static void updateUiOnPause() {
        PlayerListFragment playerListFragment = com.byteshaft.streamsound.fragments.
                PlayerListFragment.getInstance();
        PlayerFragment fragment = com.byteshaft.streamsound.fragments.PlayerFragment.getsInstance();
        if (playerListFragment != null) {
            playerListFragment.mPlayerControl.setImageResource(R.drawable.play_light);
        }
        if (fragment != null) {
            fragment.play_pause.setImageResource(R.drawable.play_light);
        }
    }

    public static void updateUiOnPlayerStart() {
        PlayerListFragment playerListFragment = com.byteshaft.streamsound.fragments.
                PlayerListFragment.getInstance();
        PlayerFragment fragment = com.byteshaft.streamsound.fragments.PlayerFragment.getsInstance();
        if (playerListFragment != null) {
            playerListFragment.mPlayerControl.setImageResource(R.drawable.pause_light);
        }
        if (fragment!= null) {
            fragment.play_pause.setImageResource(R.drawable.pause_light);
        }
    }

    public static void updateSeekBarOnProgress() {
        PlayerListFragment playerListFragment = com.byteshaft.streamsound.fragments.
                PlayerListFragment.getInstance();
        PlayerFragment fragment = com.byteshaft.streamsound.fragments.PlayerFragment.getsInstance();
        if (playerListFragment != null) {
            int progressValue = playerListFragment.seekBar.getProgress();
            playerListFragment.seekBar.setProgress(progressValue
                    + (playerListFragment.updateValue / 2));
            playerListFragment.timeTextView.setText(secondToMinutes(progressValue
                    + (playerListFragment.updateValue / 2)));
        }

        if (fragment != null) {
            int progressValue = fragment.seekBar.getProgress();
            System.out.println(progressValue);
            fragment.seekBar.setProgress(progressValue
                    + (playerListFragment.updateValue / 2));
            fragment.time_progress.setText(secondToMinutes(progressValue
                    + (playerListFragment.updateValue / 2)));
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
        PlayerListFragment playerListFragment = com.byteshaft.streamsound.fragments.
                PlayerListFragment.getInstance();
        PlayerFragment fragment = com.byteshaft.streamsound.fragments.PlayerFragment.getsInstance();
        if (playerListFragment != null) {
            playerListFragment.seekBar.setIndeterminate(true);
            playerListFragment.bufferingTextView.setText("Buffering...");
        }
        if (fragment != null) {
            fragment.seekBar.setIndeterminate(true);
            fragment.buffer.setText("Buffering...");

        }
    }

    public static void removeSeekBarIndeterminate() {
        PlayerListFragment playerListFragment = com.byteshaft.streamsound.fragments.
                PlayerListFragment.getInstance();
        PlayerFragment fragment = com.byteshaft.streamsound.fragments.PlayerFragment.getsInstance();
        if (playerListFragment != null) {
            playerListFragment.seekBar.setIndeterminate(false);
            playerListFragment.bufferingTextView.setText(
                    AppGlobals.getTitlesHashMap().get(AppGlobals.getCurrentPlayingSong()));
        }
        if (fragment != null) {
            fragment.seekBar.setIndeterminate(false);
            fragment.buffer.setText(AppGlobals.getTitlesHashMap()
                    .get(AppGlobals.getCurrentPlayingSong()));
        }
    }

    public static boolean getSeekbarIndeterminateStatus() {
        PlayerListFragment playerListFragment = com.byteshaft.streamsound.fragments.
                PlayerListFragment.getInstance();
        if (playerListFragment != null) {
            return playerListFragment.seekBar.isIndeterminate();
        }
        return false;
    }
}
