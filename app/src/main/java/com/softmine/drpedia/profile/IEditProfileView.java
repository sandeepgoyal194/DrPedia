package com.softmine.drpedia.profile;

public interface IEditProfileView {
    public void setProfileUpatedSuccessfully();
    public void showProgress();
    public void hideProgress();
    public void setProfileUpdateFailed();
    public void showSnackBar(String message);
    public void showToast(String message);
}
