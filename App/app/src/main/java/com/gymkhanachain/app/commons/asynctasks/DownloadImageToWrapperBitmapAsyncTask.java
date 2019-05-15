package com.gymkhanachain.app.commons.asynctasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.gymkhanachain.app.commons.WrapperBitmap;
import com.gymkhanachain.app.model.commons.ImageCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DownloadImageToWrapperBitmapAsyncTask extends AsyncTask<Uri, Void, Bitmap> {

    private static final String TAG = "DownloadImageToWrapper";
    private static final ImageCache imgCache = ImageCache.getInstance();

    private WrapperBitmap wrapper;

    public DownloadImageToWrapperBitmapAsyncTask(WrapperBitmap wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    protected Bitmap doInBackground(Uri... uris) {
        String url = uris[0].toString();
        Bitmap bitmap = imgCache.getBitmap(url);

        if (bitmap == null) {
            try (InputStream inputStream = new URL(uris[0].toString()).openStream()) {
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                Log.e(TAG, "Error al descargar imagen", e);
            }
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imgCache.setBitmap(wrapper.getUri().toString(), bitmap);
        wrapper.setBitmap(bitmap);
    }
}
