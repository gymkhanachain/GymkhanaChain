package com.gymkhanachain.app.commons.asynctasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import com.gymkhanachain.app.model.commons.ImageCache;

import java.io.InputStream;
import java.net.URL;

public class DownloadImageToImageViewAsyncTask extends AsyncTask<Uri, Void, Bitmap> {
    private static final ImageCache imgCache = ImageCache.getInstance();

    private Context appContext;
    private ImageView targetView;

    public DownloadImageToImageViewAsyncTask(Context context, ImageView view) {
        appContext = context;
        targetView = view;
    }

    @Override
    protected Bitmap doInBackground(Uri... uris) {
        String url = uris[0].toString();
        Bitmap bitmap = imgCache.getBitmap(url);

        if (bitmap == null) {
            try {
                InputStream inputStream = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                imgCache.setBitmap(url, bitmap);
                inputStream.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if (bitmap != null)
            targetView.setImageBitmap(bitmap);
        else
            Toast.makeText(appContext, "Error al cargar imagen en " + targetView.toString(), Toast.LENGTH_SHORT).show();
    }
}
