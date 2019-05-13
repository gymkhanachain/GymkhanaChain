package com.gymkhanachain.app.ui.commons.fragments.mapfragment;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.MapStyleOptions;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Esta clase representa un envoltorio de los parámetros que va a tomar MapFragment para cambiar su
 * comportamiento
 */
@Parcel
public class MapFragmentParams {
    PointType typePoints;
    PointOrder orderPoints;
    MapMode mapMode;
    boolean showInfoWindow;
    int locationPriority;
    MapStyleOptions style;
    int mapType;

    /**
     * Crea un nuevo MapFragment con puntos de Gymkhana en modo normal, sin mostrar el pop-up de los
     * marcadores, con un intervalo de 25s de actualización, sin estilo y sin tipo de mapa
     */
    public MapFragmentParams() {
        this(PointType.GYMKHANA_POINTS, PointOrder.NONE_ORDER, MapMode.NORMAL_MODE);
    }

    /**
     * Crea un nuevo MapFragment con los valores por defecto y definiendo los más importantes
     * @param typePoints Tipo de puntos (mirar PointType)
     * @param orderPoints Orden los puntos en una Gymkhana (mirar PointOrder)
     * @param mapMode Modo del mapa (mirar MapMode)
     */
    public MapFragmentParams(PointType typePoints, PointOrder orderPoints, MapMode mapMode) {
        this(typePoints, orderPoints, mapMode, false, LocationRequest
                .PRIORITY_HIGH_ACCURACY, null, -1);
    }

    @ParcelConstructor
    public MapFragmentParams(PointType typePoints, PointOrder orderPoints, MapMode mapMode,
                             boolean showInfoWindow, int locationPriority, MapStyleOptions style,
                             int mapType) {
        setTypePoints(typePoints).setOrderPoints(orderPoints).setMapMode(mapMode)
                .setLocationPriority(locationPriority).setStyle(style).setMapType(mapType);
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

    public int getLocationPriority() {
        return locationPriority;
    }

    public MapFragmentParams setLocationPriority(int locationPriority) {
        this.locationPriority = locationPriority;
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
