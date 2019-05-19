package com.gymkhanachain.app.ui.playgymkhana.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.gymkhanachain.app.R;
import com.gymkhanachain.app.commons.GymkConstants;
import com.gymkhanachain.app.model.beans.GymkhanaBean;
import com.gymkhanachain.app.model.beans.GymkhanaType;
import com.gymkhanachain.app.model.commons.GymkhanaCache;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapFragment;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapFragmentParams;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapMode;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapPoint;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.PointOrder;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.PointType;
import com.gymkhanachain.app.ui.gymkpoint.activities.PointActivity;
import com.gymkhanachain.app.ui.playgymkhana.states.PlayGymkhanaState;
import com.gymkhanachain.app.ui.playgymkhana.states.StartPlayGymkhanaState;

import java.util.ArrayList;
import java.util.List;

public class PlayGymkhanaActivity extends AppCompatActivity
        implements MapFragment.OnMapFragmentInteractionListener {

    private static final GymkhanaCache gymkCache = GymkhanaCache.getInstance();

    private static String TAG = "PlayGymkhanaActivity";

    private MapFragment mapFragment;
    private Integer gymkhanaId;
    private PlayGymkhanaState state;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean useDarkTheme = preferences.getBoolean("activate_dark_theme", false);

        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_gymkhana);

        // Obtenemos la gymkhana
        Intent intent = getIntent();
        gymkhanaId = intent.getIntExtra(GymkConstants.TAG_GYMKHANA_ID, -1);

        // Comprobamos que el bean exista
        if (gymkhanaId > -1) {
            // Se obtiene el bean
            GymkhanaBean bean = gymkCache.getGymkhana(gymkhanaId);

            if (bean != null) {
                List<MapPoint> points = new ArrayList<>();

                // Se crea el mapa con gymkhanas
                PointOrder order = PointOrder.NONE_ORDER;
                if (bean.getType() == GymkhanaType.ordenada) {
                    order = PointOrder.ROUTE_ORDER;
                }

                // Creamos los parámetros de la gymkhana
                MapFragmentParams params = new MapFragmentParams(PointType.GIS_POINTS, order, MapMode.PLAY_MODE);
                params.setShowInfoWindow(true);
                // Creamos el fragmento de mapa
                mapFragment = MapFragment.newInstance(bean.getName(), params, points);
                // Lo añadimos a la interfaz
                getSupportFragmentManager().beginTransaction().replace(R.id.map_content, mapFragment).commit();

                // Obtenemos la posición actual
                getCurrentPosition();
            }
        } else {
            Log.w(TAG, "La gymkhana no existe");
        }

        // Para activar el botón de volver atrás
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void getCurrentPosition() {
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Creamos el estado inicial
                state = new StartPlayGymkhanaState(PlayGymkhanaActivity.this, location);
                state = state.onCreateState(gymkCache.getGymkhana(gymkhanaId));
            }
        });
    }

    public void addPoint(MapPoint point) {
        if (mapFragment != null) {
            Log.i(TAG, "Añadir el punto " + point.toString());
            mapFragment.addPoint(point);
        }
    }

    public void removePoint(MapPoint point) {
        if (mapFragment != null) {
            Log.i(TAG, "Eliminar el punto " + point.toString());
            mapFragment.removePoint(point);
        }
    }

    public void startPointActivity(Integer pointId) {
        Intent intent = new Intent(this, PointActivity.class);
        intent.putExtra(GymkConstants.TAG_POINT_ID, pointId);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onMapSearchButtonClick() {
        return;
    }

    @Override
    public void onMapAccesibilityFilterClick() {
        return;
    }

    @Override
    public void onMapLongClick(LatLng point) {
        return;
    }

    @Override
    public void onMapChangePosition(LatLngBounds bounds, Location position) {
        return;
    }

    @Override
    public void onMapChangeCamera(LatLngBounds bounds, CameraPosition position) {
        return;
    }

    @Override
    public void onMapPointClick(MapPoint point) {
        Log.i(TAG, "Marcador pulsado: " + point.getName());
    }

    @Override
    public void onMapPointMove(MapPoint point) {
        return;
    }

    @Override
    public void onMapPointsNearLocation(List<MapPoint> points) {
        if (state != null) {
            state = state.onMapPointsNearLocation(points);
        }

        if (state == null) {
            onBackPressed();
        }
    }
}
