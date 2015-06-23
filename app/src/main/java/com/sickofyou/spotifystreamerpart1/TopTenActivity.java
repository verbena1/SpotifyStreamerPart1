package com.sickofyou.spotifystreamerpart1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by dtorres on 06/22/2015.
 */
public class TopTenActivity extends AppCompatActivity {

    private String artistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            if (getIntent() != null && getIntent().hasExtra("artistName")) {
                artistName = getIntent().getStringExtra("artistName");
            } else {
                artistName = "";
            }
        } else {
            artistName = savedInstanceState.getString("artistName");
        }

        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setSubtitle(artistName);

        setContentView(R.layout.activity_top_ten);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top_ten, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("artistName", artistName);
    }
}
