package com.softmine.drpedia.home.model;

import com.google.gson.annotations.SerializedName;

public class CommentData {

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getTotal_like() {
        return total_like;
    }

    public void setTotal_like(int total_like) {
        this.total_like = total_like;
    }

    public int getTotal_reply() {
        return total_reply;
    }

    public void setTotal_reply(int total_reply) {
        this.total_reply = total_reply;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @SerializedName(value="comment_id")
    int comment_id;
    @SerializedName(value="comment")
    String comment;
    @SerializedName(value="total_like")
    int total_like;
    @SerializedName(value="total_reply")
    int total_reply;
    @SerializedName(value="name")
    String name;
    @SerializedName(value="userid")
    String userid;
    @SerializedName(value="emailID")
    String email_id;

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    @SerializedName(value="photourl")
    String photourl;

}
