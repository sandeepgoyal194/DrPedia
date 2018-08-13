package com.softmine.drpedia.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softmine.drpedia.R;
import com.softmine.drpedia.home.glide.CircleTransform;
import com.softmine.drpedia.home.model.CommentData;


import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import frameworks.appsession.AppSessionManager;
import frameworks.appsession.UserInfo;

public class CommentsOnPostListAdapter extends RecyclerView.Adapter<CommentsOnPostListAdapter.CommentsOnPostViewHolder> {

    private List<CommentData> commentsCollection;
    Context context;
    LayoutInflater layoutInflater;

    @Inject
    AppSessionManager appSessionManager;

    @Inject
    CommentsOnPostListAdapter(Context context, AppSessionManager appSessionManager)
    {
        this.context = context;
        this.layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.commentsCollection = Collections.emptyList();
        this.appSessionManager = appSessionManager;
    }


    @Override
    public CommentsOnPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = this.layoutInflater.inflate(R.layout.comment_item, parent, false);
        return new CommentsOnPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentsOnPostViewHolder holder, int position) {

        final CommentData commentData = this.commentsCollection.get(position);
        holder.setView(commentData);
    }

    public void setAllCommentOnPostCollection(Collection<CommentData> usersCollection) {
        Log.d("imagelogs","usersCollection size"+usersCollection.size());
        this.validateUsersCollection(usersCollection);
        this.commentsCollection = (List<CommentData>) usersCollection;
        Log.d("imagelogs","caseCollection size"+commentsCollection.size());
        this.notifyDataSetChanged();
    }


    private void validateUsersCollection(Collection<CommentData> usersCollection) {
        if (usersCollection == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }
    }

    @Override
    public int getItemCount() {
       return this.commentsCollection.size();
    }

     class CommentsOnPostViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.commented_user_photo)
        ImageView commented_user_photo;
        @BindView(R.id.userName)
        TextView username;
        @BindView(R.id.commentText)
        TextView commentText;
        @BindView(R.id.total_like)
        TextView total_like;
        @BindView(R.id.total_reply)
        TextView total_reply;
        View itemView;

        CommentsOnPostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.itemView = itemView;
        }

        public void setView(CommentData commentData)
        {
            Glide.with(itemView.getContext())
                    .load(commentData.getPhotourl()) // add your image url
                    .transform(new CircleTransform(itemView.getContext())) // applying the image transformer
                    .into(commented_user_photo);


            Log.d("loginresponse",commentData.getComment());
            Log.d("loginresponse",""+commentData.getTotal_like());
            Log.d("loginresponse",""+commentData.getTotal_reply());
            Log.d("loginresponse",commentData.getName());
            Log.d("loginresponse",""+commentData.getEmail_id());
            Log.d("loginresponse",""+commentData.getUserid());
            Log.d("loginresponse",""+commentData.getPhotourl());


            username.setText(commentData.getName());
            commentText.setText(commentData.getComment());
            total_like.setText(Integer.toString(commentData.getTotal_like()));
            total_reply.setText(Integer.toString(commentData.getTotal_reply()));
        }
    }

}
