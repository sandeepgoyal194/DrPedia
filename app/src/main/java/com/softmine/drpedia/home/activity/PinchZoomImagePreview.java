package com.softmine.drpedia.home.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.softmine.drpedia.R;
import com.softmine.drpedia.home.net.CaseStudyAPIURL;
import com.softmine.drpedia.utils.ScaleBitmapTransformation;


public class PinchZoomImagePreview extends AppCompatActivity {

    protected Toolbar mToolbar;
    boolean visible=true;

    ImageView loading;
    Glide glide;

    String imageUrl;
    String storageSrc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pinch_zoom_image);
       TouchImageView imageView = (TouchImageView)findViewById(R.id.imageView);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loading = (ImageView) findViewById(R.id.loading);

        setTitle("");
        imageUrl = getIntent().getStringExtra("picture_url");
        storageSrc = getIntent().getStringExtra("storage_src");

        Log.d("loadingimage","path=="+ CaseStudyAPIURL.BASE_URL_image_load+imageUrl);

        if(storageSrc.equalsIgnoreCase("storage"))
        {
            glide.with(this)
                    .load("file://"+imageUrl)
                    .bitmapTransform(new ScaleBitmapTransformation(this))
                    .listener(new com.bumptech.glide.request.RequestListener<String, com.bumptech.glide.load.resource.drawable.GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, com.bumptech.glide.request.target.Target<com.bumptech.glide.load.resource.drawable.GlideDrawable> target, boolean isFirstResource) {
                            Log.d("hrview","false");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(com.bumptech.glide.load.resource.drawable.GlideDrawable resource, String model, com.bumptech.glide.request.target.Target<com.bumptech.glide.load.resource.drawable.GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Log.d("hrview","readey");

                            loading.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);
        }
        else
        {
            glide.with(this)
                    .load(CaseStudyAPIURL.BASE_URL_image_load+getIntent().getStringExtra("picture_url"))
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Log.d("loadedimage","false");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Log.d("loadedimage","readey");

                            loading.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(visible) {
                    //               getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
                    getSupportActionBar().hide();
                    visible=false;
                }
                else
                {
                    getSupportActionBar().show();
                    visible=true;
                }
            }
        });
    }

    public void setmToolbarColor(int color) {
        if(mToolbar != null) {
            mToolbar.setBackgroundColor(color);
        }
    }
    public void setmToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back_black));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cross_full_image,menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.menu_item_cross_image:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
