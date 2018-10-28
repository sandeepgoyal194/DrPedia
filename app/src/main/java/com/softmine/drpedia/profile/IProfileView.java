package com.softmine.drpedia.profile;

import com.softmine.drpedia.home.model.CategoryMainItemResponse;
import com.softmine.drpedia.login.model.User;

import java.util.List;

import frameworks.basemvp.IView;

public interface IProfileView extends IView{

    public void onProfileSyncedSuccessfully(User user);
    public void onProfileSyncFailed();
    public void setUserInterestSize(List<CategoryMainItemResponse> categoryMainItemResponses);

}
