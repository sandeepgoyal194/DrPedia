package com.softmine.drpedia.home.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UploadItemResponse implements Serializable {

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    @SerializedName(value="message")
    String message;

    @SerializedName(value="image_id")
    int image_id;


}
