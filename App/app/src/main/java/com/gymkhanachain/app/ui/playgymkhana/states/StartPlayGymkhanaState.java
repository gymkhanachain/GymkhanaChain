package com.gymkhanachain.app.ui.playgymkhana.states;

import android.location.Location;
import android.widget.Toast;

import com.gymkhanachain.app.R;
import com.gymkhanachain.app.commons.GymkConstants;
import com.gymkhanachain.app.model.beans.GymkhanaBean;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapPoint;
import com.gymkhanachain.app.ui.playgymkhana.activities.PlayGymkhanaActivity;

import java.util.List;

/**
 * Esta clase representa el estado inicial al ejecutar una gymkhana, que te pedirá ir hasta el
 * punto inicial para ejecutar la gymkhana
 */
public class StartPlayGymkhanaState extends PlayGymkhanaState {
    private Location currentPosition;
    private MapPoint point;

    public StartPlayGymkhanaState(PlayGymkhanaActivity activity, Location currentPosition) {
        super(activity);
        this.currentPosition = currentPosition;
    }

    @Override
    public PlayGymkhanaState onCreateState(GymkhanaBean bean) {
        setBean(bean);

        Location beanLocation = new Location("");
        beanLocation.setLatitude(bean.getPosition().latitude);
        beanLocation.setLongitude(bean.getPosition().longitude);

        // Comprobamos si no estamos en el punto inicial para indicar al jugador que vaya a este
        if (currentPosition.distanceTo(beanLocation) > GymkConstants.DEFAULT_TRIGGERED_DISTANCE) {
            Toast toast = Toast.makeText(getActivity(), getActivity().getString(R.string.start_gymkhana), Toast.LENGTH_SHORT);
            toast.show();
            // Añadimos el punto inicial al mapa
            point = new MapPoint(bean.getId(), bean.getPosition(), bean.getName());
            getActivity().addPoint(point);
            return this;
        }

        // Si estamos en el punto devolvemos el nuevo estado
        PlayGymkhanaState state = new PlayingGymkhanaState(getActivity());
        state.onCreateState(bean);
        return state;
    }

    @Override
    public PlayGymkhanaState onMapPointsNearLocation(List<MapPoint> points) {
        // No debería de haber más de un punto en colisión
        getActivity().removePoint(points.get(0));

        PlayGymkhanaState state = new PlayingGymkhanaState(getActivity());
        state.onCreateState(getBean());
        return state;
    }
}
