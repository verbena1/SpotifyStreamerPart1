package com.sickofyou.spotifystreamerpart1;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;

public class TopTenActivityFragment extends Fragment {

    private static final String TAG = TopTenActivityFragment.class.getSimpleName();
    private ArrayList<TrackInfo> mTrackInfoList;
    private ArtistTopTenAdapter mArtistTopTenAdapter;
    private ListView mListView;

    public TopTenActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                String name = intent.getExtras().getString(Intent.EXTRA_TEXT);
                searchTopTen(name);
            }
        } else {
            mTrackInfoList = savedInstanceState.getParcelableArrayList(getString(R.string.saved_track_list));
        }
        bindView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_top_ten, container, false);

        mListView = (ListView) rootView.findViewById(R.id.top_ten_listview);
        mTrackInfoList = new ArrayList<>();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrackInfo trackInfo = mArtistTopTenAdapter.getItem(position);
                String trackName = trackInfo.getTrackName();
                String imageUrl = trackInfo.getImageUrl();
                String trackUrl = trackInfo.getTrackUrl();
                String artistName = trackInfo.getArtistName();

                Log.d(TAG, "\n"+
                        "TrackName: "+trackName+"\n"+
                        "Image URL: "+imageUrl+"\n"+
                        "Track URL: "+trackUrl+"\n"+
                        "Artist Name: "+artistName);

                // TODO: Implement Player activity/fragment
                Toast.makeText(getActivity(), getString(R.string.not_implemented), Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    private void bindView() {
        mArtistTopTenAdapter = new ArtistTopTenAdapter(
                getActivity(),
                mTrackInfoList
        );
        mListView.setAdapter(mArtistTopTenAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(getString(R.string.saved_track_list), mTrackInfoList);
        Log.d(TAG, "saved " + !(mTrackInfoList.isEmpty()));
        super.onSaveInstanceState(outState);
    }

    private void searchTopTen(String name) {
        new TopTenTask().execute(name);
    }

    private class TopTenTask extends AsyncTask<String, Void, Tracks> {

        @Override
        protected Tracks doInBackground(String... params) {

            SpotifyApi api = new SpotifyApi();
            SpotifyService service = api.getService();

            Map<String, Object> country = new HashMap<>();
            country.put("country", Locale.getDefault().getCountry());

            try {
                return service.getArtistTopTrack(params[0], country);
            }
            catch (RetrofitError e) {
                SpotifyError error = SpotifyError.fromRetrofitError(e);
                Log.e("track error", error.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Tracks tracks) {
            super.onPostExecute(tracks);
            mArtistTopTenAdapter.clear();
            if (tracks == null) {
                Log.d(TAG, "no tracks available");
                return;
            }
            for (Track track : tracks.tracks) {
                try {
                    String url;
                    // Check Artist image width and get appropriate image from JSON.
                    int width = track.album.images.get(0).width;
                    if (width > 300) {
                        url = track.album.images.get(1).url;
                    } else {
                        url = track.album.images.get(0).url;
                    }
                    mTrackInfoList.add(new TrackInfo(track.name, track.album.name, url, track.preview_url, track.artists.get(0).name));
                } catch (Exception e) {
                    mTrackInfoList.add(new TrackInfo(track.name, track.album.name, null, track.preview_url, track.artists.get(0).name));
                }
            }
            mArtistTopTenAdapter.addAll(mTrackInfoList);
        }
    }
}
