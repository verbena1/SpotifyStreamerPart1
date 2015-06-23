package com.sickofyou.spotifystreamerpart1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by dtorres on 06/22/2015.
 */
public class ArtistTopTenAdapter extends ArrayAdapter<TrackInfo> {

    private ArrayList<TrackInfo> mTrackInfo = new ArrayList<>();

    public ArtistTopTenAdapter(Context context, ArrayList<TrackInfo> topTenArrayList) {
        super(context, 0, topTenArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrackInfo trackInfo = mTrackInfo.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.track_list_item, parent, false);
            viewHolder.trackNameView = (TextView) convertView.findViewById(R.id.track_item_textview);
            viewHolder.albumNameView = (TextView) convertView.findViewById(R.id.track_album_item_textview);
            viewHolder.trackImageView = (ImageView) convertView.findViewById(R.id.track_item_imageview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.trackNameView.setText(trackInfo.getTrackName());
        viewHolder.albumNameView.setText(trackInfo.getAlbumName());
        Picasso.with(getContext()).load(trackInfo.getImageUrl()).resize(150, 150).placeholder(R.mipmap.ic_launcher).into(viewHolder.trackImageView);
        return convertView;
    }

    public void addAll(ArrayList<TrackInfo> tracks) {
        for (TrackInfo track : tracks) {
            mTrackInfo.add(track);
        }
        setNotifyOnChange(true);
        this.notifyDataSetChanged();
    }

    @Override
    public void clear() {
        super.clear();
        mTrackInfo.clear();
    }

    private class ViewHolder {
        TextView trackNameView;
        TextView albumNameView;
        ImageView trackImageView;
    }
}
