package com.gymkhanachain.app.commons;

import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapMode;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.PointOrder;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.PointType;

public class GymkConstants {
    /**
     * Número máximo de elementos que se pueden cachear
     */
    public static int MAX_ELEMENTS_CACHED = 16;

    // Elementos por defecto

    /**
     * Tipo de puntos por defecto
     */
    public static PointType DEFAULT_POINT_TYPE = PointType.GIS_POINTS;

    /**
     * Orden de los puntos por defecto
     */
    public static PointOrder DEFAULT_POINT_ORDER = PointOrder.NONE_ORDER;

    /**
     * Modo del mapa por defecto
     */
    public static MapMode DEFAULT_MAP_MODE = MapMode.NORMAL_MODE;

    /**
     * Tiempo minimo de actualización por defecto (en segundos)
     */
    public static float DEFAULT_MIN_LOCATION_INTERVAL = 2.0f;

    /**
     * Tiempo máximo de actualización por defecto (en segundos)
     */
    public static float DEFAULT_MAX_LOCATION_INTERVAL = 5.0f;

    /**
     * Distancia para lanzar los puntos por defecto (en metros)
     */
    public static int DEFAULT_TRIGGERED_DISTANCE = 20;
}
