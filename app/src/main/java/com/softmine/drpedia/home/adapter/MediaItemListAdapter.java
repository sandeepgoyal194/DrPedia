package com.softmine.drpedia.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.halilibo.bettervideoplayer.BetterVideoPlayer;
import com.softmine.drpedia.R;
import com.softmine.drpedia.home.activity.PinchZoomImagePreview;
import com.softmine.drpedia.home.activity.ViewVideoItem;
import com.softmine.drpedia.home.model.CaseMediaItem;
import com.softmine.drpedia.home.net.CaseStudyAPIURL;
import com.softmine.drpedia.utils.ScaleBitmapTransformation;
import com.softmine.drpedia.utils.ThumbnailDownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import frameworks.imageloader.ImageLoader;

public class MediaItemListAdapter extends RecyclerView.Adapter<MediaItemListAdapter.CategoryHolder>{

    List<CaseMediaItem> mediaItemList= new ArrayList<>();
    Context mContext;

    @Inject
    ImageLoader mImageLoader;


    public MediaItemListAdapter(Context context) {
        this.mContext = context;
    }


    @Override
    public MediaItemListAdapter.CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d("mediaholder","onCreateViewHolder");

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.case_mediaitem, null);
        return new CategoryHolder(v);
    }


    @Override
    public void onViewAttachedToWindow(CategoryHolder holder) {
        Log.d("mediaholder","onViewAttachedToWindow");
        super.onViewAttachedToWindow(holder);
      /*  holder.bvp.reset();
        holder.bvp.release();*/
    }

    @Override
    public void onViewDetachedFromWindow(CategoryHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.d("mediaholder","onViewDetachedFromWindow");
        Log.d("viewdetach","detached=="+holder.getAdapterPosition());

    /*    holder.bvp.reset();
        holder.bvp.release();*/

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {

        super.onAttachedToRecyclerView(recyclerView);
        Log.d("mediaholder","onAttachedToRecyclerView");
        //notifyDataSetChanged();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Log.d("mediaholder","onDetachedFromRecyclerView");
    }

    @Override
    public void onViewRecycled(CategoryHolder holder) {
        super.onViewRecycled(holder);
        Log.d("mediaholder","onViewRecycled");
        //notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MediaItemListAdapter.CategoryHolder holder, int position) {
        Log.d("mediaholder","onBindViewHolder");
        final CaseMediaItem caseMediaItem = mediaItemList.get(position);
        holder.setContent(caseMediaItem);
        Log.d("downloadVideo", "position=="+position);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(caseMediaItem.isImageAvailable())
                {
                    Intent i = new Intent(mContext, PinchZoomImagePreview.class);
                    i.putExtra("picture_url",caseMediaItem.getImage());

                    if(caseMediaItem.getSrc()!=null)
                    {
                        i.putExtra("storage_src","storage");
                    }
                    else
                    {
                        i.putExtra("storage_src","url");
                    }

                    mContext.startActivity(i);
                }
                else if(caseMediaItem.isVideoAvailable())
                {
                    Intent i = new Intent(mContext, ViewVideoItem.class);
                    i.putExtra("video_url",caseMediaItem.getVideo());
                    if(caseMediaItem.getSrc()!=null)
                    {
                        i.putExtra("storage_src","storage");
                    }
                    else
                    {
                        i.putExtra("storage_src","url");
                    }
                    mContext.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mediaItemList.size();
    }

    public void setMediaItemList(List<CaseMediaItem> mediaItemList)
    {
        Log.d("hrview","list size=="+mediaItemList.size());

        for(CaseMediaItem item : mediaItemList)
        {
            Log.d("mediaData","image=="+item.getImage());
            Log.d("mediaData","video=="+item.getVideo());
            Log.d("mediaData","thumbnail=="+item.getThumbnail());
        }

        if(mediaItemList!=null)
        {
            this.mediaItemList = mediaItemList;
            notifyDataSetChanged();
        }
    }


    class CategoryHolder extends RecyclerView.ViewHolder implements Player.EventListener {

        @BindView(R.id.case_image)
        ImageView mediaImage;

        @BindView(R.id.img_loading)
        ImageView img_loading;

        CaseMediaItem mMediaItem;

        @BindView(R.id.thumbview)
        ImageView thumbview;

        @BindView(R.id.case_playIcon)
        ImageView casePlayIcon;

        @BindView(R.id.player_view)
        PlayerView mPlayerView;

        private SimpleExoPlayer player;
        private DefaultTrackSelector trackSelector;
        private boolean shouldAutoPlay;
        private BandwidthMeter bandwidthMeter;

        private boolean playWhenReady;
        private int currentWindow;
        private long playbackPosition;

        @BindView(R.id.videoContainer)
        RelativeLayout videoContainer;

        @BindView(R.id.progress_bar)
        ProgressBar bar;

        Glide glide;
        View mView;

        public CategoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            mView = itemView;
            player = null;
            playWhenReady = true;
            currentWindow = 0;
            playbackPosition = 0;
            shouldAutoPlay = false;
            bandwidthMeter = new DefaultBandwidthMeter();
            initializePlayer();
            bar.setVisibility(View.VISIBLE);
        }


        public void setContent(CaseMediaItem caseMediaItem) {

            mMediaItem = caseMediaItem;
            Log.d("hrview","=================================");
            Log.d("hrview","url =="+caseMediaItem.getVideo());
            Log.d("hrview","url =="+caseMediaItem.getImage());
            if(caseMediaItem.isImageAvailable())
            {
                Log.d("hrview","image available");

                if(caseMediaItem.getSrc()!=null)
                {
                    Log.d("hrview","image source "+caseMediaItem.getSrc());
                    glide.with(mView.getContext())
                            .load("file://"+caseMediaItem.getImage())
                            .bitmapTransform(new ScaleBitmapTransformation(mView.getContext()))
                            .listener(new com.bumptech.glide.request.RequestListener<String, com.bumptech.glide.load.resource.drawable.GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, com.bumptech.glide.request.target.Target<com.bumptech.glide.load.resource.drawable.GlideDrawable> target, boolean isFirstResource) {
                                    Log.d("hrview","false");
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(com.bumptech.glide.load.resource.drawable.GlideDrawable resource, String model, com.bumptech.glide.request.target.Target<com.bumptech.glide.load.resource.drawable.GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    Log.d("hrview","readey");

                                    img_loading.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(mediaImage);
                    mediaImage.setVisibility(View.VISIBLE);
                    videoContainer.setVisibility(View.GONE);
                    thumbview.setVisibility(View.GONE);
                    casePlayIcon.setVisibility(View.GONE);
                    bar.setVisibility(View.GONE);
                }
                else
                {
                    Log.d("hrview","image source "+caseMediaItem.getSrc());
                    glide.with(mView.getContext())
                            .load(CaseStudyAPIURL.BASE_URL_image_load+caseMediaItem.getImage())
                            .bitmapTransform(new ScaleBitmapTransformation(mView.getContext()))
                            .listener(new com.bumptech.glide.request.RequestListener<String, com.bumptech.glide.load.resource.drawable.GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, com.bumptech.glide.request.target.Target<com.bumptech.glide.load.resource.drawable.GlideDrawable> target, boolean isFirstResource) {
                                    Log.d("hrview","false");
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(com.bumptech.glide.load.resource.drawable.GlideDrawable resource, String model, com.bumptech.glide.request.target.Target<com.bumptech.glide.load.resource.drawable.GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    Log.d("hrview","readey");

                                    img_loading.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(mediaImage);
                    mediaImage.setVisibility(View.VISIBLE);
                    videoContainer.setVisibility(View.GONE);
                    thumbview.setVisibility(View.GONE);
                    casePlayIcon.setVisibility(View.GONE);
                    bar.setVisibility(View.GONE);
                }

                //scaleImage(mediaImage);

            }
            else if (caseMediaItem.isVideoAvailable())
            {
                Log.d("hrview","video available");
                Log.d("hrview","video src===="+caseMediaItem.getSrc());
           //     bvp.reset();
                //bvp.release();
                if(caseMediaItem.getSrc()!=null)
                {
                    Log.d("hrview","video source "+caseMediaItem.getSrc());
                    /*glide.with(mView.getContext())
                            .load(caseMediaItem.getVideo())
                            .centerCrop()
                            .crossFade()
                            .into(thumbview);

                   *//* Bitmap bmThumbnail = createThumbnailFromPath(caseMediaItem.getVideo(), MediaStore.Video.Thumbnails.MINI_KIND);
                    thumbview.setImageBitmap(bmThumbnail);*//*
                    thumbview.setVisibility(View.VISIBLE);
                    casePlayIcon.setVisibility(View.VISIBLE);
                    mediaImage.setVisibility(View.GONE);*/

                  //  bvp.setAutoPlay(false);
                  //  bvp.setSource( Uri.fromFile(new File(caseMediaItem.getVideo())));
                    Log.d("hrview","file video source "+Uri.fromFile(new File(caseMediaItem.getVideo())).toString());
                    MediaSource mediaSource = buildFileMediaSource(Uri.fromFile(new File(caseMediaItem.getVideo())));
                    preparePlayer(mediaSource);

                    videoContainer.setVisibility(View.VISIBLE);
                    casePlayIcon.setVisibility(View.VISIBLE);
                    mediaImage.setVisibility(View.GONE);
                  //  bvp.start();
                   // bvp.pause();
                }
                else
                {
                    Log.d("hrview","video source "+caseMediaItem.getSrc());
                   /* ThumbnailDownloader.download(Uri.parse(CaseStudyAPIURL.BASE_URL_image_load+caseMediaItem.getVideo()).toString(),thumbview , this.getAdapterPosition());
                    thumbview.setVisibility(View.VISIBLE);
                    casePlayIcon.setVisibility(View.VISIBLE);
                    mediaImage.setVisibility(View.GONE);*/
                  //  bvp.setAutoPlay(false);
                  //  bvp.setSource(Uri.parse(CaseStudyAPIURL.BASE_URL_image_load+caseMediaItem.getVideo()));

                    MediaSource mediaSource = buildMediaSource(Uri.parse(CaseStudyAPIURL.BASE_URL_image_load+caseMediaItem.getVideo()));
                    preparePlayer(mediaSource);
                    videoContainer.setVisibility(View.VISIBLE);
                    casePlayIcon.setVisibility(View.VISIBLE);
                    mediaImage.setVisibility(View.GONE);
                  //  bvp.start();
                   // bvp.pause();
                }
            }
        }

        private void initializePlayer() {
            mPlayerView.requestFocus();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);

            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            player = ExoPlayerFactory.newSimpleInstance(mView.getContext(), trackSelector);

            mPlayerView.setPlayer(player);

            player.setPlayWhenReady(shouldAutoPlay);

          player.addListener(this);

        }

        private  void preparePlayer(MediaSource mediaSource)
        {
            boolean haveStartPosition = currentWindow != C.INDEX_UNSET;
            if (haveStartPosition) {
                player.seekTo(currentWindow, playbackPosition);
            }

            player.prepare(mediaSource, !haveStartPosition, false);
        }

        private MediaSource buildMediaSource(Uri uri) {
            return new ExtractorMediaSource.Factory(
                    new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                    createMediaSource(uri);
        }

        private MediaSource buildFileMediaSource(Uri uri) {
            return new ExtractorMediaSource.Factory(
                    new DefaultDataSourceFactory(mView.getContext(), Util.getUserAgent(mView.getContext(), "mediaPlayerSample"),(TransferListener<? super DataSource>) bandwidthMeter)).
                    createMediaSource(uri);
        }

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            Log.d("bufferupdate" , "onTimelineChanged "+reason);
            if(reason>0)
                bar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            Log.d("bufferupdate" , "loading changed "+isLoading);
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_BUFFERING){
                bar.setVisibility(View.VISIBLE);
                Log.d("bufferupdate" , "position  "+player.getBufferedPosition());
                Log.d("bufferupdate" , "percentage  "+player.getBufferedPercentage());
            } else {
                Log.d("bufferupdate" , "state  else "+playbackState);
                Log.d("bufferupdate" , "position  else "+player.getBufferedPosition());
                Log.d("bufferupdate" , "percentage  else "+player.getBufferedPercentage());
                bar.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {
            Log.d("bufferupdate", "onSeekProcessed");
        }
    }
}

