package com.softmine.drpedia.home.model;

import java.io.Serializable;
import java.util.List;

public class CategoryMainItemResponse implements Serializable{

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
