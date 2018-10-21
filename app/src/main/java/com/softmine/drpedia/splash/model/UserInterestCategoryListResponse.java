package com.softmine.drpedia.splash.model;

import com.google.gson.annotations.SerializedName;
import com.softmine.drpedia.home.model.CategoryMainItemResponse;

import java.io.Serializable;
import java.util.List;

public class UserInterestCategoryListResponse implements Serializable {


    public List<CategoryMainItemResponse> getData() {
        return data;
    }

    public void setData(List<CategoryMainItemResponse> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName(value="Intrest")
    List<CategoryMainItemResponse> data;
    @SerializedName(value="message")
    String message;

}
