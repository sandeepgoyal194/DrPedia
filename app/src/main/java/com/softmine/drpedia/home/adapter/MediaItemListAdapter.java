package com.softmine.drpedia.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
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
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.case_mediaitem, null);
        return new CategoryHolder(v);
    }

    private class ProgressBack extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... arg0) {
            Log.d("downloadVideo","downloading");
            downloadFile("http://122.160.30.50:8080//DrPedia_Images/image_1532685708960_20180723_162209.mp4","Sample1.mp4");

            return true;
        }
        protected void onPostExecute(Boolean result) {
            Log.d("downloadVideo","download complete");
        }

    }

    private void downloadFile(String fileURL, String fileName) {
        Log.d("downloadVideo","download method called");
        try {
            String rootDir = Environment.getExternalStorageDirectory()
                    + File.separator + "Video";
            File rootFile = new File(rootDir);
            rootFile.mkdir();
            URL url = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(new File(rootFile,
                    fileName));
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            int count=0;
            while ((len1 = in.read(buffer)) > 0 && count<10) {
                Log.d("downloadVideo","inside if");
                f.write(buffer, 0, len1);
                count++;
            }
            f.close();
        } catch (IOException e) {
            Log.d("downloadVideo", e.toString());
        }
    }


    @Override
    public void onBindViewHolder(MediaItemListAdapter.CategoryHolder holder, int position) {
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


    class CategoryHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.case_image)
         ImageView mediaImage;

        @BindView(R.id.img_loading)
        ImageView img_loading;

        CaseMediaItem mMediaItem;

        @BindView(R.id.thumbview)
        ImageView thumbview;

        @BindView(R.id.case_playIcon)
        ImageView casePlayIcon;

        @BindView(R.id.case_video)
         VideoView mVideoView;

        @BindView(R.id.videoContainer)
        RelativeLayout videoContainer;

        MediaController mediaController;
        Glide glide;
        View mView;

        public CategoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            mView = itemView;
            mediaController= new MediaController(itemView.getContext());
            mediaController.setAnchorView(mVideoView);
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
                    thumbview.setVisibility(View.GONE);
                    casePlayIcon.setVisibility(View.GONE);
                }
                else
                {
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
                    thumbview.setVisibility(View.GONE);
                    casePlayIcon.setVisibility(View.GONE);
                }

                //scaleImage(mediaImage);

            }
            else if (caseMediaItem.isVideoAvailable())
            {
                Log.d("hrview","video available");
                Log.d("hrview","video src===="+caseMediaItem.getSrc());
            /*    mediaImage.setVisibility(View.GONE);
                img_loading.setVisibility(View.GONE);
                mVideoView.setVisibility(View.VISIBLE);
                videoContainer.setVisibility(View.VISIBLE);
                mVideoView.setMediaController(mediaController);
                mVideoView.setVideoURI(Uri.parse(CaseStudyAPIURL.BASE_URL_image_load+caseMediaItem.getVideo()));*/
              //  mVideoView.requestFocus();
             //   mVideoView.seekTo(1000);
                //   videoView.start();

                if(caseMediaItem.getSrc()!=null)
                {
                    glide.with(mView.getContext())
                            .load(caseMediaItem.getVideo())
                            .centerCrop()
                            .crossFade()
                            .into(thumbview);

                   /* Bitmap bmThumbnail = createThumbnailFromPath(caseMediaItem.getVideo(), MediaStore.Video.Thumbnails.MINI_KIND);
                    thumbview.setImageBitmap(bmThumbnail);*/
                    thumbview.setVisibility(View.VISIBLE);
                    casePlayIcon.setVisibility(View.VISIBLE);
                    mediaImage.setVisibility(View.GONE);
                }
                else
                {
                    ThumbnailDownloader.download(Uri.parse(CaseStudyAPIURL.BASE_URL_image_load+caseMediaItem.getVideo()).toString(),thumbview , this.getAdapterPosition());
                    thumbview.setVisibility(View.VISIBLE);
                    casePlayIcon.setVisibility(View.VISIBLE);
                    mediaImage.setVisibility(View.GONE);
                }
             }
        }

        public Bitmap createThumbnailFromPath(String filePath, int type){
            return ThumbnailUtils.createVideoThumbnail(filePath, type);
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

        }



    }

