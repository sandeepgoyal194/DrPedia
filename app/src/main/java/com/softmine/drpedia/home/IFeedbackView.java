package com.softmine.drpedia.home;

public interface IFeedbackView {

     String setResult(String result);
     public void showProgress();
     public void hideProgress();
     public void setProfileUpdateFailed();
     public void showSnackBar(String message);
     public void showToast(String message);
     void setEmptyView();
}
