package com.gymkhanachain.app.ui.playgymkhana.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gymkhanachain.app.R;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapFragment;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapPoint;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayGymkhanaActivity extends AppCompatActivity implements MapFragment.OnMapFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_gymkhana);
        ButterKnife.bind(this);

        // Create map fragment
        final Fragment map = MapFragment.newInstance(MapFragment.GYMKHANA_POINTS, new ArrayList<MapPoint>());
        getSupportFragmentManager().beginTransaction().add(R.id.map_content, map).commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onMapFragmentInteraction() {

    }
}
