package com.softmine.drpedia.home.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class SubCategoryItem implements  Parcelable {

    int subtype_id;

    protected SubCategoryItem(Parcel in) {
        subtype_id = in.readInt();
        subtype = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(subtype_id);
        dest.writeString(subtype);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubCategoryItem> CREATOR = new Creator<SubCategoryItem>() {
        @Override
        public SubCategoryItem createFromParcel(Parcel in) {
            return new SubCategoryItem(in);
        }

        @Override
        public SubCategoryItem[] newArray(int size) {
            return new SubCategoryItem[size];
        }
    };

    public int getSubtype_id() {
        return subtype_id;
    }

    public void setSubtype_id(int subtype_id) {
        this.subtype_id = subtype_id;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    String subtype;

}
