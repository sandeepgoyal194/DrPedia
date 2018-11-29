package com.softmine.drpedia.home;

import com.softmine.drpedia.home.model.CategoryMainItemResponse;

import java.util.List;

import frameworks.basemvp.IView;

public interface CategoryListView extends IView {

    public void updateCategoryList(List<CategoryMainItemResponse> categoryMainItemList);
    public void startActivity();
    public void startProfileActivity();
}
