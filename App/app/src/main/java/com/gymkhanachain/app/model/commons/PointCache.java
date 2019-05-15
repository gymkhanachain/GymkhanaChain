package com.gymkhanachain.app.model.commons;

import android.util.Log;

import com.gymkhanachain.app.commons.GymkConstants;
import com.gymkhanachain.app.model.beans.PointBean;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class PointCache {
    private static final String TAG = "PointCache";

    private static PointCache instance = null;

    private Queue<Integer> pointsIds;
    private Map<Integer, PointBean> points;

    private PointCache() {
        pointsIds = new ArrayDeque<>();
        points = new HashMap<>();
    }

    public static synchronized PointCache getInstance() {
        if (instance == null) {
            instance = new PointCache();
        }

        return instance;
    }


    public synchronized PointBean getPoint(Integer id) {
        PointBean pointBean = points.get(id);
        return pointBean;
    }

    public synchronized void setPoint(PointBean pointBean) {
        Log.i(TAG, "Caching " + pointBean.getName());

        if (pointsIds.size() == GymkConstants.MAX_ELEMENTS_CACHED) {
            Log.w(TAG, "Poll filled, removing old points");
            Integer id = pointsIds.poll();
            points.remove(id);
        }

        pointsIds.offer(pointBean.getId());
        points.put(pointBean.getId(), pointBean);
    }
}
