package com.softmine.drpedia.home.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentsListResponse {

    public List<CommentData> getData() {
        return data;
    }

    public void setData(List<CommentData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName(value="Comments")
    List<CommentData> data;
    @SerializedName(value="message")
    String message;
}
