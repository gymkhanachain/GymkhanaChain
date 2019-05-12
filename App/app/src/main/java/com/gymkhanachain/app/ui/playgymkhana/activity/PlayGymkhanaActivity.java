package com.gymkhanachain.app.ui.playgymkhana.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

public class PlayGymkhanaActivity extends AppCompatActivity implements MapFragment.OnMapFragmentInteractionListener {

    private static String TAG = "PlayGymkhanaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_gymkhana);
        ButterKnife.bind(this);

        // Create map fragment
        final MapFragmentParams params = new MapFragmentParams(PointType.GIS_POINTS, PointOrder.NONE_ORDER, MapMode.EDIT_MODE);
        final Fragment map = MapFragment.newInstance(params, getGisPoints());
        getSupportFragmentManager().beginTransaction().add(R.id.map_content, map).commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private List<MapPoint> getGisPoints() {
        List<MapPoint> points = new ArrayList<>();

        MapPoint estadioUdc = new MapPoint(0, new LatLng(43.333259, -8.4126029), "Estadio UDC");
        points.add(estadioUdc);
        MapPoint facultadSociología = new MapPoint(1, new LatLng(43.332556, -8.4139761), "Facultad de Sociología");
        points.add(facultadSociología);
        MapPoint circuito = new MapPoint(2, new LatLng(43.3313183, -8.4116328), "Servicios de Apoyo a la Investigación");
        points.add(circuito);
        MapPoint areaCientifica = new MapPoint(3, new LatLng(43.3300947, -8.4125125), "UDC");
        points.add(areaCientifica);

        return points;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onMapLongClickListener(LatLng point) {
        Log.i(TAG, "Pulsación larga en: " + point);
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
    public void onMapPointEditListener(MapPoint point) {
        Log.i(TAG, "Marcador editado: " + point.getName());
    }

    @Override
    public void onMapPointRemoveListener(MapPoint point) {
        Log.i(TAG, "Marcador eliminado: " + point.getName());
    }
}
