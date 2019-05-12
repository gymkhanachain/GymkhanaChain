package com.gymkhanachain.app.ui.commons.fragments.mapfragment;

import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.gymkhanachain.app.R;

public class MapFragmentInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private static final String TAG = "MapFragmentInfoWindowAdapter";
    private LayoutInflater inflater;

    public MapFragmentInfoWindowAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        // Se carga el layout personalizado
        View view = inflater.inflate(R.layout.content_marker_windows, null);
        return view;
    }
}
