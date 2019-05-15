package com.gymkhanachain.app.model.commons;

import com.gymkhanachain.app.commons.GymkConstants;
import com.gymkhanachain.app.model.beans.PointBean;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class PointCache {
    private static Queue<Integer> pointsIds;
    private static Map<Integer, PointBean> points;
    private static PointCache instance = null;

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

        // TODO: comprobar que si no existe, se obtiene del modelo y se invoca a setPoint

        return pointBean;
    }

    public synchronized void setPoint(PointBean pointBean) {
        if (pointsIds.size() == GymkConstants.MAX_ELEMENTS_CACHED) {
            Integer id = pointsIds.poll();
            points.remove(id);
        }

        pointsIds.offer(pointBean.getId());
        points.put(pointBean.getId(), pointBean);
    }
}
