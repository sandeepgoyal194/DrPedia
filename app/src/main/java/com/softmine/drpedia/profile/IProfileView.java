package com.softmine.drpedia.profile;

import com.softmine.drpedia.login.model.User;

public interface IProfileView {

    public void showProgress();
    public void hideProgress();
    public void onProfileSyncedSuccessfully(User user);
    public void onProfileSyncFailed();

}
