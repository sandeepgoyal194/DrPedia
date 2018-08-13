package com.softmine.drpedia.home.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CaseItem implements Serializable{


    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public String getLong_desc() {
        return long_desc;
    }

    public void setLong_desc(String long_desc) {
        this.long_desc = long_desc;
    }

    public String getTotal_comment() {
        return total_comment;
    }

    public void setTotal_comment(String total_comment) {
        this.total_comment = total_comment;
    }

    public String getTotal_like() {
        return total_like;
    }

    public void setTotal_like(String total_like) {
        this.total_like = total_like;
    }

    public String getTotal_bookmark() {
        return total_bookmark;
    }

    public void setTotal_bookmark(String total_bookmark) {
        this.total_bookmark = total_bookmark;
    }



    public String getPostLikeStatus() {
        return postLikeStatus;
    }

    public void setPostLikeStatus(String postLikeStatus) {
        this.postLikeStatus = postLikeStatus;
    }

    public String getPostBookmarkStatus() {
        return postBookmarkStatus;
    }

    public void setPostBookmarkStatus(String postBookmarkStatus) {
        this.postBookmarkStatus = postBookmarkStatus;
    }

    private int post_id;
    private String post_type;
    @SerializedName(value="short_description")
    private String short_desc;
    @SerializedName(value="long_description")
    private String long_desc;
    private String total_comment;
    private String total_like;
    private String total_bookmark;

    public List<CaseMediaItem> getPostPicUrl() {
        return postPicUrl;
    }

    public void setPostPicUrl(List<CaseMediaItem> postPicUrl) {
        this.postPicUrl = postPicUrl;
    }

    @SerializedName(value="image_path")
    private List<CaseMediaItem> postPicUrl;
    @SerializedName(value="likes")
    private String postLikeStatus;
    @SerializedName(value="bookmark")
    private String postBookmarkStatus;

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    private String created_on;



}