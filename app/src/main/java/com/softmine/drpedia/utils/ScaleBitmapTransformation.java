package com.softmine.drpedia.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class ScaleBitmapTransformation extends BitmapTransformation {

    public ScaleBitmapTransformation(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {

        Log.d("bitmapvalue",""+toTransform.getWidth());
        Log.d("bitmapvalue",""+toTransform.getHeight());
        Log.d("bitmapvalue",""+outWidth);
        Log.d("bitmapvalue",""+outHeight);

        if (toTransform.getWidth() == outWidth && toTransform.getHeight() == outHeight) {
            return toTransform;
        }

        return Bitmap.createScaledBitmap(toTransform, outWidth, outHeight, /*filter=*/ true);
    }

    @Override
    public String getId() {
        return "ScaleBitmapTransformation";
    }
}
