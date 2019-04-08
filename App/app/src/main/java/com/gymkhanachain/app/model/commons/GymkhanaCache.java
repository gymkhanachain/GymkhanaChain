package com.gymkhanachain.app.model.commons;

import com.gymkhanachain.app.model.beans.GymkhanaBean;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class GymkhanaCache {
    private static Queue<Integer> gymkhanaIds;
    private static Map<Integer, GymkhanaBean> gymkhanas;
    private static GymkhanaCache instance = null;

    private static int MAX_ELEMENTS = 16;

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

        // TODO: comprobar que si no existe, se obtiene del modelo y se invoca a setGymkhana

        return gymkhanaBean;
    }

    public synchronized void setGymkhana(GymkhanaBean gymkhanaBean) {
        if (gymkhanaIds.size() == MAX_ELEMENTS) {
            Integer id = gymkhanaIds.poll();
            gymkhanas.remove(id);
        }

        gymkhanaIds.offer(gymkhanaBean.getId());
        gymkhanas.put(gymkhanaBean.getId(), gymkhanaBean);
    }
}
