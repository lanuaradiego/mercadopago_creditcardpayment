package com.dlanuara.ejercicioc.utilities.imagedownloadasynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class ImageDownloadAsyncTask extends AsyncTask<String, Integer, Bitmap> {
    private final static String TAG = ImageDownloadAsyncTask.class.getName();

    private ImageDownloadedListener toload;

    public ImageDownloadAsyncTask(ImageDownloadedListener ondownloaded){
        this.toload = ondownloaded;
    }

    @Override
    protected @Nullable Bitmap doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        catch (MalformedURLException e){
            Log.e(TAG, "Error on download " +  urls[0], e);
            return null;
        }
        catch (IOException e){
            Log.e(TAG, "Error on download " + urls[0], e);
            return null;
        }
        catch (Exception e){
            Log.e(TAG, "Error on download " + urls[0], e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(@Nullable Bitmap bitmap){
        toload.onImageDownloaded(bitmap);
    }
}
