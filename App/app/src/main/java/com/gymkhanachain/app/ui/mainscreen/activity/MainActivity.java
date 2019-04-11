package com.gymkhanachain.app.ui.mainscreen.activity;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.gymkhanachain.app.ui.commons.dialogs.LocationDialog;
import com.gymkhanachain.app.ui.commons.fragments.LoginFragment;
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
    private static final String LOGIN_FRAGMENT_TAG = "LoginFragment";
    private static final String LIST_GYMK_FRAGMENT_TAG = "ListGymkFragment";
    private static final String INFO_GYMK_FRAGMENT_TAG = "GymkInfoFragment";


    // Tag para identificar los permisos
    public static final int REQUEST_MY_LOCATION = 0x666;

    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 9001;

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


//Login TODO: ilopez: Trasladar las clases de login y autenticación a main activity
        //(y borrar la barull que esta esparcida por el código)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account == null) {

           // DialogFragment dialogLog = new LoginFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            /*
            fragments.add(dialogLog);
            fragmentManager.beginTransaction().add(dialogLog,LOGIN_FRAGMENT_TAG )
            .commit();
            */
            DialogFragment dialogLog = new LoginFragment();
            dialogLog.showNow(fragmentManager,LOGIN_FRAGMENT_TAG);
            //dialogLog.show(fragmentManager, "loginRequired");
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_DENIED) {
                DialogFragment dialog = new LocationDialog();
                dialog.show(fragmentManager, "requestLocation");
            } else {
                setContent();
            }
        }



    }

    //1 Login In the activity's onClick method, handle sign-in button taps by creating a sign-in intent
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //Login 2: After the user signs in, you can get a GoogleSignInAccount object for the user in the activity's onActivityResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /*
        if (requestCode == LoginFragment.RC_SIGN_IN) {
            LoginFragment fragment = (LoginFragment) getFragmentManager()
                    .findFragmentById(R.id.);
            fragment.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        */

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            //handleSignInResult(task);
        }
    }

    private void closeApplication() {
        this.finish();
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

        switch (menuItem.getItemId()) {
            case R.id.nav_start: // MapFragment
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
            case R.id.nav_my_gymk: // Listar gymkhanas
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
            case R.id.nav_profile:
                startActivity(new Intent(this, UserProfileActivity.class));
                break;
            case R.id.nav_settings:
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


