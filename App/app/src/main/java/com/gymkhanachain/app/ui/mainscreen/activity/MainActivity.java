package com.gymkhanachain.app.ui.mainscreen.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gymkhanachain.app.ui.commons.dialogs.LocationDialog;
import com.gymkhanachain.app.ui.commons.fragments.MapFragment;
import com.gymkhanachain.app.R;
import com.gymkhanachain.app.ui.userprofile.activity.UserProfileActivity;
import com.gymkhanachain.app.ui.creategymkana.activity.CreateGymkActivity;
import com.gymkhanachain.app.ui.mainscreen.fragments.GymkInfoFragment;
import com.gymkhanachain.app.ui.mainscreen.fragments.ListGymkFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MapFragment.OnMapFragmentInteractionListener, ListGymkFragment.OnListGymkFragmentInteractionListener, GymkInfoFragment.OnGymkInfoFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {

    // Tags para identificar los distintos fragmentos de la Actividad
    private static final String MAP_FRAGMENT_TAG = "MapFragment";
    private static final String LIST_GYMK_FRAGMENT_TAG = "ListGymkFragment";
    private static final String INFO_GYMK_FRAGMENT_TAG = "GymkInfoFragment";

    // Tag para identificar los permisos
    public static final int REQUEST_MY_LOCATION = 0x666;


    // Elementos del NavigationDrawer
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    // Lista de los fragmentos creados que gestiona esta actividad
    List<Fragment> fragments;
    // La gestionamos con este FragmentManager, mediante transacciones
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_DENIED) {
            DialogFragment dialog = new LocationDialog();
            dialog.show(fragmentManager, "requestLocation");
        } else {
            setContent();
        }
    }

    private void setContent() {
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(getString(R.string.app_name));
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle("Drawer");
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        fragments = new ArrayList<>();

        // Al iniciar la Aplicación, cargamos el primer fragmento visible: MapFragment
        MapFragment mapFragment = new MapFragment();
        fragments.add(mapFragment);

        fragmentManager.beginTransaction()
                .add(R.id.placeholder_main, mapFragment, MAP_FRAGMENT_TAG)
                .commit();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    // Implementación de las interfaces de los fragmentos
    // TODO Hay que definir las interfaces de cada fragmento, en función de las interacciones con MainActivity
    @Override
    public void onMapFragmentInteraction() {
        Fragment fragment = fragmentManager.findFragmentByTag(INFO_GYMK_FRAGMENT_TAG);

        if (fragment != null && fragment.getTag().equals(INFO_GYMK_FRAGMENT_TAG)) {
            fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.placeholder_main, GymkInfoFragment.newInstance(), INFO_GYMK_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void onCreateGymkInteraction() {
        Intent intent = new Intent(this, CreateGymkActivity.class);
        startActivity(intent);
    }


    @Override
    public void onGymkInfoFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;

        // TODO Los elementos del switch deberían estar codificados en el archivo de strings
        switch (menuItem.getTitle().toString()) {
            case "Inicio": // MapFragment
                fragment = fragmentManager.findFragmentByTag(MAP_FRAGMENT_TAG);

                if (fragment != null) {
                    if (fragment.getTag().equals(MAP_FRAGMENT_TAG))
                        break;
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, fragment)
                            .commit();
                } else
                    fragmentManager.beginTransaction()
                            .replace(R.id.placeholder_main, MapFragment.newInstance(), MAP_FRAGMENT_TAG)
                            .commit();
                break;
            case "Mis gymkhanas": // Listar gymkhanas
                fragment = fragmentManager.findFragmentByTag(LIST_GYMK_FRAGMENT_TAG);

                if (fragment != null) {
                    if (fragment.getTag().equals(LIST_GYMK_FRAGMENT_TAG))
                        break;
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, fragment)
                            .commit();
                } else
                    fragmentManager.beginTransaction()
                            .replace(R.id.placeholder_main, ListGymkFragment.newInstance(), LIST_GYMK_FRAGMENT_TAG)
                            .commit();
                break;
            case "Perfil":
                startActivity(new Intent(this, UserProfileActivity.class));
                break;
            case "Ajustes":
                // TODO Aquí lanzamos SettingsActivity
                Toast.makeText(getApplicationContext(), "Lanzar ajustes", Toast.LENGTH_SHORT).show();
                break;
        }

        mDrawerLayout.closeDrawer(findViewById(R.id.nav_view));

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_MY_LOCATION: {
                if (grantResults.length > 0 || grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setContent();
                    mDrawerToggle.syncState();
                } else {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
            }
        }
    }
}


