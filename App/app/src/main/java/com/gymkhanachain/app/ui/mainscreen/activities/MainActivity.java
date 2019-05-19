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
import com.gymkhanachain.app.ui.settings.SettingsActivity;
import com.gymkhanachain.app.commons.GymkConstants;
import com.gymkhanachain.app.commons.ProxyBitmap;
import com.gymkhanachain.app.commons.asynctasks.DownloadImageAsyncTask;
import com.gymkhanachain.app.commons.asynctasks.DownloadImageToImageViewAsyncTask;
import com.gymkhanachain.app.model.beans.GymkhanaBean;
import com.gymkhanachain.app.model.beans.GymkhanaType;
import com.gymkhanachain.app.model.beans.PointBean;
import com.gymkhanachain.app.model.beans.QuizzPointBean;
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
import java.util.Arrays;
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

    // La gestionamos con este FragmentManager, mediante transacciones
    FragmentManager fragmentManager;
    String activeFragment;
    Fragment.SavedState nearGymkFragmentSavedState;
    Fragment.SavedState listGymkFragmentSavedState;
    Fragment.SavedState infoGymkFragmentSavedState;
    Integer currentGymkId = null;

    private List<Integer> gymkhanasId;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean useDarkTheme = preferences.getBoolean("activate_dark_theme", false);

        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark);
        }

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

        // Al iniciar la Aplicación, cargamos el primer fragmento visible: NearGymkFragment
        loadDummyGymkhanas();

        //Fragment fragment = fragmentManager.findFragmentByTag(NEAR_GYMK_FRAGMENT_TAG);
/*        Fragment fragment = fragmentManager.findFragmentByTag(MAP_FRAGMENT_TAG);

        if (fragment != null){
            Log.d(TAG, "Muestro MapFragment ya existente");
            fragmentManager.beginTransaction()
                    .replace(R.id.placeholder_main, fragment, MAP_FRAGMENT_TAG)
                    //.addToBackStack(MAP_FRAGMENT_TAG)
                    .commit();
        } else {
            Log.d(TAG, "Nueva instancia de MapFragment");
            //Fragment nearGymkFragment = NearGymkFragment.newInstance(gymkhanasId);
            Fragment mapFragment = MapFragment.newInstance("", new MapFragmentParams(), new ArrayList<MapPoint>());
            fragmentManager.beginTransaction()
                    .replace(R.id.placeholder_main, mapFragment, MAP_FRAGMENT_TAG)
                    //.addToBackStack(MAP_FRAGMENT_TAG)
                    .commit();
        }*/
        Fragment fragment = fragmentManager.findFragmentByTag(NEAR_GYMK_FRAGMENT_TAG);

        if (activeFragment == null) {
            if (fragment == null) { // no existe, lo instancio
                Log.d(TAG, "-SetContent- Nueva instancia de NearGymkFragment");
                fragmentManager.beginTransaction()
                        .replace(R.id.placeholder_main, NearGymkFragment.newInstance(gymkhanasId), NEAR_GYMK_FRAGMENT_TAG)
                        .addToBackStack(NEAR_GYMK_FRAGMENT_TAG)
                        .commit();
            } else if (fragment != null && !fragment.isVisible()) { // existe, pero no está visible. lo muestro
                Log.d(TAG, "-SetContent- Muestro NearGymkFragment ya existente");
                fragmentManager.beginTransaction()
                        .replace(R.id.placeholder_main, fragment, NEAR_GYMK_FRAGMENT_TAG)
                        .show(fragment)
                        .commit();
            } else { // No hacemos nada porque ya es el que se está viendo
                Toast.makeText(getApplicationContext(), "No hacemos nada", Toast.LENGTH_SHORT).show();
            }
           // activeFragment = NearGymkFragment.class.getName();
            Log.d(TAG, "Active fragment: " + activeFragment);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, fragmentManager.getFragments().toString());
        Log.d(TAG, "onResume active fragment: " + activeFragment);

        if (activeFragment == ListGymkFragment.class.getName() && listGymkFragmentSavedState != null) {
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(LIST_GYMK_FRAGMENT_TAG)).commit();
            Fragment listGymkFragment = ListGymkFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.placeholder_main, listGymkFragment, LIST_GYMK_FRAGMENT_TAG).commit();
            return;
        }
        if (activeFragment == NearGymkFragment.class.getName() && nearGymkFragmentSavedState != null) {
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(NEAR_GYMK_FRAGMENT_TAG)).commit();
            Fragment nearGymkFragment = NearGymkFragment.newInstance(gymkhanasId);
            nearGymkFragment.setInitialSavedState(nearGymkFragmentSavedState);
            fragmentManager.beginTransaction().replace(R.id.placeholder_main, nearGymkFragment, NEAR_GYMK_FRAGMENT_TAG).commit();
            return;
        }
        if (activeFragment == GymkInfoFragment.class.getName() && infoGymkFragmentSavedState != null) {
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(INFO_GYMK_FRAGMENT_TAG)).commit();
            Fragment infoGymkFragment = GymkInfoFragment.newInstance(currentGymkId);
            infoGymkFragment.setInitialSavedState(infoGymkFragmentSavedState);
            fragmentManager.beginTransaction().replace(R.id.placeholder_main, infoGymkFragment, INFO_GYMK_FRAGMENT_TAG).commit();
            return;
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        Log.d(TAG, " onAttachFragment " + fragment.getClass().getName());
        if (fragment.getClass().getName().equals(NearGymkFragment.class.getName()))
            activeFragment = NearGymkFragment.class.getName();
        if (fragment.getClass().getName().equals(ListGymkFragment.class.getName()))
            activeFragment = ListGymkFragment.class.getName();
        if (fragment.getClass().getName().equals(GymkInfoFragment.class.getName()))
            activeFragment = GymkInfoFragment.class.getName();

        super.onAttachFragment(fragment);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, " onStop");
        // Necesito guardar el estado actual de todos los fragmentos existentes, para recuperarlos en onResume
        try {
            nearGymkFragmentSavedState = fragmentManager.saveFragmentInstanceState(fragmentManager.findFragmentByTag(NEAR_GYMK_FRAGMENT_TAG));
            infoGymkFragmentSavedState = fragmentManager.saveFragmentInstanceState(fragmentManager.findFragmentByTag(INFO_GYMK_FRAGMENT_TAG));

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            listGymkFragmentSavedState = fragmentManager.saveFragmentInstanceState(fragmentManager.findFragmentByTag(LIST_GYMK_FRAGMENT_TAG));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        Log.d(TAG, " StateSaved: " + fragmentManager.isStateSaved());
        Log.d(TAG, "Active fragment: " + activeFragment);
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, " onPause");
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

    private PointBean loadPoint(int id, String name, String description, String url, LatLng position, String question, List<String> solutions, int solution) {
        Uri uri = Uri.parse(url);
        ProxyBitmap wrapper = new ProxyBitmap();
        DownloadImageAsyncTask asyncTask = new DownloadImageAsyncTask(wrapper);
        asyncTask.execute(uri);
        return new QuizzPointBean(id, name, description, wrapper, position, question, solutions, solution);
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
        pointsGymk.add(loadPoint(pointId++, "Area Científica", "Area Científica",
                "https://i.avoz.es/default/2014/12/07/00121417989423745121334/Foto/H05M2043.jpg",
                new LatLng(43.333035, -8.409033), "Edificio donde se desarrolló GymkhanaChain"));
        pointsGymk.add(loadPoint(pointId++, "Facultade de Informática", "Facultade de Informática",
                "https://www.fic.udc.es/sites/default/files/styles/banner_inicio/public/banner/facultad-informatica-coruna.jpg",
                new LatLng(43.332685, -8.410568), "Facultade de Informática', 'En esta facultad estudiarion el grupo de GymkhanaChain"));
        pointsGymk.add(loadPoint(pointId++, "Plaza del campus", "Plaza del campus",
                "https://fotos02.laopinioncoruna.es/2016/01/23/318x200/universidade-abre.jpg",
                new LatLng(43.332606, -8.412421), "Disfruta del aire libre"));

        GymkhanaBean bean = loadGymkhana(gymkId++, "Gymkhana de Pruebas", "Gymkhana de la UDC", GymkhanaType.desordenada,
                new LatLng(43.333516, -8.410707),"http://consellosocial.udc.es/uploadedFiles/CSUDC.b7psr/fileManager/universidad_300112_68_LQ.jpg",
                pointsGymk);

        // GYMKHANA DE PRUEBA
        gymkCache.setGymkhana(bean);
        gymkhanasId.add(bean.getId());

        // PUNTOS DE GYMKHANA
        pointsGymk = new ArrayList<>();
        pointsGymk.add(loadPoint(pointId++, "Plaza del Humor", "Plaza del Humor",
                "http://3.bp.blogspot.com/-OkWv5N7q7SI/VK7UwysnHbI/AAAAAAAABr0/4NC15Jubw8U/s1600/PlazaDelHumor.jpg",
                new LatLng(43.371417, -8.397840),
                "Esta es una plaza dedicada a las figuras del humor. Allí el visitante se encontrará con personajes inmortales del género, tanto creadores como figuras de series de cómics, libros y televisión."));
        pointsGymk.add(loadPoint(pointId++, "Plaza de María Píta", "Plaza de María Píta",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d2/Plaza_A_Coru%C3%B1a.JPG/1200px-Plaza_A_Coru%C3%B1a.JPG",
                new LatLng(43.370897, -8.395806), "Por esta plaza céntrica, dedicada a la heroína María Pita, pasa un meridiano. ¿Cúal es?",
                Arrays.asList("8º24'56\"", "9º23'39\"", "8º24'38\"", "8º23'39\""), 3));
        pointsGymk.add(loadPoint(pointId++, "Playas de Coruña", "Playas de Coruña",
                "http://www.turismo.gal/imaxes/mdaw/mduw/~edisp/~extract/TURGA050853~1~staticrendition/tg_carrusel_cabecera_grande.jpg",
                new LatLng(43.369305, -8.407861), "Coruña tiene dos quilómetros de playas urbanas repartidas en 6 localizaciones. Todas tiene bandera azul."));
        pointsGymk.add(loadPoint(pointId++, "Jardines de Mendez Núñez", "-",
                "https://saposyprincesas.elmundo.es/wp-content/uploads/2016/03/mendez_3.jpg",
                new LatLng(43.367022, -8.403461),
                "Los Jardines de Méndez Núñez son unos jardines de la ciudad de la Coruña situados entre Los Cantones y el puerto. Están dedicados al marino Casto Méndez Núñez, héroe de la Primera Guerra del Pacífico."));

        bean = loadGymkhana(gymkId++, "Conociendo la Coruña", "Conoce las zonas emblemáticas de la coruña", GymkhanaType.libre,
                new LatLng(43.368673, -8.402353),"http://www.turismocoruna.com/web/galeria/Paseo_Maritimo_(Stephane_Lutier_2011)2.jpg",
                pointsGymk);

        // GYMKHANA DE PRUEBA
        gymkCache.setGymkhana(bean);
        gymkhanasId.add(bean.getId());

        // PUNTOS DE GYMKHANA
        pointsGymk = new ArrayList<>();
        pointsGymk.add(loadPoint(pointId++, "Praza de Azcárraga", "Praza de Azcárraga",
                "https://grupocoruna.es/sites/default/files/styles/flexslider_full/public/photos/plaza-de-azcarraga.jpg?itok=WmAtnehf",
                new LatLng(43.370289, -8.393641),
                ""));
        pointsGymk.add(loadPoint(pointId++, "Igrexa de Santiago", "Igrexa de Santiago",
                "https://upload.wikimedia.org/wikipedia/commons/e/e5/2165-Igrexa_de_Santiago_na_Cidade_Vella_da_Coru%C3%B1a.jpg",
                new LatLng(43.369808, -8.394701),
                ""));
        pointsGymk.add(loadPoint(pointId++, "Ruteiros da Cidade Vella", "Ruteiros da Cidade Vella",
                "https://www.paxinasgalegas.es/fiestas/imagenes/ruta-por-la-ciudad-vieja-de-a-coru%C3%B1a-a-coru%C3%B1a_img5454n6t0.jpg",
                new LatLng(43.369904, -8.392332),
                ""));
        pointsGymk.add(loadPoint(pointId++, "Arquivo do Reino de Galiza", "Arquivo do Reino de Galiza",
                "https://petrybasket.files.wordpress.com/2017/04/archivos-del-reino-de-galicia.jpg?w=333&h=222",
                new LatLng(43.369296, -8.391211),
                ""));
        pointsGymk.add(loadPoint(pointId++, "Colexio Santo Domingo", "Colexio Santo Domingo",
                "",
                new LatLng(43.370221, -8.391513),
                ""));

        bean = loadGymkhana(gymkId++, "Caminata pola Cidade Vella", "Dende a colegiata de Santa María, imos pasar polos sitios máis recónditos desta fermosa cidade", GymkhanaType.ordenada,
                new LatLng(43.370821, -8.392935),"https://viajology.com/wp-content/uploads/2013/11/Colegiata-de-Santa-Maria-del-Campo.jpg",
                pointsGymk);

        // GYMKHANA DE PRUEBA
        gymkCache.setGymkhana(bean);
        gymkhanasId.add(bean.getId());

        ////// Vioño park
        // PUNTOS DE GYMKHANA
        pointsGymk = new ArrayList<>();
        pointsGymk.add(loadPoint(pointId++, "Fuente central", "Fuente grande del parque del Vioño",
                "https://i.ytimg.com/vi/fX3SUTGYW2k/hqdefault.jpg",
                new LatLng(43.355499, -8.416777),
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum convallis cursus hendrerit. Morbi volutpat ex ac ligula condimentum pulvinar."));
        pointsGymk.add(loadPoint(pointId++, "Hórreo del Vioño", "Hórreo, piorno, cabazo...",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/2/29/H%C3%B3rreo_no_parque_de_Vio%C3%B1o.jpg/1200px-H%C3%B3rreo_no_parque_de_Vio%C3%B1o.jpg",
                new LatLng(43.354553, -8.415481), "Por aquí pasa un meridiano. ¿Cúal es?",
                Arrays.asList("8º24'56\"", "9º23'39\"", "8º24'38\"", "8º23'39\""), 4));
        pointsGymk.add(loadPoint(pointId++, "Parque infantil", "Parque infantil con mazo colores",
                "https://grupocoruna.es/sites/default/files/parque-infantil-de-viono.jpg",
                new LatLng(43.354455, -8.416785), "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum convallis cursus hendrerit. Morbi volutpat ex ac ligula condimentum pulvinar."));

        bean = loadGymkhana(gymkId++, "Vioño Park", "Conoce la zona recreativa más molona de A Coruña", GymkhanaType.libre,
                new LatLng(43.353442, -8.416401),"https://i.pinimg.com/originals/01/9b/90/019b9028e2bbb4c479ffd4cfb41dc987.jpg",
                pointsGymk);

        // GYMKHANA DE PRUEBA
        gymkCache.setGymkhana(bean);
        gymkhanasId.add(bean.getId());
    }

    // Implementación de las interfaces de los fragmentos
    // TODO Hay que definir las interfaces de cada fragmento, en función de las interacciones con MainActivity
    @Override
    public void onNearGymkhanaClick(Integer gymkhanaId) {

        currentGymkId = gymkhanaId;

        Fragment fragment = fragmentManager.findFragmentByTag(INFO_GYMK_FRAGMENT_TAG);

        if (fragment == null) {// No existe, instancio uno nuevo
            Log.d(TAG, "Nueva instancia de InfoGymkFragment");
            fragmentManager.beginTransaction()
                    .replace(R.id.placeholder_main, GymkInfoFragment.newInstance(gymkhanaId), INFO_GYMK_FRAGMENT_TAG)
                    .addToBackStack(INFO_GYMK_FRAGMENT_TAG)
                    .commit();
        } else if (fragment != null && !fragment.isVisible()) { // Existe, pero no es visible. Lo muestro
            Log.d(TAG, "Muestro InfoGymkFragment ya existente");
            fragmentManager.beginTransaction()
                    .replace(R.id.placeholder_main, fragment, INFO_GYMK_FRAGMENT_TAG)
                    .show(fragment)
                    .commit();
        } else { // No hacemos nada
            Toast.makeText(getApplicationContext(), "No hacemos nada", Toast.LENGTH_SHORT).show();
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
        intent.putExtra(GymkConstants.TAG_GYMKHANA_ID, gymkhanaId);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;

        // TODO Los elementos del switch deberían estar codificados en el archivo de strings
        switch (menuItem.getItemId()) {
            case R.id.nav_start: // NearGymkFragment
                //activeFragment = NearGymkFragment.class.getName();

                fragment = fragmentManager.findFragmentByTag(NEAR_GYMK_FRAGMENT_TAG);

                if (fragment == null) {// No existe, instancio uno nuevo
                    Log.d(TAG, "Nueva instancia de NearGymkFragment");
                    fragmentManager.beginTransaction()
                            .replace(R.id.placeholder_main, NearGymkFragment.newInstance(gymkhanasId), NEAR_GYMK_FRAGMENT_TAG)
                            //.addToBackStack(NEAR_GYMK_FRAGMENT_TAG)
                            .commit();
                } else if (fragment != null && !fragment.isVisible()) { // Existe, pero no es visible. Lo muestro
                    Log.d(TAG, "Muestro NearGymkFragment ya existente");
                    fragmentManager.beginTransaction()
                            .replace(R.id.placeholder_main, fragment, NEAR_GYMK_FRAGMENT_TAG)
                            .show(fragment)
                            .commit();
                } else { // No hacemos nada
                    Toast.makeText(getApplicationContext(), "No hacemos nada", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_advanced_search: // Cambiar a busqueda avanzada
                // TODO: cambiar a AdvancedSearchActivity (todavia no se ha creado)
                Toast.makeText(this, "Búsqueda avanzada", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_saved_gymk: // Listar gymkhanas guardadas
                // TODO: mostrar fragment LIST_GYMK_FRAGMENT_TAG con las gymkhanas que correspondan
                Toast.makeText(this, "Mostrar gymkhanas guardadas", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_done_gymk: // Listar gymkhanas completadas
                // TODO: mostrar fragment LIST_GYMK_FRAGMENT_TAG con las gymkhanas que correspondan
                Toast.makeText(this, "Mostrar gymkhanas completadas", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_my_gymk: // Listar gymkhanas creadas por el usuario
                //activeFragment = ListGymkFragment.class.getName();

                fragment = fragmentManager.findFragmentByTag(LIST_GYMK_FRAGMENT_TAG);

                if (fragment == null) { // no existe, lo instancio
                    Log.d(TAG, "Nueva instancia de ListGymkFragment");
                    fragmentManager.beginTransaction()
                            .replace(R.id.placeholder_main, ListGymkFragment.newInstance(), LIST_GYMK_FRAGMENT_TAG)
                            .addToBackStack(LIST_GYMK_FRAGMENT_TAG)
                            .commit();
                } else if (fragment != null && !fragment.isVisible()) { // existe, pero no está visible. lo muestro
                    Log.d(TAG, "Muestro ListGymkFragment ya existente");
                    fragmentManager.beginTransaction()
                            .replace(R.id.placeholder_main, fragment, LIST_GYMK_FRAGMENT_TAG)
                            .addToBackStack(LIST_GYMK_FRAGMENT_TAG)
                            //.show(fragment)
                            .commit();
                } else { // No hacemos nada porque ya es el que se está viendo
                    Toast.makeText(getApplicationContext(), "No hacemos nada", Toast.LENGTH_SHORT).show();
                }
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

        Fragment fragment = fragmentManager.findFragmentByTag(NEAR_GYMK_FRAGMENT_TAG);

        if (fragment != null && fragment.isVisible()) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getString(R.string.double_tap_exit_app), Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentById(R.id.placeholder_main)).commit();
            super.onBackPressed();
        }
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

        currentGymkId = point.getId();

        fragmentManager.beginTransaction()
                .replace(R.id.placeholder_main, GymkInfoFragment.newInstance(currentGymkId), INFO_GYMK_FRAGMENT_TAG)
                .addToBackStack(INFO_GYMK_FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void onMapPointMove(MapPoint point) {

    }

    @Override
    public void onMapPointsNearLocation(List<MapPoint> points) {

    }
}


