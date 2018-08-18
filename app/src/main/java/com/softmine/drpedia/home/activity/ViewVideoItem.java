package com.softmine.drpedia.home.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;


import com.halilibo.bettervideoplayer.BetterVideoCallback;
import com.halilibo.bettervideoplayer.BetterVideoPlayer;
import com.softmine.drpedia.R;
import com.softmine.drpedia.home.net.CaseStudyAPIURL;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewVideoItem extends AppCompatActivity implements BetterVideoCallback {


    @BindView(R.id.bvp)
    BetterVideoPlayer bvp;

    String storageSrc;
    String videoUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video_item);
        ButterKnife.bind(this);
        videoUrl = getIntent().getStringExtra("video_url");
        storageSrc = getIntent().getStringExtra("storage_src");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("hrview","video path===="+Uri.parse(videoUrl).toString());
        bvp.setAutoPlay(true);
        bvp.setCallback(this);
        if(storageSrc.equalsIgnoreCase("storage"))
        {
            bvp.setSource(Uri.fromFile(new File(videoUrl)));
        }
        else
        {
            bvp.setSource(Uri.parse(CaseStudyAPIURL.BASE_URL_image_load+videoUrl));
        }
        bvp.setHideControlsOnPlay(true);
        bvp.enableSwipeGestures(getWindow());
    }

    @Override
    public void onPause(){
        bvp.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        bvp.stop();
        bvp.reset();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        bvp.release();
        super.onDestroy();
    }

    @Override
    public void onStarted(BetterVideoPlayer player) {

        Log.d("videoview","video started");
    }

    @Override
    public void onPaused(BetterVideoPlayer player) {
        Log.d("videoview","video paused");
    }

    @Override
    public void onPreparing(BetterVideoPlayer player) {

    }

    @Override
    public void onPrepared(BetterVideoPlayer player) {
        Log.d("videoview","video prepared");
    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(BetterVideoPlayer player, Exception e) {

    }

    @Override
    public void onCompletion(BetterVideoPlayer player) {
        Log.d("videoview","video completed");
    }

    @Override
    public void onToggleControls(BetterVideoPlayer player, boolean isShowing) {
        Log.d("videoview","video toggle");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
