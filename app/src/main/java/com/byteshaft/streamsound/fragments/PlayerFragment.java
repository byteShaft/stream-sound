package com.byteshaft.streamsound.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.byteshaft.streamsound.R;
import com.byteshaft.streamsound.service.PlayService;
import com.byteshaft.streamsound.utils.AppGlobals;

import java.util.concurrent.TimeUnit;

public class PlayerFragment extends Fragment implements View.OnClickListener {

    private View mBaseView;
    public SeekBar seekBar;
    public TextView buffer;
    public ImageButton previous;
    public ImageButton next;
    public ImageButton play_pause;
    public TextView time_progress;
    public ImageView imageArt;
    private static PlayerFragment sInstance;

    public static PlayerFragment getsInstance() {
        return sInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.player_fragment, container, false);
        sInstance = this;
        time_progress = (TextView) mBaseView.findViewById(R.id.time_progress);
        seekBar = (SeekBar) mBaseView.findViewById(R.id.seekBar);
        buffer = (TextView) mBaseView.findViewById(R.id.buffer);
        previous = (ImageButton) mBaseView.findViewById(R.id.previous);
        next = (ImageButton) mBaseView.findViewById(R.id.next);
        play_pause = (ImageButton) mBaseView.findViewById(R.id.play_pause);
        imageArt = (ImageView) mBaseView.findViewById(R.id.imageArt);
        play_pause.setOnClickListener(this);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (AppGlobals.getCurrentPlayingSongBitMap() != null) {
            imageArt.setImageBitmap(AppGlobals.getCurrentPlayingSongBitMap());
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean seek = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seek = fromUser;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seek) {
                    PlayService.sMediaPlayer.seekTo((int) TimeUnit.SECONDS.toMillis(seekBar.getProgress()));
                    PlayService.sMediaPlayer.start();
                }
            }
        });
        if (AppGlobals.getCurrentPlayingSongBitMap() != null) {
            imageArt.setImageBitmap(AppGlobals.getCurrentPlayingSongBitMap());
        } else {
            imageArt.setImageResource(R.drawable.ic_launcher);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_pause:
                PlayService.togglePlayPause();
                break;
            case R.id.next:
                PlayerListFragment.getInstance().nextSong();
                break;
            case R.id.previous:
                PlayerListFragment.getInstance().previousSong();
                break;
        }

    }
}
