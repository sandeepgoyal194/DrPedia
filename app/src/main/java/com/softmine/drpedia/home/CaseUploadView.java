package com.softmine.drpedia.home;

import java.util.List;

import frameworks.basemvp.IView;

public interface CaseUploadView extends IView{

    void selectImageFromStorage();

    void selectVideoFromStorage();

    void setUploadResult(String result);

    String getCaseDesc();

    String getCaseType();

    List<String> getDataUri();

    String getImageType();

    void onCaseTypeEmpty();

    void onCaseDescEmpty();

    void onUriListEmpty();

}
