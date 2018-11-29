package com.softmine.drpedia.home.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.softmine.drpedia.expendablerecylerview.Genre;
import com.softmine.drpedia.expendablerecylerview.MultiCheckExpandableGroup;

import java.util.List;

public class CategoryMainItem extends MultiCheckExpandableGroup implements Parcelable{

    public CategoryMainItem(String categoryName, List<SubCategoryItem> subCategory, int categoryID) {
        super(categoryName, subCategory , categoryID);
        this.categoryID = categoryID;
        Log.d("categoryListItems" , "CategoryMainItem constructor called ============");
    }

    protected CategoryMainItem(Parcel in) {
        super(in);
    }

    int categoryID;

    public int getIconResId() {
        return categoryID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryMainItem)) return false;

        CategoryMainItem genre = (CategoryMainItem) o;

        return getIconResId() == genre.getIconResId();

    }

    @Override
    public int hashCode() {
        return getIconResId();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(categoryID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CategoryMainItem> CREATOR =
            new Parcelable.Creator<CategoryMainItem>() {
                @Override
                public CategoryMainItem createFromParcel(Parcel in) {
                    return new CategoryMainItem(in);
                }

                @Override
                public CategoryMainItem[] newArray(int size) {
                    return new CategoryMainItem[size];
                }
            };
}
