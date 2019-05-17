package com.gymkhanachain.app.ui.playgymkhana.states;

import com.gymkhanachain.app.model.beans.GymkhanaBean;
import com.gymkhanachain.app.ui.commons.fragments.mapfragment.MapPoint;
import com.gymkhanachain.app.ui.playgymkhana.activities.PlayGymkhanaActivity;

import java.util.List;

public abstract class PlayGymkhanaState {

    private GymkhanaBean bean;
    private PlayGymkhanaActivity activity;

    public PlayGymkhanaState(PlayGymkhanaActivity activity) {
        setActivity(activity);
    }

    GymkhanaBean getBean() {
        return this.bean;
    }

    void setBean(GymkhanaBean bean) {
        this.bean = bean;
    }

    PlayGymkhanaActivity getActivity() {
        return this.activity;
    }

    void setActivity(PlayGymkhanaActivity activity) {
        this.activity = activity;
    }

    abstract public PlayGymkhanaState onCreateState(GymkhanaBean bean);

    abstract public PlayGymkhanaState onMapPointsNearLocation(List<MapPoint> points);
}
