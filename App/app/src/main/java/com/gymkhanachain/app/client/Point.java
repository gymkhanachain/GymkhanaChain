package com.gymkhanachain.app.client;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class Point {
    @Expose
    @SerializedName("POINT_ID")
    private Integer point_id;
    @Expose
    @SerializedName("IMAGE")
    private String image;
    @Expose
    @SerializedName("NAME")
    private String name;
    @Expose
    @SerializedName("SHORT_DESC")
    private String short_desc;
    @Expose
    @SerializedName("LAT")
    private double latitude;
    @Expose
    @SerializedName("LNG")
    private double longitude;

    public Point(Integer point_id, Bitmap image, String name, String short_desc, double latitude, double longitude) {
        this.point_id = point_id;
        this.image = GymkhanaUtils.BitMapToString(image);
        this.name = name;
        this.short_desc = short_desc;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Point() {
    }

    public Integer getPoint_id() {
        return point_id;
    }

    public void setPoint_id(Integer point_id) {
        this.point_id = point_id;
    }

    public String getImage() {
        return image;
    }

    public Bitmap getBitmapImage() {
        return GymkhanaUtils.StringToBitMap(image);
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return short_desc;
    }

    public void setDescription(String description) {
        this.short_desc = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LatLng getPosition(){
        LatLng latlng = new LatLng(this.latitude, this.longitude);
        return latlng;
    }
}
