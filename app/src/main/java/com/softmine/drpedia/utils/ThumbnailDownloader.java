package com.softmine.drpedia.utils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public  class ThumbnailDownloader {

    public static void download(String url, ImageView imageView , int position) {
        if (cancelPotentialDownload(url, imageView,position)) {
            ThumbnailDownloaderTask task = new ThumbnailDownloaderTask(imageView);
            DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
            imageView.setImageDrawable(downloadedDrawable);
            task.execute(url , Integer.toString(position));
        }
    }

    private static boolean cancelPotentialDownload(String url, ImageView imageView , int position) {
       ThumbnailDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

        if (bitmapDownloaderTask != null) {
            String bitmapUrl = bitmapDownloaderTask.url;
           // int pos = Integer.parseInt(bitmapDownloaderTask.position);
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                bitmapDownloaderTask.cancel(true);
            } else {
                // The same URL is already being downloaded.
                return false;
            }
        }
        return true;
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

