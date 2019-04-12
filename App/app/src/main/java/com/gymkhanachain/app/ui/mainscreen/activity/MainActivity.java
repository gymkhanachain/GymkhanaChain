package com.gymkhanachain.app.ui.mainscreen.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.gymkhanachain.app.model.beans.GymkhanaBean;
import com.gymkhanachain.app.model.commons.GymkhanaCache;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.gymkhanachain.app.R;
import com.gymkhanachain.app.ui.commons.dialogs.LocationDialog;
import com.gymkhanachain.app.ui.commons.fragments.LoginFragment;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapFragment;
import com.gymkhanachain.app.R;
import com.gymkhanachain.app.ui.mainscreen.fragments.NearGymkFragment;
import com.gymkhanachain.app.ui.userprofile.activity.UserProfileActivity;
import com.gymkhanachain.app.ui.commons.fragments.LoginFragment;
import com.gymkhanachain.app.ui.creategymkana.activity.CreateGymkActivity;
import com.gymkhanachain.app.ui.mainscreen.fragments.GymkInfoFragment;
import com.gymkhanachain.app.ui.mainscreen.fragments.ListGymkFragment;
import com.gymkhanachain.app.ui.userprofile.activity.UserProfileActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        NearGymkFragment.OnNearGymkFragmentInteractionListener,
        MapFragment.OnMapFragmentInteractionListener,
        ListGymkFragment.OnListGymkFragmentInteractionListener,
        GymkInfoFragment.OnGymkInfoFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener, LoginFragment.OnLoginFragmentInteractionListener {

    private static final GymkhanaCache gymkhanas = GymkhanaCache.getInstance();

    private static final String TAG = "MainActivity";
    // Tags para identificar los distintos fragmentos de la Actividad
    private static final String MAP_FRAGMENT_TAG = "MapFragment";
    private static final String LOGIN_FRAGMENT_TAG = "LoginFragment";
    private static final String NEAR_GYMK_FRAGMENT_TAG = "NearGymkFragment";
    private static final String LIST_GYMK_FRAGMENT_TAG = "ListGymkFragment";
    private static final String INFO_GYMK_FRAGMENT_TAG = "GymkInfoFragment";


    // Tag para identificar los permisos
    public static final int REQUEST_MY_LOCATION = 0x01;

    private GoogleSignInClient mGoogleSignInClient;

    // Elementos del NavigationDrawer
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    // Lista de los fragmentos creados que gestiona esta actividad
    List<Fragment> fragments;
    // La gestionamos con este FragmentManager, mediante transacciones
    FragmentManager fragmentManager;

    private List<Integer> gymkhanasId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            // Ya estamos logeados, por lo que se puede continuar el flujo de la app
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_DENIED) {
                DialogFragment dialog = new LocationDialog();
                dialog.show(fragmentManager, "requestLocation");
            } else {
                setContent();
            }
        } else {
            // Se lanza el fragmento de login para solicitar inicio de sesión
            DialogFragment dialogLog = new LoginFragment();
            dialogLog.showNow(fragmentManager,LOGIN_FRAGMENT_TAG);
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

        View headerView = navigationView.getHeaderView(0);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();

            TextView tvUserName = headerView.findViewById(R.id.tv_drawer_username);
            if (tvUserName != null)
                tvUserName.setText(personName);

            TextView tvMail = headerView.findViewById(R.id.tv_drawer_mail);
            if (tvMail != null)
                tvMail.setText(personEmail);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        fragments = new ArrayList<>();

        // Al iniciar la Aplicación, cargamos el primer fragmento visible: NearGymkFragment
        loadDummyGymkhanas();
        NearGymkFragment nearGymkFragment = NearGymkFragment.newInstance(gymkhanasId);
        fragments.add(nearGymkFragment);

        fragmentManager.beginTransaction()
                .add(R.id.placeholder_main, nearGymkFragment, NEAR_GYMK_FRAGMENT_TAG)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();


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

    private void loadDummyGymkhanas() {
        gymkhanasId = new ArrayList<>();

        Bitmap beach = BitmapFactory.decodeResource(getResources(), R.drawable.beach);
        // Torre de hércules
        GymkhanaBean torre = new GymkhanaBean(0, "Torre de Hércules", "El faro romano más antiguo", beach, new LatLng(43.3821723,-8.4061368));
        gymkhanas.setGymkhana(torre);
        gymkhanasId.add(0);
        // A Coruña Oculta
        GymkhanaBean oculta = new GymkhanaBean(1, "A Coruña Oculta", "Descobre sitios que non están ao alcance de todo o mundo", beach, new LatLng(43.3613122,-8.4117282));
        gymkhanas.setGymkhana(oculta);
        gymkhanasId.add(1);
        // Orzán
        GymkhanaBean orzan = new GymkhanaBean(2, "Orzán y su bahía", "Las costas del océano atlantico bañan esta localidad", beach, new LatLng(43.3735763,-8.4029852));
        gymkhanas.setGymkhana(orzan);
        gymkhanasId.add(2);
        // Coruña turismo
        GymkhanaBean turismo = new GymkhanaBean(3, "A Coruña-Turismo", "GAL: GymkhanaBean promocionada pola Oficina de Turismo da Coruña. ESP: GymkhanaBean promocionada por la Oficina de Turismo de A Coruña. ENG: GymkhanaBean provide by the A Coruña Official Tourism", beach, new LatLng(43.370967,-8.3959424));
        gymkhanas.setGymkhana(turismo);
        gymkhanasId.add(3);
    }

    // Implementación de las interfaces de los fragmentos
    // TODO Hay que definir las interfaces de cada fragmento, en función de las interacciones con MainActivity
    @Override
    public void onNearGymkFragmentInteraction() {
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
        switch (menuItem.getItemId()) {
            case R.id.nav_start: // NearGymkFragment
                fragment = fragmentManager.findFragmentByTag(NEAR_GYMK_FRAGMENT_TAG);

                if (fragment != null) {
                    if (fragment.getTag().equals(NEAR_GYMK_FRAGMENT_TAG))
                        break;
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, fragment)
                            .commit();
                } else
                    fragmentManager.beginTransaction()
                            .replace(R.id.placeholder_main, NearGymkFragment.newInstance(gymkhanasId), NEAR_GYMK_FRAGMENT_TAG)
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
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setContent();
                    mDrawerToggle.syncState();
                } else {
                    Toast.makeText(this, getString(R.string.toast_no_location), Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }
    }

    @Override
    public void onMapFragmentInteraction() {
        Toast.makeText(this, "Ejemplo", Toast.LENGTH_SHORT);
    }

    @Override
    public void refusedToLogin() {
        Toast.makeText(getApplicationContext(), getString(R.string.toast_no_login), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Log.d(TAG, "handleSignInResult");

        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
}


