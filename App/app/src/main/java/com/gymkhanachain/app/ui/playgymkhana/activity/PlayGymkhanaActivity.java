package com.gymkhanachain.app.ui.playgymkhana.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.gymkhanachain.app.R;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapFragment;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapFragmentParams;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapMode;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapPoint;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.PointOrder;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.PointType;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class PlayGymkhanaActivity extends AppCompatActivity
        implements MapFragment.OnMapFragmentInteractionListener {

    private static String TAG = "PlayGymkhanaActivity";

    private static int RADIUS_POINT = 10;

    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_gymkhana);
        ButterKnife.bind(this);

        // Se crea el mapa con gymhanas
        final MapFragmentParams params = new MapFragmentParams(PointType.GIS_POINTS,
                PointOrder.NONE_ORDER, MapMode.PLAY_MODE);
        mapFragment = MapFragment.newInstance("Ejemplo", params, getGisPoints());
        getSupportFragmentManager().beginTransaction().add(R.id.map_content, mapFragment).commit();

        // Para activar el botón de volver atrás
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private List<MapPoint> getGisPoints() {
        List<MapPoint> points = new ArrayList<>();

        MapPoint pabellonDeDeportes = new MapPoint(0, new LatLng(43.335080, -8.414539),
                "Pabellon de deportes");
        points.add(pabellonDeDeportes);
        MapPoint xoanaCapdevielle = new MapPoint(1, new LatLng(43.334206, -8.405787),
                "Xoana Capdevielle");
        points.add(xoanaCapdevielle);
        MapPoint facultadeDeEconomicas = new MapPoint(2, new LatLng(43.331124,
                -8.413017), "Facultade de económicas");
        points.add(facultadeDeEconomicas);
        MapPoint arquitectura = new MapPoint(3, new LatLng(43.328036, -8.408088),
                "Arquitectura");
        points.add(arquitectura);

        return points;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onMapSearchButtonClickListener() {
        Log.i(TAG, "Búsqueda");
        Toast toast = Toast.makeText(this, "Búsqueda", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onMapAccesibilityFilterClickListener() {
        Log.i(TAG, "Accesibilidad");
        Toast toast = Toast.makeText(this, "Accesibilidad", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onMapLongClickListener(LatLng point) {
        Log.i(TAG, "Pulsación larga en: " + point);
    }

    @Override
    public void onMapChangeListener(CameraPosition position) {
        Log.i(TAG, "La cámara ha cambiado");
    }

    @Override
    public void onMapPointClickListener(MapPoint point) {
        Log.i(TAG, "Marcador pulsado: " + point.getName());
    }

    @Override
    public void onMapPointMoveListener(MapPoint point) {
        Log.i(TAG, "Marcador movido: " + point.getName());
    }

    @Override
    public void onMapPointsNearLocationListener(List<MapPoint> points) {
        for (MapPoint point : points) {
            Log.i(TAG, "Has pasado cerca de " + point.getName());
            mapFragment.removePoint(point);
        }
    }
}
