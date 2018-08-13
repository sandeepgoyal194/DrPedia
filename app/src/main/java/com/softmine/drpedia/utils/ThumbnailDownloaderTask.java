package com.softmine.drpedia.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class ThumbnailDownloaderTask extends AsyncTask<String, Void, Bitmap> {

    String url;
    String position;
    private final WeakReference<ImageView> imageViewReference;

    public ThumbnailDownloaderTask(ImageView imageView) {
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        // params comes from the execute() call: params[0] is the url.
        Bitmap videoThumb = null;
        url = params[0];
        position = params[1];
        try {
            videoThumb =  retriveVideoFrameFromVideo(url);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return videoThumb;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            ThumbnailDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
            // Change bitmap only if this process is still associated with it
            if (this == bitmapDownloaderTask) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable
    {
        Log.d("downloadVideo","inside retriveVideoFrameFromVideo");
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        Uri uri = Uri.parse(videoPath);
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            Log.d("downloadVideo","inside retriveVideoFrameFromVideo object");
            if (Build.VERSION.SDK_INT >= 14) {
                Log.d("downloadVideo","data source created");
                mediaMetadataRetriever.setDataSource(uri.toString(), new HashMap<String, String>());
                Log.d("downloadVideo","data source created1");
            }
            else {
                Log.d("downloadVideo","data source created2");
                mediaMetadataRetriever.setDataSource(uri.toString());
                Log.d("downloadVideo","data source created3");
            }
            //   mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(2000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            Log.d("downloadVideo","bitmap created");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    private static ThumbnailDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }

}
