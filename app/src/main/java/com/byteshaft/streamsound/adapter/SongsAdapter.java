package com.byteshaft.streamsound.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.byteshaft.streamsound.R;
import com.byteshaft.streamsound.utils.AppGlobals;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongsAdapter extends ArrayAdapter {

    private int layoutResource;
    private ArrayList<Integer> songsList;
    private Context mContext;
    private ViewHolder holder;
    private Activity mActivity;

    public SongsAdapter(Context context, int resource, ArrayList<Integer> songsList,
                        Activity activity) {
        super(context, resource, songsList);
        this.songsList = songsList;
        mContext = context;
        mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.single_row, parent, false);
            holder = new ViewHolder();
            holder.songId = (TextView) convertView.findViewById(R.id.songId);
            holder.title = (TextView) convertView.findViewById(R.id.song_title);
            holder.thumbNail = (ImageView) convertView.findViewById(R.id.thumbnail);
            holder.artist = (TextView) convertView.findViewById(R.id.artist);
            holder.genre = (TextView) convertView.findViewById(R.id.genre);
            holder.duration = (TextView) convertView.findViewById(R.id.duration);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.songId.setText(String.valueOf(songsList.get(position)));
        if (AppGlobals.getSongArtistHashMap().containsKey(songsList.get(position))) {
            holder.artist.setText(AppGlobals.getSongArtistHashMap().get(songsList.get(position)));
        }
        if (AppGlobals.getDurationHashMap().containsKey(songsList.get(position))) {
            int milliseconds = Integer.valueOf(AppGlobals.getDurationHashMap().get(songsList.get(position)));
            int sec =  (milliseconds / 1000) % 60 ;
            int min =  ((milliseconds / (1000*60)) % 60);
            holder.duration.setText(min + ":" + sec);
        }
        if (AppGlobals.getTitlesHashMap().containsKey(songsList.get(position))) {
            holder.title.setText(AppGlobals.getTitlesHashMap().get(songsList.get(position)));
        }
        if (AppGlobals.getGenreHashMap().containsKey(songsList.get(position))) {
            holder.genre.setText(AppGlobals.getGenreHashMap().get(songsList.get(position)));
        }
        if (AppGlobals.getSongImageUrlHashMap().containsKey(songsList.get(position))) {
            Picasso.with(mActivity).load(AppGlobals.getSongImageUrlHashMap()
                    .get(songsList.get(position))).into(holder.thumbNail);
        }
        return convertView;
    }

    static class ViewHolder {
        public TextView songId;
        public ImageView thumbNail;
        public TextView artist;
        public TextView title;
        public TextView genre;
        public TextView duration;
    }
}