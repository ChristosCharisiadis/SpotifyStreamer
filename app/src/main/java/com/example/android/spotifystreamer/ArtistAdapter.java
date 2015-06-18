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


/**
 * ArtistAdapter.java - Class for a custom adapter for the artists
 */
public class ArtistAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Artist> artists;

    /**
     * ViewHolder.class - class to hold the views that the adapter inflates
     */
    private class ViewHolder {
        TextView mTextView;
        ImageView mImageView;
    }

    public ArtistAdapter(Context context, ArrayList<Artist> artists) {
        inflater = LayoutInflater.from(context);
        this.artists = artists;
    }


    @Override
    public int getCount() {
        return artists.size();
    }

    @Override
    public Object getItem(int position) {
        return artists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.artist_layout, null);
            holder.mTextView = (TextView) convertView.findViewById(R.id.artist_name);
            holder.mImageView = (ImageView) convertView.findViewById(R.id.artist_image);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTextView.setText(artists.get(position).getName());
        Picasso.with(parent.getContext()).load(artists.get(position).getImage()).into(holder.mImageView);
        return convertView;
    }
}
