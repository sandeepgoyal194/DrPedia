package com.softmine.drpedia.login.view;

import android.util.Log;

import com.softmine.drpedia.getToken.model.LoginResponse;
import com.softmine.drpedia.login.domain.LoginFacebookUseCase;

import javax.inject.Inject;

import frameworks.basemvp.AppBasePresenter;
import frameworks.network.usecases.RequestParams;
import rx.Subscriber;

public class SocialLoginPresentor extends AppBasePresenter<ILoginViewContractor.View> implements ILoginViewContractor.Presenter {

    LoginFacebookUseCase loginFacebookUseCase;

    @Inject
    public SocialLoginPresentor(LoginFacebookUseCase loginFacebookUseCase)
    {
        this.loginFacebookUseCase = loginFacebookUseCase;
    }

    @Override
    public void getAuthID(String token) {

        RequestParams requestParams =  LoginFacebookUseCase.createRequestParams(token);
        loginFacebookUseCase.execute(requestParams, new Subscriber<LoginResponse>() {
            @Override
            public void onCompleted() {
                // Call MainActivity to show list of feedbacks
                Log.d("loginresponse","onCompleted called");
                getView().startActivity();
            }

            @Override
            public void onError(Throwable e) {
                Log.d("loginresponse","error");
                e.printStackTrace();
            }

            @Override
            public void onNext(LoginResponse loginResponse) {
               // Log.d("loginresponse","api key==="+sessionValue.getApi_key());
               // getView().setAuthID(sessionValue);
                Log.d("loginresponse","api key==="+loginResponse.getAuthToken());
                Log.d("loginresponse","api key==="+loginResponse.getMessage());
                Log.d("loginresponse","api key==="+loginResponse.getList().get(0).getName());
                Log.d("loginresponse","api key==="+loginResponse.getList().get(0).getUserid());
                Log.d("loginresponse","api key==="+loginResponse.getList().get(0).getPhotoUrl());
                Log.d("loginresponse","api key==="+loginResponse.getList().get(0).getEmailid());
                getView().setLoginResponse(loginResponse);
            }
        });

    }
}
