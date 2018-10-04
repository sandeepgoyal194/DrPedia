package com.softmine.drpedia.home.notification;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class UploadNotificationConfig implements Parcelable {

    private boolean ringToneEnabled;
    private String notificationChannelId;
    private UploadNotificationStatusConfig progress;
    private UploadNotificationStatusConfig completed;
    private UploadNotificationStatusConfig error;
    private UploadNotificationStatusConfig cancelled;

    public UploadNotificationConfig() {

        // common configuration for all the notification statuses
        ringToneEnabled = true;

        // progress notification configuration
        progress = new UploadNotificationStatusConfig();
        progress.message = "File uploading";

        // completed notification configuration
        completed = new UploadNotificationStatusConfig();
        completed.message = "Upload completed successfully";

        // error notification configuration
        error = new UploadNotificationStatusConfig();
        error.message = "Error during upload";

        // cancelled notification configuration
        cancelled = new UploadNotificationStatusConfig();
        cancelled.message = "Upload cancelled";
    }

    public final UploadNotificationConfig setTitleForAllStatuses(String title) {
        progress.title = title;
        completed.title = title;
        error.title = title;
        cancelled.title = title;
        return this;
    }

    public final UploadNotificationConfig setRingToneEnabled(Boolean enabled) {
        this.ringToneEnabled = enabled;
        return this;
    }

    public final UploadNotificationConfig setNotificationChannelId(@NonNull String channelId){
        this.notificationChannelId = channelId;
        return this;
    }

    public boolean isRingToneEnabled() {
        return ringToneEnabled;
    }

    public UploadNotificationStatusConfig getProgress() {
        return progress;
    }

    public UploadNotificationStatusConfig getCompleted() {
        return completed;
    }

    public UploadNotificationStatusConfig getError() {
        return error;
    }

    public UploadNotificationStatusConfig getCancelled() {
        return cancelled;
    }

    public String getNotificationChannelId(){
        return notificationChannelId;
    }

    protected UploadNotificationConfig(Parcel in) {

        this.notificationChannelId = in.readString();
        this.ringToneEnabled = in.readByte() != 0;
        this.progress = in.readParcelable(UploadNotificationStatusConfig.class.getClassLoader());
        this.completed = in.readParcelable(UploadNotificationStatusConfig.class.getClassLoader());
        this.error = in.readParcelable(UploadNotificationStatusConfig.class.getClassLoader());
        this.cancelled = in.readParcelable(UploadNotificationStatusConfig.class.getClassLoader());

    }

    public static final Creator<UploadNotificationConfig> CREATOR = new Creator<UploadNotificationConfig>() {
        @Override
        public UploadNotificationConfig createFromParcel(Parcel in) {
            return new UploadNotificationConfig(in);
        }

        @Override
        public UploadNotificationConfig[] newArray(int size) {
            return new UploadNotificationConfig[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.notificationChannelId);
        dest.writeByte(this.ringToneEnabled ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.progress, flags);
        dest.writeParcelable(this.completed, flags);
        dest.writeParcelable(this.error, flags);
        dest.writeParcelable(this.cancelled, flags);
    }
}
