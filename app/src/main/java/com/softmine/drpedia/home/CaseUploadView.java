package com.softmine.drpedia.home;

public interface CaseUploadView {

    void selectImageFromStorage();

    void setUploadResult(String result);

    String getCaseDesc();

    String getCaseType();

    byte[] getImageBytes();

    String getImageType();
}
