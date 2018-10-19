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
import com.softmine.drpedia.expendablerecylerview.MultiCheckActivity;
import com.softmine.drpedia.home.activity.CategoryListActivity;
import com.softmine.drpedia.home.activity.DashBoardActivity;
import com.softmine.drpedia.home.model.CategoryMainItemResponse;
import com.softmine.drpedia.home.model.SubCategoryItem;
import com.softmine.drpedia.login.model.User;
import com.softmine.drpedia.login.view.SocialLoginActivity;
import com.softmine.drpedia.splash.di.DaggerSplashComponent;
import com.softmine.drpedia.splash.di.SplashComponent;
import com.softmine.drpedia.utils.UserManager;

import java.util.List;

import javax.inject.Inject;

import frameworks.AppBaseApplication;
import frameworks.appsession.AppSessionManager;
import frameworks.appsession.SessionValue;

public class DoctorSGuideSplashScreen extends AppCompatActivity {

    private static final int START_DASHBOARD = 1;
    private static final int START_LOGIN = 2;
    private static final int START_CATEGORY_LIST = 3;
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

        userManager = new UserManager(this);

        if(appSessionManager!=null)
        {
            SessionValue sessionValue = appSessionManager.getSession();
            if(sessionValue!=null)
            {
                userInfo = userManager.getUser();
                CaseStudyAppApplication.getParentApplication().setAuthId(sessionValue);
                CaseStudyAppApplication.getParentApplication().setUser(userInfo);
                Log.d("loginresponse","session value from login activity==="+sessionValue.getApi_key());
                Log.d("userprofile","api key==="+userInfo.getAuthToken());
                Log.d("userprofile","name==="+userInfo.getName());
                Log.d("userprofile","user id==="+userInfo.getUserid());
                Log.d("userprofile","photo url==="+userInfo.getPhotoUrl());
                Log.d("userprofile","email id==="+userInfo.getEmailid());
                Log.d("userprofile","gender==="+userInfo.getGender());
                Log.d("userprofile","DOB==="+userInfo.getDob());

                List<CategoryMainItemResponse> res = userInfo.getData();

                for(CategoryMainItemResponse res1 : res)
                {
                    Log.d("userprofile","Main Category name==="+res1.getCategoryName());
                    Log.d("userprofile","Main Category ID==="+res1.getCategoryID());
                    for(SubCategoryItem item1 : res1.getSubCategory())
                    {
                        Log.d("userprofile","sub Category name==="+item1.getSubtype());
                        Log.d("userprofile","sub Category ID==="+item1.getSubtype_id());
                        Log.d("userprofile","sub Category interest ID==="+item1.getIntrest_id());
                    }
                }

                if(res.size()>0)
                    mHandler.sendEmptyMessageDelayed(START_DASHBOARD,SPLASH_TIME_OUT);
                else
                    mHandler.sendEmptyMessageDelayed(START_CATEGORY_LIST,SPLASH_TIME_OUT);
            }
            else
            {
                mHandler.sendEmptyMessageDelayed(START_LOGIN,SPLASH_TIME_OUT);

                Log.d("loginresponse"," session value is null===");
            }
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
                    i.setClass(DoctorSGuideSplashScreen.this, CategoryListActivity.class);
                   // i.putExtra("user",userInfo);
                    break;
                case START_LOGIN:
                    Log.d("loginresponse","open SocialLoginActivity");
                    i.setClass(DoctorSGuideSplashScreen.this, SocialLoginActivity.class);
                    break;
                case START_CATEGORY_LIST:
                    Log.d("loginresponse","open SocialLoginActivity");
                    i.setClass(DoctorSGuideSplashScreen.this, CategoryListActivity.class);
                    break;
            }
            startActivity(i);
            finish();
        }
    }

}
