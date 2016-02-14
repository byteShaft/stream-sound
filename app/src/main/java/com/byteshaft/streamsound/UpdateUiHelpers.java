package com.byteshaft.streamsound;


import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.byteshaft.streamsound.utils.AppGlobals;

public class UpdateUiHelpers extends ContextWrapper{

    public UpdateUiHelpers(Context base) {
        super(base);
    }

    public static void updateUiOnCompletion() {
        MainActivity mainActivity = MainActivity.getInstance();
        if (mainActivity != null) {
            mainActivity.mPlayerControl.setImageResource(R.drawable.play_light);
            Animation bottomDown = AnimationUtils.loadAnimation(AppGlobals.getContext(),
                    R.anim.bottom_down);
            mainActivity.controls_layout.startAnimation(bottomDown);
            mainActivity.controls_layout.setVisibility(View.GONE);
            AppGlobals.setControlsVisible(false);
        }
    }

    public static void updateUiOnPause() {
        MainActivity mainActivity = MainActivity.getInstance();
        if (mainActivity != null) {
            mainActivity.mPlayerControl.setImageResource(R.drawable.play_light);
        }
    }

    public static void updateUiOnPlayerStart() {
        MainActivity mainActivity = MainActivity.getInstance();
        if (mainActivity != null) {
            mainActivity.mPlayerControl.setImageResource(R.drawable.pause_light);
        }
    }

    public static void updateSeekBarOnProgress() {
        MainActivity mainActivity = MainActivity.getInstance();
        if (mainActivity != null) {
            mainActivity.seekBar.setProgress(mainActivity.seekBar.getProgress()
                    + (mainActivity.updateValue / 2));
            mainActivity.timeTextView.setText(secondToMinutes(mainActivity.seekBar.getProgress()
                    + (mainActivity.updateValue / 2)));
        }
    }

    private static String secondToMinutes(int second) {
        int minutes = second / 60;
        int seconds = second % 60;
        System.out.println(minutes);
        System.out.println(seconds);
        return minutes + ":" + seconds;

    }

    public static void setSeekBarIndeterminate() {
        MainActivity mainActivity = MainActivity.getInstance();
        if (mainActivity != null) {
            mainActivity.seekBar.setIndeterminate(true);
            mainActivity.bufferingTextView.setText("Buffering...");
        }
    }

    public static void removeSeekBarIndeterminate() {
        MainActivity mainActivity = MainActivity.getInstance();
        if (mainActivity != null) {
            mainActivity.seekBar.setIndeterminate(false);
            mainActivity.bufferingTextView.setText(
                    AppGlobals.getTitlesHashMap().get(AppGlobals.getCurrentPlayingSong()));
        }
    }

    public static boolean getSeekbarIndeterminateStatus() {
        MainActivity mainActivity = MainActivity.getInstance();
        if (mainActivity != null) {
            return mainActivity.seekBar.isIndeterminate();
        }
        return false;
    }
}
