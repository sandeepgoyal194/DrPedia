package com.softmine.drpedia.splash.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.softmine.drpedia.CaseStudyAppApplication;
import com.softmine.drpedia.R;
import com.softmine.drpedia.expendablerecylerview.MultiCheckActivity;
import com.softmine.drpedia.home.activity.CategoryListActivity;
import com.softmine.drpedia.home.activity.DashBoardActivity;
import com.softmine.drpedia.home.di.GetCaseStudyListModule;
import com.softmine.drpedia.home.model.CategoryMainItemResponse;
import com.softmine.drpedia.home.model.SubCategoryItem;
import com.softmine.drpedia.home.presentor.CategoryListPresentor;
import com.softmine.drpedia.login.model.User;
import com.softmine.drpedia.login.view.SocialLoginActivity;
import com.softmine.drpedia.splash.di.DaggerSplashComponent;
import com.softmine.drpedia.splash.di.SplashComponent;
import com.softmine.drpedia.splash.presentor.SplashScreenPresentor;
import com.softmine.drpedia.utils.UserManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import frameworks.AppBaseApplication;
import frameworks.appsession.AppSessionManager;
import frameworks.appsession.SessionValue;

public class DoctorSGuideSplashScreen extends AppCompatActivity implements SplashScreenView{

    private static final int START_DASHBOARD = 1;
    private static final int START_LOGIN = 2;
    private static final int START_CATEGORY_LIST = 3;
    public static int SPLASH_TIME_OUT = 3000;

    @BindView(R.id.splashContainer)
    FrameLayout splashContainer;

    @BindView(R.id.progress_bar)
    ProgressBar rl_progress;

    @Inject
    AppSessionManager appSessionManager;

    @Inject
    SplashScreenPresentor splashScreenPresentor;

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

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(appSessionManager!=null)
        {
            SessionValue sessionValue = appSessionManager.getSession();
            if(sessionValue!=null)
            {
                userInfo = userManager.getUser();
                CaseStudyAppApplication.getParentApplication().setAuthId(sessionValue);
                CaseStudyAppApplication.getParentApplication().setUser(userInfo);
                /*Log.d("splashresponse","session value from login activity==="+sessionValue.getApi_key());
                Log.d("splashresponse","api key==="+userInfo.getAuthToken());
                Log.d("splashresponse","name==="+userInfo.getName());
                Log.d("splashresponse","user id==="+userInfo.getUserid());
                Log.d("splashresponse","photo url==="+userInfo.getPhotoUrl());
                Log.d("splashresponse","email id==="+userInfo.getEmailid());
                Log.d("splashresponse","gender==="+userInfo.getGender());
                Log.d("splashresponse","DOB==="+userInfo.getDob());

                List<CategoryMainItemResponse> res = userInfo.getData();

                for(CategoryMainItemResponse res1 : res)
                {
                    Log.d("splashresponse","Main Category name==="+res1.getCategoryName());
                    Log.d("splashresponse","Main Category ID==="+res1.getCategoryID());
                    for(SubCategoryItem item1 : res1.getSubCategory())
                    {
                        Log.d("splashresponse","sub Category name==="+item1.getSubtype());
                        Log.d("splashresponse","sub Category ID==="+item1.getSubtype_id());
                        Log.d("splashresponse","sub Category interest ID==="+item1.getIntrest_id());
                    }
                }*/
                splashScreenPresentor.setView(this);
                splashScreenPresentor.getUserInterest();
            }
            else
            {
                mHandler.sendEmptyMessageDelayed(START_LOGIN,SPLASH_TIME_OUT);

                Log.d("splashresponse"," session value is null===");
            }
        }
    }

    protected void initInjector() {
        splashComponent = DaggerSplashComponent.builder()
                .baseAppComponent(((AppBaseApplication)getApplication())
                .getBaseAppComponent())
                .getCaseStudyListModule(new GetCaseStudyListModule(this))
                .build();
        splashComponent.inject(this);
    }

    @Override
    public void setUserInterestSize(int size) {
        Log.d("splashresponse"," user interest list size is "+size);
        if(size>0) {
            if(mHandler!=null)
                mHandler.sendEmptyMessageDelayed(START_DASHBOARD, SPLASH_TIME_OUT);
            else
                Log.d("splashresponse"," handler is null");
        }
        else {
            if(mHandler!=null)
                mHandler.sendEmptyMessageDelayed(START_CATEGORY_LIST, SPLASH_TIME_OUT);
            else
                Log.d("splashresponse"," handler is null");
        }
    }
    @Override
    public void showProgressBar() {
        this.rl_progress.setVisibility(View.VISIBLE);
        this.setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void showProgressBar(String message) {

    }

    @Override
    public void hideProgressBar() {
        this.rl_progress.setVisibility(View.GONE);
        this.setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void showSnackBar(String message) {
        Log.d("splashresponse"," showSnackBar called");
        Log.d("splashresponse"," message  === "+message);
        Snackbar snackbar = Snackbar
                .make(splashContainer, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void addEmptyLayout() {

    }

    class H extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Intent i = new Intent();

            switch (msg.what) {
                case START_DASHBOARD:
                    Log.d("splashresponse","open dashboard");
                    i.setClass(DoctorSGuideSplashScreen.this, DashBoardActivity.class);
                    break;
                case START_LOGIN:
                    Log.d("splashresponse","open SocialLoginActivity");
                    i.setClass(DoctorSGuideSplashScreen.this, SocialLoginActivity.class);
                    break;
                case START_CATEGORY_LIST:
                    Log.d("splashresponse","open SocialLoginActivity");
                    i.setClass(DoctorSGuideSplashScreen.this, CategoryListActivity.class);
                    break;
            }
            startActivity(i);
            finish();
        }
    }

}
