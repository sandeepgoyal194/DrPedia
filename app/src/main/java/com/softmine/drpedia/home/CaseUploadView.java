package com.softmine.drpedia.home;

import android.support.annotation.StringRes;

import com.softmine.drpedia.home.notification.UploadNotificationConfig;

import java.util.List;

import frameworks.basemvp.IView;

public interface CaseUploadView extends IView{

    void selectImageFromStorage();

    void selectVideoFromStorage();

    void setUploadResult(String result);

    String getCaseDesc();

    String getCaseTitle();

    UploadNotificationConfig getNotificationConfig(@StringRes int title);

    List<String> getDataUri();

    String getInterestType();

    void onCaseTypeEmpty();

    void onCaseDescEmpty();

    void onUriListEmpty();

}
