package com.gymkhanachain.app.model.commons;

import android.graphics.Bitmap;
import android.util.Log;

import com.gymkhanachain.app.commons.GymkConstants;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class ImageCache {
    private static final String TAG = "ImageCache";

    private static ImageCache instance = null;

    private Queue<String> urls;
    private Map<String, Bitmap> images;

    private ImageCache() {
        urls = new ArrayDeque<>();
        images = new HashMap<>();
    }

    public static synchronized ImageCache getInstance()  {
        if (instance == null) {
            instance = new ImageCache();
        }

        return instance;
    }

    public Bitmap getBitmap(String url) {
        return images.get(url);
    }

    public void setBitmap(String url, Bitmap image) {
        Log.i(TAG, "Caching image: " + url);

        if (urls.size() == GymkConstants.MAX_ELEMENTS_CACHED) {
            Log.w(TAG, "Poll filled, removing old images");
            String key = urls.poll();
            images.remove(key);
        }

        images.put(url, image);
    }
}
