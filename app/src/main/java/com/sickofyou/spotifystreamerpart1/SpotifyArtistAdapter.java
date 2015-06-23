package com.sickofyou.spotifystreamerpart1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SpotifyArtistAdapter extends ArrayAdapter<SpotifyArtist> {

    private ArrayList<SpotifyArtist> mSpotifyArtists = new ArrayList<SpotifyArtist>();

    public SpotifyArtistAdapter(Context context, ArrayList<SpotifyArtist> artists) {
        super(context, 0, artists);
    }

    private static class ViewHolder {
        TextView artistNameView;
        ImageView artistImageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SpotifyArtist spotifyArtist = mSpotifyArtists.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.search_list_item, parent, false);

            viewHolder.artistNameView = (TextView) convertView.findViewById(R.id.spotify_item_textview);
            viewHolder.artistImageView = (ImageView) convertView.findViewById(R.id.spotify_item_imageview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.artistNameView.setText(spotifyArtist.getName());

        Glide.with(getContext()).load(spotifyArtist.getImageUrl()).override(150, 150).placeholder(R.mipmap.ic_launcher).into(viewHolder.artistImageView);

        return convertView;
    }

    public void addAll(ArrayList<SpotifyArtist> artists) {
        for (SpotifyArtist a : artists) {
            mSpotifyArtists.add(a);
        }
        setNotifyOnChange(true);
        this.notifyDataSetChanged();
    }

    @Override
    public void clear() {
        super.clear();
        mSpotifyArtists.clear();
    }
}
