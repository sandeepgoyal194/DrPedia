package com.softmine.drpedia.home.model;

import android.util.Log;

import com.softmine.drpedia.expendablerecylerview.MultiCheckExpandableGroup;

import java.util.List;

public class CategoryMainItem extends MultiCheckExpandableGroup {

    public CategoryMainItem(String categoryName, List<SubCategoryItem> subCategory, int categoryID) {
        super(categoryName, subCategory);
        this.categoryID = categoryID;
        Log.d("categoryListItems" , "CategoryMainItem constructor called finalled============");
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<SubCategoryItem> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(List<SubCategoryItem> subCategory) {
        this.subCategory = subCategory;
    }

    int categoryID;
    String categoryName;
    List<SubCategoryItem> subCategory;

}
