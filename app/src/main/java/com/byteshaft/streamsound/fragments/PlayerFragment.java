package com.byteshaft.streamsound.fragments;

import android.content.Intent;
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
import com.byteshaft.streamsound.service.NotificationService;
import com.byteshaft.streamsound.service.PlayService;
import com.byteshaft.streamsound.utils.AppGlobals;
import com.byteshaft.streamsound.utils.Constants;

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
                if (seek && PlayService.sMediaPlayer != null && PlayService.sMediaPlayer.isPlaying()) {
                    AppGlobals.setChangeFromPlayer(true);
                    PlayService.sMediaPlayer.seekTo((int) TimeUnit.SECONDS.toMillis(seekBar.getProgress()));
                    PlayService.sMediaPlayer.start();
                }
            }
        });
        if (AppGlobals.getCurrentPlayingSongBitMap() != null) {
            imageArt.setImageBitmap(AppGlobals.getCurrentPlayingSongBitMap());
        } else {
            imageArt.setImageResource(R.drawable.default_song);
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
                if (PlayService.sMediaPlayer != null) {
                    PlayService.togglePlayPause();
                    if (!AppGlobals.isNotificationVisible()) {
                        Intent notificationIntent = new Intent(AppGlobals.getContext(), NotificationService.class);
                        notificationIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                        getActivity().startService(notificationIntent);
                    }
                }
                if (!AppGlobals.isRunningFromList() && !AppGlobals.isRunningFirstSong()) {
                    AppGlobals.setRunningFirstSong(true);
                    String url = AppGlobals.getStreamUrlsHashMap().
                            get(AppGlobals.getsSongsIdsArray().get(0));
                    String formattedUrl = String.format("%s%s%s", url,
                            AppGlobals.ADD_CLIENT_ID, AppGlobals.CLIENT_KEY);
                    AppGlobals.setCurrentPlayingSong(AppGlobals.getsSongsIdsArray().get(0));
                    PlayerListFragment.getInstance().songLength = Integer.valueOf(AppGlobals.getDurationHashMap()
                            .get(AppGlobals.getsSongsIdsArray().get(0)));
                    PlayerListFragment.getInstance().playSong(formattedUrl);
                }
                break;
            case R.id.next:
                if (PlayService.sMediaPlayer != null) {
                    PlayerListFragment.getInstance().nextSong();
                }
                break;
            case R.id.previous:
                if (PlayService.sMediaPlayer != null) {
                    PlayerListFragment.getInstance().previousSong();
                }
                break;
        }
    }
}
