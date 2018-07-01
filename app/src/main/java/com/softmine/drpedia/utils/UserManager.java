package com.softmine.drpedia.utils;

import android.content.Context;

import com.softmine.drpedia.login.model.User;

import frameworks.dbhandler.PrefManager;
import frameworks.utils.GsonFactory;

public class UserManager {
    private static final String KEY_SESSION_ID = "USER";
    Context mContext;

    public UserManager(Context mContext) {
        this.mContext = mContext;
    }

    public void saveUser(User user) {
        PrefManager mPrefManager;
        mPrefManager = new PrefManager(mContext);
        mPrefManager.putString(KEY_SESSION_ID, GsonFactory.getGson().toJson(user));

    }

    public User getUser() {
        PrefManager mPrefManager;
        mPrefManager = new PrefManager(mContext);
        return GsonFactory.getGson().fromJson(mPrefManager.getString(KEY_SESSION_ID), User.class);
    }
    public void clearUser() {
        PrefManager mPrefManager;
        mPrefManager = new PrefManager(mContext);
        mPrefManager.putString(KEY_SESSION_ID, null);
    }

}