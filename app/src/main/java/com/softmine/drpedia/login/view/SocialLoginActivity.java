package com.softmine.drpedia.login.view;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


import com.softmine.drpedia.CaseStudyAppApplication;
import com.softmine.drpedia.R;
import com.softmine.drpedia.getToken.model.LoginResponse;
import com.softmine.drpedia.home.activity.DashBoardActivity;
import com.softmine.drpedia.login.di.DaggerSocialLoginComponent;
import com.softmine.drpedia.login.di.SocialLoginComponent;
import com.softmine.drpedia.login.model.User;
import com.softmine.drpedia.utils.UserManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import frameworks.AppBaseApplication;
import frameworks.appsession.AppSessionManager;
import frameworks.appsession.SessionValue;
import frameworks.appsession.UserInfo;
import frameworks.basemvp.AppBaseActivity;
import frameworks.di.component.HasComponent;

public class SocialLoginActivity extends AppBaseActivity<ILoginViewContractor.Presenter> implements ILoginViewContractor.View,HasComponent<SocialLoginComponent> {

    @Inject
    SocialLoginPresentor socialLoginPresentor;
    @Inject
    AppSessionManager appSessionManager;
    SocialLoginComponent socialLoginComponent;

    @BindView(R.id.fbLoginContainer)
    RelativeLayout parentLayout;

    @BindView(R.id.fblogin)
    FrameLayout loginFbButton;

    int FACEBOOK_LOGIN_REQUEST_CODE = 21;
    int FACEBOOK_LOGIN_RESPONSE_OK = 22;
    int FACEBOOK_LOGIN_RESPONSE_FAILS = 23;
    int FACEBOOK_INTERNET_LOGIN_RESPONSE_FAILS = 24;

    UserManager userManager;

    User user;

    @Override
    protected void initInjector() {
        socialLoginComponent = DaggerSocialLoginComponent.builder().baseAppComponent(((AppBaseApplication)getApplication())
                .getBaseAppComponent()).build();
        socialLoginComponent.inject(this);
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
    }

    @Override
    public int getViewToCreate() {
        return R.layout.activity_main;
    }

    @Override
    public ILoginViewContractor.Presenter getPresenter() {
        return socialLoginPresentor;
    }

    @OnClick(R.id.fblogin)
    public void getFacebookToken()
    {
        startActivityForResult(new Intent(this, FacebookLoginActivity.class), FACEBOOK_LOGIN_REQUEST_CODE);
    }

    // UserInfo userInfo;
    String token;
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FACEBOOK_LOGIN_REQUEST_CODE) {
            if (resultCode == FACEBOOK_LOGIN_RESPONSE_OK) {
                if (data != null)
                {
                    token = (String) data.getExtras().get("authToken");
                    Log.d("loginresponse","fbtoken===="+token);
                    socialLoginPresentor.getAuthID(token);
                }
                else
                {
                    Log.d("loginresponse","data is null");
                }
            }
            else if(resultCode == FACEBOOK_LOGIN_RESPONSE_FAILS)
            {
                Log.d("loginresponse","failed");
                showSnackBar("Error occured while login with facebook");
            }
            else if(resultCode == FACEBOOK_INTERNET_LOGIN_RESPONSE_FAILS)
            {
                Log.d("loginresponse","Internet not working");
                showSnackBar("Internet not working");
            }
        }
    }

    @Override
    public SocialLoginComponent getComponent() {
        if(socialLoginComponent == null)
            initInjector();
        return socialLoginComponent;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (socialLoginPresentor != null) {
            socialLoginPresentor.detachView();
        }
    }

    public void showSnackBar(String message) {

        Snackbar snackbar = Snackbar
                .make(parentLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    @Override
    public void setLoginResponse(LoginResponse loginResponse) {
        SessionValue sessionValue = new SessionValue();
        sessionValue.setApi_key(loginResponse.getAuthToken());

        user = loginResponse.getList().get(0);

        CaseStudyAppApplication.getParentApplication().setAuthId(sessionValue);
        CaseStudyAppApplication.getParentApplication().setUser(user);
        appSessionManager.saveSession(sessionValue);
        userManager = new UserManager(this);
        userManager.saveUser(user);
        Log.d("loginresponse","session value==="+appSessionManager.getSession().getApi_key());
    }

    @Override
    public void startActivity() {
        Intent dashBoardIntent = new Intent(this, DashBoardActivity.class);
        //   dashBoardIntent.putExtra("user",CaseStudyAppApplication.getParentApplication().getUser());
        startActivity(dashBoardIntent);
        finish();
    }
}
