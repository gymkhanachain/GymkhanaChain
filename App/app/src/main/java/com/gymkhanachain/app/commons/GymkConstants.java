package com.gymkhanachain.app.commons;

import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapMode;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.PointOrder;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.PointType;

public class GymkConstants {
    /**
     * Número máximo de elementos que se pueden cachear
     */
    public static final int MAX_ELEMENTS_CACHED = 24;

    // Elementos por defecto

    /**
     * Tipo de puntos por defecto
     */
    public static final PointType DEFAULT_POINT_TYPE = PointType.GYMKHANA_POINTS;

    /**
     * Orden de los puntos por defecto
     */
    public static final PointOrder DEFAULT_POINT_ORDER = PointOrder.NONE_ORDER;

    /**
     * Modo del mapa por defecto
     */
    public static final MapMode DEFAULT_MAP_MODE = MapMode.NORMAL_MODE;

    /**
     * Tiempo minimo de actualización por defecto (en segundos)
     */
    public static final float DEFAULT_MIN_LOCATION_INTERVAL = 2.0f;

    /**
     * Tiempo máximo de actualización por defecto (en segundos)
     */
    public static final float DEFAULT_MAX_LOCATION_INTERVAL = 5.0f;

    /**
     * Distancia para lanzar los puntos por defecto (en metros)
     */
    public static final int DEFAULT_TRIGGERED_DISTANCE = 20;
}
