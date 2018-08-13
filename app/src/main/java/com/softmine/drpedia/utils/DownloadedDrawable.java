package com.softmine.drpedia.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import java.lang.ref.WeakReference;

class DownloadedDrawable extends ColorDrawable {
    private final WeakReference<ThumbnailDownloaderTask> bitmapDownloaderTaskReference;

    public DownloadedDrawable(ThumbnailDownloaderTask bitmapDownloaderTask) {
        super(Color.BLACK);
        bitmapDownloaderTaskReference =
                new WeakReference<ThumbnailDownloaderTask>(bitmapDownloaderTask);
    }

    public ThumbnailDownloaderTask getBitmapDownloaderTask() {
        return bitmapDownloaderTaskReference.get();
    }
}