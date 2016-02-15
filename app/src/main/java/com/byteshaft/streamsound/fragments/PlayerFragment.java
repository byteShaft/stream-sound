package com.byteshaft.streamsound.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.byteshaft.streamsound.R;

public class PlayerFragment extends Fragment {

    private View mBaseView;
    public SeekBar seekBar;
    public TextView buffer;
    public ImageButton previous;
    public ImageButton next;
    public ImageButton play_pause;
    public TextView time_progress;
    private static PlayerFragment sInstance;

    public static PlayerFragment getsInstance() {
        return sInstance;
    }

    public static PlayerFragment getFragment() {
        PlayerFragment fragment = new PlayerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.player_fragment, container, false);
        System.out.println("PlayerFragment");
        sInstance = this;
        time_progress = (TextView) mBaseView.findViewById(R.id.time_progress);
        seekBar = (SeekBar) mBaseView.findViewById(R.id.seekBar);
        buffer = (TextView) mBaseView.findViewById(R.id.buffer);
        previous = (ImageButton) mBaseView.findViewById(R.id.previous);
        next = (ImageButton) mBaseView.findViewById(R.id.next);
        play_pause = (ImageButton) mBaseView.findViewById(R.id.play_pause);
        return mBaseView;
    }
}
