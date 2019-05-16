package com.gymkhanachain.app.model.commons;

import android.util.Log;

import com.gymkhanachain.app.client.Gymkhana;
import com.gymkhanachain.app.client.RestServ;
import com.gymkhanachain.app.commons.GymkConstants;
import com.gymkhanachain.app.model.adapters.GymkAdapter;
import com.gymkhanachain.app.model.beans.GymkhanaBean;
import com.gymkhanachain.app.model.beans.PointBean;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class GymkhanaCache {
    private static final String TAG = "GymkhanaCache";

    private static GymkhanaCache instance = null;

    private Queue<Integer> gymkhanaIds;
    private Map<Integer, GymkhanaBean> gymkhanas;

    private GymkhanaCache() {
        gymkhanaIds = new ArrayDeque<>();
        gymkhanas = new HashMap<>();
    }

    public static synchronized GymkhanaCache getInstance() {
        if (instance == null) {
            instance = new GymkhanaCache();
        }

        return instance;
    }

    public synchronized GymkhanaBean getGymkhana(Integer id) {
        GymkhanaBean gymkhanaBean = gymkhanas.get(id);

        if (gymkhanaBean == null) {
            Gymkhana gymkhana = RestServ.getGymkhanaById(id);

            if (gymkhana != null) {
                setGymkhana(GymkAdapter.adapt(gymkhana));
            }
        }

        return gymkhanaBean;
    }

    public synchronized void setGymkhana(GymkhanaBean gymkhanaBean) {
        Log.i(TAG, "Caching " + gymkhanaBean.getName());

        if (gymkhanaIds.size() == GymkConstants.MAX_ELEMENTS_CACHED) {
            Log.w(TAG, "Poll filled, removing old gymkhanas");
            Integer id = gymkhanaIds.poll();
            gymkhanas.remove(id);
        }

        gymkhanaIds.offer(gymkhanaBean.getId());
        gymkhanas.put(gymkhanaBean.getId(), gymkhanaBean);

        PointCache pointCache = PointCache.getInstance();
        for (PointBean bean : gymkhanaBean.getPoints()) {
            pointCache.setPoint(bean);
        }
    }
}
