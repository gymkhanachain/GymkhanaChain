package com.gymkhanachain.app.client;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gymkhanachain.app.model.beans.GymkhanaType;

import java.util.List;

public class Gymkhana {
    @Expose
    @SerializedName("GYMK_ID")
    private Integer gymk_id;
    @Expose
    @SerializedName("IMAGE")
    private String image;
    @Expose
    @SerializedName("NAME")
    private String name;
    @Expose
    @SerializedName("DESCRIPCION")
    private String description;
    @Expose
    @SerializedName("LAT")
    private double latitude;
    @Expose
    @SerializedName("LNG")
    private double longitude;
    @Expose
    @SerializedName("TYPE")
    private GymkhanaType type;
    @Expose
    @SerializedName("A11Y")
    private boolean a11y;
    @Expose
    @SerializedName("PRIV")
    private boolean priv;
    @Expose
    @SerializedName("ACC_COD")
    private String acc_cod;
    @Expose
    @SerializedName("CREATOR")
    private String creator; //Object
    @Expose
    @SerializedName("CREA_DATE")
    private int crea_time; //'crea_time'
    @Expose
    @SerializedName("APER_TIME")
    private int aper_time; //aper_time
    @Expose
    @SerializedName("CLOSE_TIME")
    private int close_time;//close_time
    @Expose
    @SerializedName("POINTS")
    private List<Point> points; //close_time

    public Gymkhana(Integer gymk_id, Bitmap image, String name, String description,
                    LatLng latlng , GymkhanaType type, boolean a11y, boolean priv, String acc_cod, String creator, int crea_time, int aper_time, int close_time, List<Point> points) {
        this.gymk_id = gymk_id;
        this.image = GymkhanaUtils.BitMapToString(image);
        this.name = name;
        this.description = description;
        this.latitude= latlng.latitude;
        this.longitude = latlng.longitude;
        this.type = type;
        this.a11y = a11y;
        this.priv = priv;
        this.acc_cod = acc_cod;
        this.creator = creator;
        this.crea_time = crea_time;
        this.aper_time = aper_time;
        this.close_time = close_time;
        this.points = points;
    }

    public Integer getGymk_id() {
        return gymk_id;
    }

    public void setGymk_id(Integer gymk_id) {
        this.gymk_id = gymk_id;
    }

    public String getImage() {
        return image;
    }

    public Bitmap getBitmapImage(){
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
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setLongitude(double position) {
        this.longitude = position;
    }

    public GymkhanaType getType() {
        return type;
    }

    public void setType(GymkhanaType type) {
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

    public int getCrea_time() {
        return crea_time;
    }

    public void setCrea_time(int crea_time) {
        this.crea_time = crea_time;
    }

    public int getAper_time() {
        return aper_time;
    }

    public void setAper_time(int aper_time) {
        this.aper_time = aper_time;
    }

    public int getClose_time() {
        return close_time;
    }

    public void setClose_time(int close_time) {
        this.close_time = close_time;
    }

    public LatLng getPosition(){
        LatLng latlng = new LatLng(this.latitude, this.longitude);
        return latlng;
    }
    public List<Point> getPuntos() {
        return points;
    }
    public void setPuntos(List<Point> points) {
        this.points = points;
    }
}




