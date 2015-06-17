package com.example.android.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/*
Custom adapter to create the list view for the top 10 songs of an artist.
 */
public class SongAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Song> songs;

    private class ViewHolder {
        TextView songNameText;
        TextView albumNameText;
        ImageView mImageView;
    }

    public SongAdapter(Context context, ArrayList<Song> songs) {
        inflater = LayoutInflater.from(context);
        this.songs = songs;
    }


    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.song_layout, null);
            holder.songNameText = (TextView) convertView.findViewById(R.id.song_name);
            holder.albumNameText = (TextView) convertView.findViewById(R.id.song_album);
            holder.mImageView = (ImageView) convertView.findViewById(R.id.song_image);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.songNameText.setText(songs.get(position).getTrackName());
        holder.albumNameText.setText(songs.get(position).getAlbumName());
        Picasso.with(parent.getContext()).load(songs.get(position).getSmallImage()).into(holder.mImageView);
        return convertView;
    }
}
