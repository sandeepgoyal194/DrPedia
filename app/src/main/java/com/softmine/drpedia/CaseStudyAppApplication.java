package com.softmine.drpedia;

import android.content.Intent;
import android.util.Log;

import com.softmine.drpedia.login.model.User;
import com.softmine.drpedia.login.view.SocialLoginActivity;

import frameworks.AppBaseApplication;
import frameworks.appsession.AppSessionManager;
import frameworks.appsession.SessionValue;

public class CaseStudyAppApplication extends AppBaseApplication {

    public static SessionValue sessionValue = null;
    public static User mUserInfo;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static CaseStudyAppApplication getParentApplication()
    {
        return (CaseStudyAppApplication)getApplication();
    }

    public  void setAuthId(SessionValue sessionValue) {
        Log.d("loginresponse","set AuthID called");
        this.sessionValue = sessionValue;
    }

    public static SessionValue getAuthId()
    {
        if(sessionValue==null)
        {
            sessionValue = new AppSessionManager(getApplication().getApplicationContext()).getSession();
        }

        if(sessionValue==null)
        {
            Intent i = new Intent(getApplication().getBaseContext(), SocialLoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().getBaseContext().startActivity(i);
            return null;
        }
        return sessionValue;
    }

    public void setUser(User userInfo)
    {
        mUserInfo = userInfo;
    }

    public static User getUser() {
        return mUserInfo;
    }
}
