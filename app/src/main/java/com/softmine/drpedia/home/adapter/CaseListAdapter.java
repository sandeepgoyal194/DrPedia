package com.softmine.drpedia.home.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.softmine.drpedia.R;
import com.softmine.drpedia.home.customview.CaseMediaItemHorizontalRecyclerView;
import com.softmine.drpedia.home.model.CaseItem;
import com.softmine.drpedia.home.model.CaseMediaItem;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import frameworks.imageloader.ImageLoader;

public class CaseListAdapter extends RecyclerView.Adapter<CaseListAdapter.CaseViewHolder>{

    private final LayoutInflater layoutInflater;

    private List<? extends CaseItem> caseCollection;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener <T extends CaseItem>{
        void onUserItemClicked(T userModel);
    }

    Context context;
    @Inject
    ImageLoader mImageLoader;

    @Inject
    CaseListAdapter(Context context)
    {
        this.context = context;
        this.layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.caseCollection = Collections.emptyList();
    }

    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public CaseListAdapter.CaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d("adapterlogs" , "onCreateViewHolder");

        final View view = this.layoutInflater.inflate(R.layout.case_item, parent, false);
        return new CaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CaseListAdapter.CaseViewHolder holder, int position) {

        Log.d("adapterlogs" , "onBindViewHolder  "+position);

        final CaseItem caseItem = this.caseCollection.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (CaseListAdapter.this.onItemClickListener != null) {
                    CaseListAdapter.this.onItemClickListener.onUserItemClicked(caseItem);
                }
            }
        });
        holder.setView(caseItem);

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    context.startActivity(new Intent(context, CaseDiscussionDetail.class));
            }
        });*/

    }

    @Override public long getItemId(int position) {
        return position;
    }

    public void setUsersCollection(Collection<? extends CaseItem> usersCollection) {
        Log.d("loginresponse","usersCollection size"+usersCollection.size());
        this.validateUsersCollection(usersCollection);
        this.caseCollection = (List<CaseItem>) usersCollection;
        Log.d("loginresponse","caseCollection size"+caseCollection.size());
        this.notifyDataSetChanged();
    }

    private void validateUsersCollection(Collection<? extends CaseItem> usersCollection) {
        if (usersCollection == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }
    }
    @Override
    public int getItemCount() {
        return (this.caseCollection != null) ? this.caseCollection.size() : 0;
        // return 20;
    }

    static class CaseViewHolder extends RecyclerView.ViewHolder {

       /* @BindView(R.id.casePic)
        ImageView pic;*/

        @BindView(R.id.horizontal_recycler_view)
        CaseMediaItemHorizontalRecyclerView rc_view;

        @BindView(R.id.desc)
        TextView desc;
        @BindView(R.id.like_count)
        TextView likecount;
        @BindView(R.id.bookmark_count)
        TextView bookmarkCount;
        @BindView(R.id.comments_count)
        TextView totalComment;

        @BindView(R.id.img_like)
        ImageView img_like;
        @BindView(R.id.img_bookmart)
        ImageView img_bookmark;

        View mView;

        MediaItemListAdapter mediaItemListAdapter ;


        CaseViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
            mediaItemListAdapter =  new MediaItemListAdapter(mView.getContext());
            rc_view.setAdapter(mediaItemListAdapter);
        }

        public void setView(CaseItem caseItem)
        {
            Log.d("adapterresponse","desc=="+caseItem.getLong_desc());
            Log.d("adapterresponse","like=="+caseItem.getTotal_like());
            Log.d("adapterresponse","bookmark=="+caseItem.getTotal_bookmark());
            Log.d("adapterresponse","comment=="+caseItem.getTotal_comment());

            for (CaseMediaItem item: caseItem.getPostPicUrl())
            {
                Log.d("adapterresponse","image url===="+item.getImage());
                Log.d("adapterresponse","video url===="+item.getVideo());
                Log.d("adapterresponse","thumbnail url===="+item.getThumbnail());
            }

            Log.d("adapterresponse","likes=="+caseItem.getPostLikeStatus());
            Log.d("adapterresponse","bookmark=="+caseItem.getPostBookmarkStatus());
            Log.d("adapterresponse","date=="+caseItem.getCreated_on());

            //Log.d("loginresponse","loader called ======== "+mImageLoader.printMsg());

            desc.setText(caseItem.getLong_desc());
            likecount.setText(caseItem.getTotal_like());
            bookmarkCount.setText(caseItem.getTotal_bookmark());
            totalComment.setText(caseItem.getTotal_comment());
           /* if(mImageLoader!=null)
            {
                if(caseItem.getPostPicUrl().get(0).isVideoAvailable())
                {
                    *//*Bitmap videoThumb = null;
                    try {
                        videoThumb = retriveVideoFrameFromVideo(CaseStudyAPIURL.BASE_URL_image_load+caseItem.getPostPicUrl().get(0).getVideo());
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    Log.d("adapterresponse","pic url not null==="+ CaseStudyAPIURL.BASE_URL_image_load+caseItem.getPostPicUrl().get(0).getVideo());
                    //mImageLoader.loadImage(videoThumb, pic);

                    if(videoThumb!=null)
                    {
                        Glide.with(context)
                                .load(videoThumb)
                                .asBitmap()
                                .into(pic);
                    }
                    else
                        Log.d("adapterresponse","videoThumb null===");*//*

                }
                else if(caseItem.getPostPicUrl().get(0).isImageAvailable())
                {
                    Log.d("adapterresponse","pic url not null==="+ CaseStudyAPIURL.BASE_URL_image_load+caseItem.getPostPicUrl().get(0).getImage());
                    //mImageLoader.loadImage(CaseStudyAPIURL.BASE_URL_image_load+caseItem.getPostPicUrl().get(0).getImage(), pic);
                }
            }
            else
                Log.d("adapterresponse","pic url=="+caseItem.getPostPicUrl().get(0).getImage());
*/
            if(caseItem.getPostLikeStatus().equals("true"))
                img_like.setImageDrawable(mView.getContext().getResources().getDrawable(R.drawable.like_checked));
            else
                img_like.setImageDrawable(mView.getContext().getResources().getDrawable(R.drawable.like));

            if(caseItem.getPostBookmarkStatus().equals("true"))
                img_bookmark.setImageDrawable(mView.getContext().getResources().getDrawable(R.drawable.checked_bookmark));
            else
                img_bookmark.setImageDrawable(mView.getContext().getResources().getDrawable(R.drawable.bookmark));

            rc_view.onListLoaded();
            mediaItemListAdapter.setMediaItemList(caseItem.getPostPicUrl());
        }


        public Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable
        {
            Log.d("adapterresponse","inside retriveVideoFrameFromVideo");
            Bitmap bitmap = null;
            MediaMetadataRetriever mediaMetadataRetriever = null;
            try
            {
                mediaMetadataRetriever = new MediaMetadataRetriever();
                Log.d("adapterresponse","inside retriveVideoFrameFromVideo object");
                if (Build.VERSION.SDK_INT >= 14) {
                    Log.d("adapterresponse","data source created");
                    mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
                    Log.d("adapterresponse","data source created1");
                }
                else {
                    Log.d("adapterresponse","data source created2");
                    mediaMetadataRetriever.setDataSource(videoPath);
                    Log.d("adapterresponse","data source created3");
                }
                //   mediaMetadataRetriever.setDataSource(videoPath);

                bitmap = mediaMetadataRetriever.getFrameAtTime(2000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                Log.d("adapterresponse","bitmap created");
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
