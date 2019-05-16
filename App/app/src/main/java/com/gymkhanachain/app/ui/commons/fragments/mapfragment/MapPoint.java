package com.gymkhanachain.app.ui.commons.fragments.mapfragment;

import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class MapPoint {
    Integer id;
    LatLng position;
    String name;

    @ParcelConstructor
    public MapPoint(Integer id, LatLng position, String name) {
        this.id = id;
        this.position = position;
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
