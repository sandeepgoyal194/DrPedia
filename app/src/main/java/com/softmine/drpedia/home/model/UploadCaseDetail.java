package com.softmine.drpedia.home.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UploadCaseDetail implements Serializable {

    String subtype_id;
    String short_description;
    String long_description;

    @SerializedName("image")
    List<Integer> upload_item_id;

    public String getSubtype_id() {
        return subtype_id;
    }

    public void setSubtype_id(String subtype_id) {
        this.subtype_id = subtype_id;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getLong_description() {
        return long_description;
    }

    public void setLong_description(String long_description) {
        this.long_description = long_description;
    }

    public List<Integer> getUpload_item_id() {
        return upload_item_id;
    }

    public void setUpload_item_id(List<Integer> upload_item_id) {
        this.upload_item_id = upload_item_id;
    }
}
