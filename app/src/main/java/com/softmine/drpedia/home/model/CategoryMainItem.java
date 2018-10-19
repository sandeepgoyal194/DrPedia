package com.softmine.drpedia.home.model;

import android.util.Log;

import com.softmine.drpedia.expendablerecylerview.MultiCheckExpandableGroup;

import java.util.List;

public class CategoryMainItem extends MultiCheckExpandableGroup {

    public CategoryMainItem(String categoryName, List<SubCategoryItem> subCategory, int categoryID) {
        super(categoryName, subCategory , categoryID);
        this.categoryID = categoryID;
        Log.d("categoryListItems" , "CategoryMainItem constructor called ============");
    }


    int categoryID;
}
