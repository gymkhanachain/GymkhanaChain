package com.gymkhanachain.app.ui.mainscreen.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.Task;
import com.gymkhanachain.app.R;
import com.gymkhanachain.app.SettingsActivity;
import com.gymkhanachain.app.commons.ProxyBitmap;
import com.gymkhanachain.app.commons.asynctasks.DownloadImageToImageViewAsyncTask;
import com.gymkhanachain.app.commons.asynctasks.DownloadImageAsyncTask;
import com.gymkhanachain.app.model.beans.GymkhanaBean;
import com.gymkhanachain.app.model.beans.GymkhanaType;
import com.gymkhanachain.app.model.beans.PointBean;
import com.gymkhanachain.app.model.beans.TextPointBean;
import com.gymkhanachain.app.model.commons.GymkhanaCache;
import com.gymkhanachain.app.ui.commons.dialogs.LocationDialog;
import com.gymkhanachain.app.ui.commons.fragments.LoginFragment;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapFragment;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapPoint;
import com.gymkhanachain.app.ui.creategymkana.activities.CreateGymkActivity;
import com.gymkhanachain.app.ui.mainscreen.fragments.GymkInfoFragment;
import com.gymkhanachain.app.ui.mainscreen.fragments.ListGymkFragment;
import com.gymkhanachain.app.ui.mainscreen.fragments.NearGymkFragment;
import com.gymkhanachain.app.ui.playgymkhana.activities.PlayGymkhanaActivity;
import com.gymkhanachain.app.ui.userprofile.activities.UserProfileActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        NearGymkFragment.OnNearGymkFragmentInteractionListener,
        MapFragment.OnMapFragmentInteractionListener,
        ListGymkFragment.OnListGymkFragmentInteractionListener,
        GymkInfoFragment.OnGymkInfoFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        LoginFragment.OnLoginFragmentInteractionListener {

    private static final GymkhanaCache gymkCache = GymkhanaCache.getInstance();

    private static final String TAG = "MainActivity";
    // Tags para identificar los distintos fragmentos de la Actividad
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

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

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

            if (preferences.getBoolean("profilepic_pref_switch", false)) {
                ImageView imageView = headerView.findViewById(R.id.iv_drawer_picture);
                DownloadImageToImageViewAsyncTask asyncTask = new DownloadImageToImageViewAsyncTask(getApplicationContext(), imageView);
                asyncTask.execute(acct.getPhotoUrl());
            }
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

    // TODO Borrar
    private PointBean loadPoint(int id, String name, String description, String url, LatLng position, String longText) {
        Uri uri = Uri.parse(url);
        ProxyBitmap wrapper = new ProxyBitmap();
        DownloadImageAsyncTask asyncTask = new DownloadImageAsyncTask(wrapper);
        asyncTask.execute(uri);
        return new TextPointBean(id, name, description, wrapper, position, longText);
    }

    // TODO Borrar
    private GymkhanaBean loadGymkhana(int id, String name, String description, GymkhanaType type, LatLng position, String url, List<PointBean> points) {
        Uri uri = Uri.parse(url);
        ProxyBitmap wrapper = new ProxyBitmap();
        DownloadImageAsyncTask asyncTask = new DownloadImageAsyncTask(wrapper);
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 8);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.setTimeInMillis(today.getTimeInMillis());
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);
        asyncTask.execute(uri);
        return new GymkhanaBean(id, name, description, type, false, false, "", "GymkhanaChain", today.getTime(), today.getTime(), tomorrow.getTime(), wrapper, position, points);
    }

    // TODO Borrar
    private void loadDummyGymkhanas() {
        gymkhanasId = new ArrayList<>();
        int pointId = 0;
        int gymkId = 0;

        // PUNTOS DE GYMKHANA
        List<PointBean> pointsGymk = new ArrayList<>();
        pointsGymk.add(loadPoint(pointId++, "Area Científica", "-",
                "https://i.avoz.es/default/2014/12/07/00121417989423745121334/Foto/H05M2043.jpg",
                new LatLng(43.333035, -8.409033), "En este edificio se hacen cosas ewe"));
        pointsGymk.add(loadPoint(pointId++, "Facultade de Informática", "-",
                "https://www.fic.udc.es/sites/default/files/styles/banner_inicio/public/banner/facultad-informatica-coruna.jpg?itok=1TIC5sZI",
                new LatLng(43.332685, -8.410568), "En esta facultad sufrimos mucho"));
        pointsGymk.add(loadPoint(pointId++, "Plaza del campus", "-",
                "https://fotos02.laopinioncoruna.es/2016/01/23/318x200/universidade-abre.jpg",
                new LatLng(43.332606, -8.412421), "Por fin algo libre"));

        GymkhanaBean bean = loadGymkhana(gymkId++, "GymkhanaChain Pruebas", "Gymkhana de la UDC", GymkhanaType.desordenada,
                new LatLng(43.333516, -8.410707),"http://consellosocial.udc.es/uploadedFiles/CSUDC.b7psr/fileManager/universidad_300112_68_LQ.jpg",
                pointsGymk);

        // GYMKHANA DE PRUEBA
        gymkCache.setGymkhana(bean);
        gymkhanasId.add(bean.getId());

        // PUNTOS DE GYMKHANA
        pointsGymk = new ArrayList<>();
        pointsGymk.add(loadPoint(pointId++, "Plaza del humor", "-",
                "http://www.turismocoruna.com/web/galeria/Paseo_Maritimo_(Stephane_Lutier_2011)2.jpg",
                new LatLng(43.371417, -8.397840), "Plaza del Humor"));
        pointsGymk.add(loadPoint(pointId++, "Plaza de María Píta", "-",
                "http://www.turismocoruna.com/web/galeria/Paseo_Maritimo_(Stephane_Lutier_2011)2.jpg",
                new LatLng(43.370897, -8.395806), "Plaza de Maria Pita"));
        pointsGymk.add(loadPoint(pointId++, "Playas de Coruña", "-",
                "http://www.turismocoruna.com/web/galeria/Paseo_Maritimo_(Stephane_Lutier_2011)2.jpg",
                new LatLng(43.369305, -8.407861), "Playas de Coruña"));
        pointsGymk.add(loadPoint(pointId++, "Jardines de Mendez Núñez", "-",
                "http://www.turismocoruna.com/web/galeria/Paseo_Maritimo_(Stephane_Lutier_2011)2.jpg",
                new LatLng(43.367022, -8.403461), "Jardines de Mendez Núñez"));
        pointsGymk.add(loadPoint(pointId++, "Plaza de España", "-",
                "http://www.turismocoruna.com/web/galeria/Paseo_Maritimo_(Stephane_Lutier_2011)2.jpg",
                new LatLng(43.368082, -8.407074), "Plaza de España"));
        pointsGymk.add(loadPoint(pointId++, "Plaza Pontevedra", "-",
                "http://www.turismocoruna.com/web/galeria/Paseo_Maritimo_(Stephane_Lutier_2011)2.jpg",
                new LatLng(43.373076, -8.396615), "Plaza Pontevedra"));

        bean = loadGymkhana(gymkId++, "Conociendo la Coruña", "-", GymkhanaType.libre,
                new LatLng(43.368673, -8.402353),"http://www.turismocoruna.com/web/galeria/Paseo_Maritimo_(Stephane_Lutier_2011)2.jpg",
                pointsGymk);

        // GYMKHANA DE PRUEBA
        gymkCache.setGymkhana(bean);
        gymkhanasId.add(bean.getId());
    }

    // Implementación de las interfaces de los fragmentos
    // TODO Hay que definir las interfaces de cada fragmento, en función de las interacciones con MainActivity
    @Override
    public void onNearGymkhanaClick(Integer gymkhanaId) {
        Fragment fragment = fragmentManager.findFragmentByTag(INFO_GYMK_FRAGMENT_TAG);

        if (fragment != null && fragment.getTag().equals(INFO_GYMK_FRAGMENT_TAG)) {
            fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.placeholder_main, GymkInfoFragment.newInstance(gymkhanaId), INFO_GYMK_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void onCreateGymkInteraction() {
        Intent intent = new Intent(this, CreateGymkActivity.class);
        startActivity(intent);
    }


    @Override
    public void onGymkhanaActivate(Integer gymkhanaId) {
        Intent intent = new Intent(this, PlayGymkhanaActivity.class);
        startActivity(intent);
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
            case R.id.nav_advanced_search: // Cambiar a busqueda avanzada
                // TODO: cambiar a AdvancedSearchActivity (todavia no se ha creado)
                Toast.makeText(this, "Búsqueda avanzada", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_saved_gymk: // Listar gymkhanas guardadas
                // TODO: mostrar fragment LIST_GYMK_FRAGMENT_TAG con lsa gymkhanas que correspondan
                Toast.makeText(this, "Mostrar gymkhanas guardadas", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_done_gymk: // Listar gymkhanas completadas
                // TODO: mostrar fragment LIST_GYMK_FRAGMENT_TAG con lsa gymkhanas que correspondan
                Toast.makeText(this, "Mostrar gymkhanas completadas", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_my_gymk: // Listar gymkhanas creadas por el usuario
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
            case R.id.nav_create_gymk: // Listar gymkhanas creadas por el usuario
                startActivity(new Intent(this, CreateGymkActivity.class));
                break;
            case R.id.nav_profile:
                startActivity(new Intent(this, UserProfileActivity.class));
                break;
            case R.id.nav_settings:
                Toast.makeText(getApplicationContext(), "Lanzar ajustes", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
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

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.double_tap_exit_app), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
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
        fragmentManager.beginTransaction()
                .replace(R.id.placeholder_main, GymkInfoFragment.newInstance(point.getId()), INFO_GYMK_FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void onMapPointMove(MapPoint point) {

    }

    @Override
    public void onMapPointsNearLocation(List<MapPoint> points) {

    }
}


