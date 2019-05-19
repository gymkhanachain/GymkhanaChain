package com.gymkhanachain.app.ui.playgymkhana.states;

import android.util.Log;

import com.gymkhanachain.app.model.beans.GymkhanaBean;
import com.gymkhanachain.app.model.beans.PointBean;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapPoint;
import com.gymkhanachain.app.ui.playgymkhana.activities.PlayGymkhanaActivity;

import java.util.List;

/**
 * Este estado es el que se ejecuta mientras estás jugando la gymkhana
 */
public class PlayingGymkhanaState extends PlayGymkhanaState {
    private int pointsCompleted = 0;

    public PlayingGymkhanaState(PlayGymkhanaActivity activity) {
        super(activity);
    }

    @Override
    public PlayGymkhanaState onCreateState(GymkhanaBean bean) {
        setBean(bean);

        // Recorremos la lista de puntos y las mostramos en el mapa
        for (PointBean pointBean: bean.getPoints()) {
            Log.i("PlayGymkhanaState", pointBean.getName());
            getActivity().addPoint(new MapPoint(pointBean.getId(), pointBean.getPosition(), pointBean.getName()));
        }

        return this;
    }

    @Override
    public PlayGymkhanaState onMapPointsNearLocation(List<MapPoint> points) {
        // Cogemos el primer punto de colisión
        MapPoint mapPoint = points.get(0);

        // Lo eliminamos del mapa
        getActivity().removePoint(mapPoint);

        // Le indicamos a la clase que hay que lanzar la actividad del punto
        getActivity().startPointActivity(mapPoint.getId());
        pointsCompleted++;

        if (pointsCompleted == getBean().getPoints().size()) {
            return null;
        }

        return this;
    }
}
