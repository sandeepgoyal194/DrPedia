package com.softmine.drpedia.home;

import frameworks.basemvp.IView;

public interface IFeedbackView extends IView {

     String setResult(String result);
     public void showProgress();
     public void hideProgress();
     public void setProfileUpdateFailed();
     public void showSnackBar(String message);
     public void showToast(String message);
     void setEmptyView();
}
