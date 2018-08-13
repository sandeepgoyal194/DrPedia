package com.softmine.drpedia.home.model;

import com.google.gson.annotations.SerializedName;
import com.softmine.drpedia.home.model.CaseItem;

import java.io.Serializable;
import java.util.List;

public class CaseListResponse implements Serializable {

    public List<CaseItem> getData() {
        return data;
    }

    public void setData(List<CaseItem> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName(value="Posts")
    List<CaseItem> data;
    @SerializedName(value="message")
    String message;

}
