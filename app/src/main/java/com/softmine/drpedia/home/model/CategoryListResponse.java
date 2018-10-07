package com.softmine.drpedia.home.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CategoryListResponse implements Serializable {

    public List<CategoryMainItem> getData() {
        return data;
    }

    public void setData(List<CategoryMainItem> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName(value="Posttype")
    List<CategoryMainItem> data;
    @SerializedName(value="message")
    String message;
}
