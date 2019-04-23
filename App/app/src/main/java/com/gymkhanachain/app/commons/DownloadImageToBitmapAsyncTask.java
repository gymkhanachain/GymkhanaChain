package com.gymkhanachain.app.commons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

public class DownloadImageToBitmapAsyncTask extends AsyncTask<Uri, Void, Bitmap> {

    private Context appContext;
    private ImageView targetView;

    public DownloadImageToBitmapAsyncTask(Context context, ImageView view) {
        appContext = context;
        targetView = view;
    }

    @Override
    protected Bitmap doInBackground(Uri... uris) {

        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(uris[0].toString()).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

        } catch (java.io.IOException e) {
            e.printStackTrace();
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
