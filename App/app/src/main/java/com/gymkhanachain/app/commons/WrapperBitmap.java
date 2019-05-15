package com.gymkhanachain.app.commons;

import android.graphics.Bitmap;
import android.net.Uri;

public class WrapperBitmap {
    OnWrapperBitmapListener listener;
    Bitmap bitmap;
    Uri uri;

    public WrapperBitmap(Uri uri, OnWrapperBitmapListener listener) {
        this.uri = uri;
        this.listener = listener;
    }

    public synchronized Bitmap getBitmap() {
        return bitmap;
    }

    public synchronized void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;

        if (listener != null) {
            listener.onBitmapChange(this);
        }
    }

    public OnWrapperBitmapListener getListener() {
        return listener;
    }

    public void setListener(OnWrapperBitmapListener listener) {
        this.listener = listener;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public interface OnWrapperBitmapListener {
        void onBitmapChange(WrapperBitmap bitmap);
    }
}
