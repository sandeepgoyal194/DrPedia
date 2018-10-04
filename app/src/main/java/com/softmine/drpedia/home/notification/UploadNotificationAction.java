package com.softmine.drpedia.home.notification;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

public class UploadNotificationAction implements Parcelable {

    private int icon;
    private CharSequence title;
    private PendingIntent actionIntent;

    public UploadNotificationAction(int icon, CharSequence title, PendingIntent intent) {
        this.icon = icon;
        this.title = title;
        this.actionIntent = intent;
    }

    final NotificationCompat.Action toAction() {
        return new NotificationCompat.Action.Builder(icon, title, actionIntent).build();
    }

    protected UploadNotificationAction(Parcel in) {
        this.icon = in.readInt();

        this.title = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);

        if (in.readInt() == 1) {
            actionIntent = PendingIntent.CREATOR.createFromParcel(in);
        }
    }

    public static final Creator<UploadNotificationAction> CREATOR = new Creator<UploadNotificationAction>() {
        @Override
        public UploadNotificationAction createFromParcel(Parcel in) {
            return new UploadNotificationAction(in);
        }

        @Override
        public UploadNotificationAction[] newArray(int size) {
            return new UploadNotificationAction[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.icon);

        TextUtils.writeToParcel(title, dest, flags);

        if (actionIntent != null) {
            dest.writeInt(1);
            actionIntent.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UploadNotificationAction)) return false;

        UploadNotificationAction that = (UploadNotificationAction) o;

        if (icon != that.icon) return false;
        if (!title.equals(that.title)) return false;
        return actionIntent.equals(that.actionIntent);

    }

    @Override
    public int hashCode() {
        int result = icon;
        result = 31 * result + title.hashCode();
        result = 31 * result + actionIntent.hashCode();
        return result;
    }
}
