package com.softmine.drpedia.home.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserDetailInterestType implements Serializable
{
    public List<CategoryMainItemResponse> getData() {
        return data;
    }

    public void setData(List<CategoryMainItemResponse> data) {
        this.data = data;
    }

    @SerializedName(value="intrests")
    List<CategoryMainItemResponse> data;

}
