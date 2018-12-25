package com.softmine.drpedia.home.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
    ProgressBar bar;
    TouchImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pinch_zoom_image);
       imageView = (TouchImageView)findViewById(R.id.imageView);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loading = (ImageView) findViewById(R.id.loading);
        bar = findViewById(R.id.progress_bar);
        setTitle("");
        imageUrl = getIntent().getStringExtra("picture_url");
        storageSrc = getIntent().getStringExtra("storage_src");

        Log.d("loadingimage","path=="+ CaseStudyAPIURL.BASE_URL_image_load+imageUrl);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(storageSrc.equalsIgnoreCase("storage"))
        {
            glide.with(this)
                    .load("file://"+imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .placeholder(R.drawable.loading3)
                    .into(imageView);
        }
        else
        {
            glide.with(this)
                    .load(CaseStudyAPIURL.BASE_URL_image_load+getIntent().getStringExtra("picture_url"))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .placeholder(R.drawable.loading3)
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
