package com.softmine.drpedia.splash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.softmine.drpedia.CaseStudyAppApplication;
import com.softmine.drpedia.R;
import com.softmine.drpedia.home.activity.DashBoardActivity;
import com.softmine.drpedia.login.model.User;
import com.softmine.drpedia.login.view.SocialLoginActivity;
import com.softmine.drpedia.splash.di.DaggerSplashComponent;
import com.softmine.drpedia.splash.di.SplashComponent;
import com.softmine.drpedia.utils.UserManager;

import javax.inject.Inject;

import frameworks.AppBaseApplication;
import frameworks.appsession.AppSessionManager;
import frameworks.appsession.SessionValue;

public class DoctorSGuideSplashScreen extends AppCompatActivity {

    private static final int START_DASHBOARD = 1;
    private static final int START_LOGIN = 2;
    public static int SPLASH_TIME_OUT = 3000;


    @Inject
    AppSessionManager appSessionManager;

    UserManager userManager;
    SplashComponent splashComponent;
    User userInfo;

    H mHandler = new H();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_guide_splash);
        this.initInjector();

    //    appSessionManager = new AppSessionManager(this);

       // SessionValue sessionValue = appSessionManager.getSession();
        userManager = new UserManager(this);

        if(appSessionManager!=null)
        {
            SessionValue sessionValue = appSessionManager.getSession();
            if(sessionValue!=null)
            {
                Log.d("loginresponse","session value from login activity==="+sessionValue.getApi_key());
            }
            else
            {
                Log.d("loginresponse"," session value is null===");
            }
        }
        else
        {
            Log.d("loginresponse","app session manager is null===");
        }
        SessionValue sessionValue = null;
        if(sessionValue!=null)
            Log.d("loginresponse","session value===="+sessionValue.getApi_key());
        else
            Log.d("loginresponse","session value null====");

        if(sessionValue == null ) {
            mHandler.sendEmptyMessageDelayed(START_LOGIN,SPLASH_TIME_OUT);
        } else {
            userInfo = userManager.getUser();
            mHandler.sendEmptyMessageDelayed(START_DASHBOARD,SPLASH_TIME_OUT);
            CaseStudyAppApplication.getParentApplication().setAuthId(sessionValue);
            CaseStudyAppApplication.getParentApplication().setUser(userInfo);
        }

    }

    protected void initInjector() {
        splashComponent = DaggerSplashComponent.builder().baseAppComponent(((AppBaseApplication)getApplication())
                .getBaseAppComponent()).build();
        splashComponent.inject(this);
    }

    class H extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Intent i = new Intent();

            switch (msg.what) {
                case START_DASHBOARD:
                    Log.d("loginresponse","open dashboard");
                    i.setClass(DoctorSGuideSplashScreen.this, DashBoardActivity.class);
                    i.putExtra("user",userInfo);
                    break;
                case START_LOGIN:
                    Log.d("loginresponse","open SocialLoginActivity");
                    i.setClass(DoctorSGuideSplashScreen.this, SocialLoginActivity.class);
                    break;
            }
            startActivity(i);
            finish();
        }
    }

}
