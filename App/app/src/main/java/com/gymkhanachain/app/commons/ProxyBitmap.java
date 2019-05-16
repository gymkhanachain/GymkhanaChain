package com.gymkhanachain.app.commons;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class ProxyBitmap {
    private List<OnProxyBitmapListener> listeners;
    private Bitmap bitmap = null;
    private Uri uri = null;

    public ProxyBitmap() {
        listeners = new ArrayList<>();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;

        for (OnProxyBitmapListener listener : listeners) {
            listener.onBitmapChange(this);
        }
    }

    public void attach(OnProxyBitmapListener listener) {
        listeners.add(listener);
    }

    public void detach(OnProxyBitmapListener listener) {
        listeners.remove(listener);
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public interface OnProxyBitmapListener {
        void onBitmapChange(ProxyBitmap bitmap);
    }
}
