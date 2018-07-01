package frameworks.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;



/**
 * Created by sandeep.g9 on 10/20/2016.
 */

public class GlideImageLoaderImpl implements ImageLoader {
    Context mContext;

    public GlideImageLoaderImpl(Context mContext) {
        this.mContext = mContext;
    }

    public String printMsg()
    {
        return "image loader called";
    }


    @Override
    public void loadImage(String url, ImageView imageView) {
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .into(imageView);
    }

    public void loadImage(String url, ImageView imageView, int placeHolderID) {
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .placeholder(placeHolderID)
                .into(imageView);
    }



}
