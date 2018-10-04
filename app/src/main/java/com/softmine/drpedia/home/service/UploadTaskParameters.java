package com.softmine.drpedia.home.service;

import android.os.Parcel;
import android.os.Parcelable;

import com.softmine.drpedia.home.notification.UploadNotificationConfig;

import java.util.ArrayList;

public class UploadTaskParameters implements Parcelable {

    public String caseTitle;
    public String caseDesc;
    public String caseCategory;
    public ArrayList<String> attachmentList = new ArrayList<>();;
    public UploadNotificationConfig notificationConfig;

    public UploadTaskParameters() {
    }

    protected UploadTaskParameters(Parcel in) {
        caseTitle = in.readString();
        caseDesc = in.readString();
        caseCategory = in.readString();
        notificationConfig = in.readParcelable(UploadNotificationConfig.class.getClassLoader());
        in.readList(attachmentList, String.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(caseTitle);
        parcel.writeString(caseDesc);
        parcel.writeString(caseCategory);
        parcel.writeParcelable(notificationConfig, 0);
        parcel.writeList(attachmentList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UploadTaskParameters> CREATOR = new Creator<UploadTaskParameters>() {
        @Override
        public UploadTaskParameters createFromParcel(Parcel in) {
            return new UploadTaskParameters(in);
        }

        @Override
        public UploadTaskParameters[] newArray(int size) {
            return new UploadTaskParameters[size];
        }
    };
}
