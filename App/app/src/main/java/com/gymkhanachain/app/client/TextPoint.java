package com.gymkhanachain.app.client;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TextPoint extends Point {
    @Expose
    @SerializedName("LONG_DESC")
    private String long_desc;

    public TextPoint(Integer point_id, String image, String name, String short_desc, LatLng latLng, String long_desc) {
        super(point_id, image, name, short_desc, latLng.latitude, latLng.longitude);
        this.long_desc = long_desc;
    }

    public TextPoint(){
        super();
    }

    public String getLong_desc() {
        return long_desc;
    }

    public void setLong_desc(String long_desc) {
        this.long_desc = long_desc;
    }
}
