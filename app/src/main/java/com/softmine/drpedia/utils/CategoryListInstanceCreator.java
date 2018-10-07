package com.softmine.drpedia.utils;

import android.util.Log;

import com.google.gson.InstanceCreator;
import com.softmine.drpedia.home.model.CategoryMainItem;
import com.softmine.drpedia.home.model.SubCategoryItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CategoryListInstanceCreator implements InstanceCreator<CategoryMainItem> {

    String name="Hello";
    List<SubCategoryItem> subCategory=new ArrayList<>();
    int categoryID;

    @Override
    public CategoryMainItem createInstance(Type type) {

        CategoryMainItem item = new CategoryMainItem(name , subCategory,categoryID);
        Log.d("categoryListItems" , "=================CategoryListInstanceCreator constructor called");
        return item;
    }

}
