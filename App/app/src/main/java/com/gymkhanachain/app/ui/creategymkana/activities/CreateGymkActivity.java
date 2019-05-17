package com.gymkhanachain.app.ui.creategymkana.activities;

import android.location.Location;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.gymkhanachain.app.R;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapFragment;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapPoint;
import com.gymkhanachain.app.ui.creategymkana.fragments.GymkDetailsFragment;
import com.gymkhanachain.app.ui.creategymkana.fragments.GymkPointsFragment;
import com.gymkhanachain.app.ui.creategymkana.fragments.GymkPrivacyFragment;

import java.util.ArrayList;
import java.util.List;

public class CreateGymkActivity extends AppCompatActivity
        implements GymkDetailsFragment.OnFragmentInteractionListener,
        GymkPointsFragment.OnFragmentInteractionListener,
        GymkPrivacyFragment.OnFragmentInteractionListener,
        MapFragment.OnMapFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean useDarkTheme = preferences.getBoolean("activate_dark_theme", false);

        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gymk);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMapSearchButtonClick() {

    }

    @Override
    public void onMapAccesibilityFilterClick() {

    }

    @Override
    public void onMapLongClick(LatLng point) {

    }

    @Override
    public void onMapChangePosition(LatLngBounds bounds, Location position) {

    }

    @Override
    public void onMapChangeCamera(LatLngBounds bounds, CameraPosition position) {

    }

    @Override
    public void onMapPointClick(MapPoint point) {

    }

    @Override
    public void onMapPointMove(MapPoint point) {

    }

    @Override
    public void onMapPointsNearLocation(List<MapPoint> points) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    /* TODO Deberíamos instanciar estos fragmentos utilizando el newInstance() pasándole parámetros
     *  Así podemos instanciar MapFragment desde donde queramos utilizando distintas funciones en cada caso.
        */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        List<String> fragments;

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            fragments = new ArrayList<>();
            fragments.add(GymkDetailsFragment.class.getName());
            fragments.add(MapFragment.class.getName());
            fragments.add(GymkPrivacyFragment.class.getName());
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            return Fragment.instantiate(getApplicationContext(), fragments.get(position));
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Detalles";
                case 1:
                    return "Puntos";
                case 2:
                    return "Privacidad";
            }
            return null;
        }
    }
}
