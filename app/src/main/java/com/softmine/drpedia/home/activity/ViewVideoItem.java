package com.softmine.drpedia.home.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;


import com.softmine.drpedia.R;
import com.softmine.drpedia.home.net.CaseStudyAPIURL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewVideoItem extends AppCompatActivity {

    @BindView(R.id.case_video)
    VideoView videoView;

    MediaController mediaController;
    String storageSrc;
    String videoUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video_item);
        ButterKnife.bind(this);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoUrl = getIntent().getStringExtra("video_url");
        storageSrc = getIntent().getStringExtra("storage_src");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("hrview","video path===="+Uri.parse(videoUrl).toString());

        videoView.setVisibility(View.VISIBLE);
        videoView.setMediaController(mediaController);
        if(storageSrc.equalsIgnoreCase("storage"))
        {
            videoView.setVideoURI(Uri.parse(videoUrl));
        }
        else
        {
            videoView.setVideoURI(Uri.parse(CaseStudyAPIURL.BASE_URL_image_load+getIntent().getStringExtra("video_url")));
        }
        videoView.requestFocus();
        videoView.seekTo(1000);
        videoView.start();
    }
}
