package com.sickofyou.spotifystreamerpart1;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;
import retrofit.RetrofitError;

public class ArtistFragment extends Fragment {

    private final String TAG = ArtistFragment.class.getSimpleName();
    private ArrayList<SpotifyArtist> mSpotifyArtists;
    private SpotifyArtistAdapter mArtistAdapter;
    private ListView mListView;
    private String mSearchQueryText;

    public ArtistFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final EditText searchText = (EditText) rootView.findViewById(R.id.search_edittext);

        mListView = (ListView) rootView.findViewById(R.id.search_list_view);

        // Check rotation? or should I put in overridden OnCreate?
        if (savedInstanceState != null) {
            mSpotifyArtists = savedInstanceState.getParcelableArrayList(getString(R.string.saved_artist_list));
        } else {
            mSpotifyArtists = new ArrayList<>();
        }

        bindView();

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    mSearchQueryText = searchText.getText().toString();
                    searchForArtist(mSearchQueryText);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
                    handled = true;
                }
                return handled;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SpotifyArtist artist = mArtistAdapter.getItem(position);
                String artistId = artist.getId();
                Intent intent = new Intent(getActivity(), TopTenActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, artistId)
                        .putExtra("artistName", artist.getName());

                startActivity(intent);
            }
        });
        return rootView;
    }

    private void bindView() {
        mArtistAdapter = new SpotifyArtistAdapter(getActivity(), mSpotifyArtists);
        mListView.setAdapter(mArtistAdapter);
    }

    // Used solution from
    // http://stackoverflow.com/questions/16692536/good-solution-to-retain-listview-items-when-user-rotate-phone-and-keep-all-data
    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        if (savedState != null) {
            savedState.putParcelableArrayList(getString(R.string.saved_artist_list), mSpotifyArtists);
            Log.d(TAG, "saved " + !(mSpotifyArtists.isEmpty()));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mSpotifyArtists = savedInstanceState.getParcelableArrayList(getString(R.string.saved_artist_list));
            bindView();
        }
    }

    private void searchForArtist(String artistName) {
        new SearchTask().execute(artistName);
    }

    private class SearchTask extends AsyncTask<String, Void, List<Artist>> {

        @Override
        protected List<Artist> doInBackground(String... params) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotifyService = api.getService();
            List<Artist> artists;
            try {
                ArtistsPager artistPager = spotifyService.searchArtists(params[0]);
                Pager<Artist> pager = artistPager.artists;
                artists = pager.items;
            } catch (RetrofitError e) {
                SpotifyError error = SpotifyError.fromRetrofitError(e);
                error.getErrorDetails();
                Log.e(TAG, error.getMessage());
                return null;
            }

            return artists;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {

            super.onPostExecute(artists);
            String url;
            mArtistAdapter.clear();

            for (Artist a : artists) {
                try {
                    int width = a.images.get(0).width;
                    if (width > 300) {
                        url = a.images.get(1).url;
                    } else {
                        url = a.images.get(0).url;
                    }
                } catch (Exception e) {
                    url = null;
                }
                mSpotifyArtists.add(new SpotifyArtist(a.name, url, a.id));
            }

            if (mSpotifyArtists.isEmpty()) {
                Toast.makeText(getActivity(), getString(R.string.no_artist_found), Toast.LENGTH_SHORT).show();
            }

            mArtistAdapter.addAll(mSpotifyArtists);
        }
    }
}
