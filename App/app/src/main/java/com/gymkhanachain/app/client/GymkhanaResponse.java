package com.gymkhanachain.app.client;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class GymkhanaResponse {

    @Expose
    @SerializedName("gymk_id")
    private Integer gymk_id;
    @Expose
    @SerializedName("image")
    private Bitmap image;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("description")
    private String description;
    @Expose
    @SerializedName("lat")
    private long latitude;
    @Expose
    @SerializedName("lng")
    private long longitude;

    private LatLng position;
    // 'lng'+ 'lat';
    @Expose
    @SerializedName("type")
    private String type;
    @Expose
    @SerializedName("a11y")
    private boolean a11y;
    @Expose
    @SerializedName("priv")
    private boolean priv;
    @Expose
    @SerializedName("acc_cod")
    private String acc_cod;
    @Expose
    @SerializedName("creator")
    private String creator; //Object
    @Expose
    @SerializedName("crea_time")
    private Date crea_time; //'crea_time'
    @Expose
    @SerializedName("aper_time")
    private Date aper_time; //aper_time
    @Expose
    @SerializedName("close_time")
    private Date close_time;//close_time

    public GymkhanaResponse(Integer gymk_id, Bitmap image, String name, String description,
                            long lat, long lng , String type, boolean a11y, boolean priv, String acc_cod, String creator, Date crea_time, Date aper_time, Date close_time) {
        this.gymk_id = gymk_id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.latitude=lat;
        this.longitude = lng;
        this.position = new LatLng(latitude,longitude);
        this.type = type;
        this.a11y = a11y;
        this.priv = priv;
        this.acc_cod = acc_cod;
        this.creator = creator;
        this.crea_time = crea_time;
        this.aper_time = aper_time;
        this.close_time = close_time;
    }

    public Integer getGymk_id() {
        return gymk_id;
    }

    public void setGymk_id(Integer gymk_id) {
        this.gymk_id = gymk_id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }
    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(long position) {
        this.longitude = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isA11y() {
        return a11y;
    }

    public void setA11y(boolean a11y) {
        this.a11y = a11y;
    }

    public boolean isPriv() {
        return priv;
    }

    public void setPriv(boolean priv) {
        this.priv = priv;
    }

    public String getAcc_cod() {
        return acc_cod;
    }

    public void setAcc_cod(String acc_cod) {
        this.acc_cod = acc_cod;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCrea_time() {
        return crea_time;
    }

    public void setCrea_time(Date crea_time) {
        this.crea_time = crea_time;
    }

    public Date getAper_time() {
        return aper_time;
    }

    public void setAper_time(Date aper_time) {
        this.aper_time = aper_time;
    }

    public Date getClose_time() {
        return close_time;
    }

    public void setClose_time(Date close_time) {
        this.close_time = close_time;
    }

}




