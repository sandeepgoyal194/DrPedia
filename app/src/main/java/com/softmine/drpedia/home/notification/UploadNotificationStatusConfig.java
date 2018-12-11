package com.softmine.drpedia.home.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;

import com.softmine.drpedia.home.activity.DashBoardActivity;
import com.softmine.drpedia.home.activity.UploadCaseActivity;
import com.softmine.drpedia.home.service.UploadService;
import com.softmine.drpedia.home.service.UploadTaskParameters;

import java.util.ArrayList;

public class UploadNotificationStatusConfig implements Parcelable {

    public String title = "File Upload";
    public String message;
    public boolean autoClear = false;
    public int iconResourceID = android.R.drawable.ic_menu_upload;
    public Bitmap largeIcon = null;

    public int iconColorResourceID = NotificationCompat.COLOR_DEFAULT;
    public PendingIntent clickIntent = null;

    Intent intent = new Intent();

    final public PendingIntent getUploadCompleteClickIntent(Context context, UploadTaskParameters params) {
        if (clickIntent == null) {
            Intent intent = new Intent(context , DashBoardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        return clickIntent;
    }


    final public PendingIntent getUploadErrorClickIntent(Context context, UploadTaskParameters params) {
        if (clickIntent == null) {
            Intent intent = new Intent(context , UploadCaseActivity.class);
            intent.putExtra(UploadService.PARAM_TASK_PARAMETERS, params);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        return clickIntent;
    }

    public boolean clearOnAction = false;
    public ArrayList<UploadNotificationAction> actions = new ArrayList<>(3);

    final void addActionsToNotificationBuilder(NotificationCompat.Builder builder) {
        if (actions != null && !actions.isEmpty()) {
            for (UploadNotificationAction notificationAction : actions) {
                builder.addAction(notificationAction.toAction());
            }
        }
    }

    public UploadNotificationStatusConfig(){
    }

    protected UploadNotificationStatusConfig(Parcel in) {
        this.title = in.readString();
        this.message = in.readString();
        this.autoClear = in.readByte() != 0;
        this.clearOnAction = in.readByte() != 0;
        this.largeIcon = in.readParcelable(Bitmap.class.getClassLoader());
        this.iconResourceID = in.readInt();
        this.iconColorResourceID = in.readInt();
        this.clickIntent = in.readParcelable(PendingIntent.class.getClassLoader());
        this.actions = in.createTypedArrayList(UploadNotificationAction.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.message);
        dest.writeByte(this.autoClear ? (byte) 1 : (byte) 0);
        dest.writeByte(this.clearOnAction ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.largeIcon, flags);
        dest.writeInt(this.iconResourceID);
        dest.writeInt(this.iconColorResourceID);
        dest.writeParcelable(this.clickIntent, flags);
        dest.writeTypedList(this.actions);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UploadNotificationStatusConfig> CREATOR = new Creator<UploadNotificationStatusConfig>() {
        @Override
        public UploadNotificationStatusConfig createFromParcel(Parcel in) {
            return new UploadNotificationStatusConfig(in);
        }

        @Override
        public UploadNotificationStatusConfig[] newArray(int size) {
            return new UploadNotificationStatusConfig[size];
        }
    };
}
