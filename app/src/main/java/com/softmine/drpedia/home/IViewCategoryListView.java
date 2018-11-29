package com.softmine.drpedia.home;

import com.softmine.drpedia.home.model.CategoryMainItemResponse;

import java.util.List;

import frameworks.basemvp.IView;

public interface IViewCategoryListView extends IView {

    public void setUserInterestSize(List<CategoryMainItemResponse> categoryMainItemResponses);

}
