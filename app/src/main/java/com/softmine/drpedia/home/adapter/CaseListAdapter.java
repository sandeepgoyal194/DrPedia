package com.softmine.drpedia.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sachin.doctorsguide.R;
import com.softmine.drpedia.home.model.CaseItem;
import com.softmine.drpedia.home.net.CaseStudyAPIURL;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import frameworks.imageloader.ImageLoader;

public class CaseListAdapter extends RecyclerView.Adapter<CaseListAdapter.CaseViewHolder>{

    private final LayoutInflater layoutInflater;

    private List<CaseItem> caseCollection;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onUserItemClicked(CaseItem userModel);
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
        final View view = this.layoutInflater.inflate(R.layout.case_item, parent, false);
        return new CaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CaseListAdapter.CaseViewHolder holder, int position) {
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

    public void setUsersCollection(Collection<CaseItem> usersCollection) {
        Log.d("loginresponse","usersCollection size"+usersCollection.size());
        this.validateUsersCollection(usersCollection);
        this.caseCollection = (List<CaseItem>) usersCollection;
        Log.d("loginresponse","caseCollection size"+caseCollection.size());
        this.notifyDataSetChanged();
    }

    private void validateUsersCollection(Collection<CaseItem> usersCollection) {
        if (usersCollection == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }
    }
    @Override
    public int getItemCount() {
        return (this.caseCollection != null) ? this.caseCollection.size() : 0;
       // return 20;
    }

    class CaseViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.casePic)
        ImageView pic;

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


        CaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setView(CaseItem caseItem)
        {
            Log.d("loginresponse","desc=="+caseItem.getLong_desc());
            Log.d("loginresponse","like=="+caseItem.getTotal_like());
            Log.d("loginresponse","bookmark=="+caseItem.getTotal_bookmark());
            Log.d("loginresponse","comment=="+caseItem.getTotal_comment());
            Log.d("loginresponse","pic url=="+caseItem.getPostPicUrl());
            Log.d("loginresponse","likes=="+caseItem.getPostPicUrl());
            Log.d("loginresponse","bookmark=="+caseItem.getPostPicUrl());

            //Log.d("loginresponse","loader called ======== "+mImageLoader.printMsg());

            desc.setText(caseItem.getLong_desc());
            likecount.setText(caseItem.getTotal_like());
            bookmarkCount.setText(caseItem.getTotal_bookmark());
            totalComment.setText(caseItem.getTotal_comment());
            if(mImageLoader!=null) {
                Log.d("loginresponse","pic url not null==="+CaseStudyAPIURL.BASE_URL_image_load+caseItem.getPostPicUrl());
                mImageLoader.loadImage(CaseStudyAPIURL.BASE_URL_image_load+caseItem.getPostPicUrl(), pic);

            }
            else
                Log.d("loginresponse","pic url=="+caseItem.getPostPicUrl());

            if(caseItem.getPostLikeStatus().equals("true"))
            img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.like_checked));
        else
            img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.like));

            if(caseItem.getPostBookmarkStatus().equals("true"))
                img_bookmark.setImageDrawable(context.getResources().getDrawable(R.drawable.checked_bookmark));
            else
                img_bookmark.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmark));


        }

    }
}
