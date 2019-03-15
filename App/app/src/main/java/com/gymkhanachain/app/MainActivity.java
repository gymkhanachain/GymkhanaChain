package com.gymkhanachain.app;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements MapFragment.OnMapFragmentInteractionListener {

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MapFragment mapFragment = new MapFragment();
        fragmentTransaction.add(R.id.placeholder_main, mapFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onMapFragmentInteraction(Uri uri) {

    }
}
