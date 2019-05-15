package com.gymkhanachain.app.ui.commons.fragments.mapfragment;

import android.util.Log;

import com.google.android.gms.maps.model.MapStyleOptions;
import com.gymkhanachain.app.commons.GymkConstants;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Esta clase representa un envoltorio de los parámetros que va a tomar MapFragment para cambiar su
 * comportamiento
 */
@Parcel
public class MapFragmentParams {
    private static String TAG = "MapFragmentParams";

    PointType typePoints;
    PointOrder orderPoints;
    MapMode mapMode;
    boolean showInfoWindow;
    float minimumLocationInterval;
    float maximumLocationInterval;
    int triggeredDistance;
    MapStyleOptions style;
    int mapType;

    /**
     * Crea un nuevo MapFragment con puntos de Gymkhana en modo normal, sin mostrar el pop-up de los
     * marcadores, con un intervalo de 2s-5s de actualización, sin estilo y sin tipo de mapa
     */
    public MapFragmentParams() {
        this(GymkConstants.DEFAULT_POINT_TYPE, GymkConstants.DEFAULT_POINT_ORDER,
                GymkConstants.DEFAULT_MAP_MODE);
    }

    /**
     * Crea un nuevo MapFragment con los valores por defecto y definiendo los más importantes
     * @param typePoints Tipo de puntos (mirar PointType)
     * @param orderPoints Orden los puntos en una Gymkhana (mirar PointOrder)
     * @param mapMode Modo del mapa (mirar MapMode)
     */
    public MapFragmentParams(PointType typePoints, PointOrder orderPoints, MapMode mapMode) {
        this(typePoints, orderPoints, mapMode, false,
                GymkConstants.DEFAULT_MIN_LOCATION_INTERVAL,
                GymkConstants.DEFAULT_MAX_LOCATION_INTERVAL,
                GymkConstants.DEFAULT_TRIGGERED_DISTANCE, null, -1);
    }

    @ParcelConstructor
    public MapFragmentParams(PointType typePoints, PointOrder orderPoints, MapMode mapMode,
                             boolean showInfoWindow, float minimumLocationInterval,
                             float maximumLocationInterval, int triggeredDistance,
                             MapStyleOptions style, int mapType) {
        setTypePoints(typePoints)
                .setOrderPoints(orderPoints)
                .setMapMode(mapMode)
                .setShowInfoWindow(showInfoWindow)
                .setMinimumLocationInterval(minimumLocationInterval)
                .setMinimumLocationInterval(maximumLocationInterval)
                .setTriggeredDistance(triggeredDistance)
                .setStyle(style)
                .setMapType(mapType);
    }

    public PointType getTypePoints() {
        return typePoints;
    }

    public MapFragmentParams setTypePoints(PointType typePoints) {
        this.typePoints = typePoints;
        return this;
    }

    public PointOrder getOrderPoints() {
        return orderPoints;
    }

    public MapFragmentParams setOrderPoints(PointOrder orderPoints) {
        this.orderPoints = orderPoints;
        return this;
    }

    public MapMode getMapMode() {
        return mapMode;
    }

    public MapFragmentParams setMapMode(MapMode mapMode) {
        this.mapMode = mapMode;
        return this;
    }

    public boolean isShowInfoWindow() {
        return showInfoWindow;
    }

    public MapFragmentParams setShowInfoWindow(boolean showInfoWindow) {
        this.showInfoWindow = showInfoWindow;
        return this;
    }

    public float getMinimumLocationInterval() {
        return minimumLocationInterval;
    }

    public MapFragmentParams setMinimumLocationInterval(float minimumLocationInterval) {
        this.minimumLocationInterval = minimumLocationInterval;

        if (this.maximumLocationInterval < this.minimumLocationInterval *2.0) {
            Log.w(TAG, "The minimun value should be less that the half of maximun");
        }

        return this;
    }

    public float getMaximumLocationInterval() {
        return maximumLocationInterval;
    }

    public MapFragmentParams setMaximumLocationInterval(float maximumLocationInterval) {
        this.maximumLocationInterval = maximumLocationInterval;

        if (this.maximumLocationInterval < this.minimumLocationInterval *2.0) {
            Log.w(TAG, "The maximun value should be greater that the double of minimun");
        }

        return this;
    }

    public int getTriggeredDistance() {
        return triggeredDistance;
    }

    public MapFragmentParams setTriggeredDistance(int triggeredDistance) {
        this.triggeredDistance = triggeredDistance;
        return this;
    }

    public MapStyleOptions getStyle() {
        return style;
    }

    public MapFragmentParams setStyle(MapStyleOptions style) {
        this.style = style;
        return this;
    }

    public int getMapType() {
        return mapType;
    }

    public MapFragmentParams setMapType(int mapType) {
        this.mapType = mapType;
        return this;
    }
}
