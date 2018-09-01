package com.softmine.drpedia.login.view;

import android.util.Log;

import com.softmine.drpedia.exception.DefaultErrorBundle;
import com.softmine.drpedia.exception.ErrorBundle;
import com.softmine.drpedia.exception.ErrorMessageFactory;
import com.softmine.drpedia.exception.NetworkConnectionException;
import com.softmine.drpedia.getToken.model.LoginResponse;
import com.softmine.drpedia.login.domain.LoginFacebookUseCase;

import org.json.JSONException;

import java.io.IOException;

import javax.inject.Inject;

import frameworks.basemvp.AppBasePresenter;
import frameworks.network.model.ResponseException;
import frameworks.network.usecases.RequestParams;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public class SocialLoginPresentor extends AppBasePresenter<ILoginViewContractor.View> implements ILoginViewContractor.Presenter {

    LoginFacebookUseCase loginFacebookUseCase;

    @Inject
    public SocialLoginPresentor(LoginFacebookUseCase loginFacebookUseCase)
    {
        this.loginFacebookUseCase = loginFacebookUseCase;
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(getView().getContext(),
                errorBundle.getException());
        getView().showSnackBar(errorMessage);
    }

    @Override
    public void getAuthID(String token) {

        RequestParams requestParams =  LoginFacebookUseCase.createRequestParams(token);
        loginFacebookUseCase.execute(requestParams, new Subscriber<LoginResponse>() {
            @Override
            public void onCompleted() {

                Log.d("loginresponse","onCompleted called");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("loginresponse","error");
                e.printStackTrace();
                if(e instanceof IOException)
                {
                    if(e instanceof HttpException)
                    {
                        Log.d("bookmarkresponse","exception code  "+((HttpException)e).code());
                        SocialLoginPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof ResponseException)
                    {
                        SocialLoginPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof NetworkConnectionException)
                    {
                        Log.d("loginresponse","other issues");
                        SocialLoginPresentor.this.showErrorMessage(new DefaultErrorBundle(new NetworkConnectionException()));
                    }
                    else
                    {
                        Log.d("loginresponse", "other issue");
                        SocialLoginPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                }
                else
                {
                    if(e instanceof JSONException) {
                        Log.d("loginresponse", "Json Parsing exception");
                        SocialLoginPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof HttpException)
                    {
                        Log.d("loginresponse", "Http exception issue");
                        SocialLoginPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else
                    {
                        Log.d("loginresponse", "other issue");
                        SocialLoginPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                }
            }

            @Override
            public void onNext(LoginResponse loginResponse) {

                Log.d("loginresponse","api key==="+loginResponse.getAuthToken());
                Log.d("loginresponse","message==="+loginResponse.getMessage());
                Log.d("loginresponse","name==="+loginResponse.getList().get(0).getName());
                Log.d("loginresponse","user id==="+loginResponse.getList().get(0).getUserid());
                Log.d("loginresponse","photo url==="+loginResponse.getList().get(0).getPhotoUrl());
                Log.d("loginresponse","email id==="+loginResponse.getList().get(0).getEmailid());
                Log.d("loginresponse","gender==="+loginResponse.getList().get(0).getGender());
                Log.d("loginresponse","DOB==="+loginResponse.getList().get(0).getDob());

                getView().setLoginResponse(loginResponse);
                getView().startActivity();
            }
        });
    }
}
