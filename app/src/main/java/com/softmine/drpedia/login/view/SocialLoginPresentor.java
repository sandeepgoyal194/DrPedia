package com.softmine.drpedia.login.view;

import android.util.Log;

import com.softmine.drpedia.getToken.model.LoginResponse;
import com.softmine.drpedia.login.domain.LoginFacebookUseCase;
import com.softmine.drpedia.login.view.ILoginViewContractor;

import org.json.JSONException;

import java.io.IOException;

import javax.inject.Inject;

import frameworks.basemvp.AppBasePresenter;
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
                if(e instanceof IOException)
                {

                    Log.d("loginresponse","Internet not working");
                    if(e instanceof HttpException)
                    {
                        Log.d("bookmarkresponse","exception code  "+((HttpException)e).code());
                    }
                    else
                    {
                        e.printStackTrace();
                        Log.d("loginresponse","other issues");
                    }
                   // Log.d("loginresponse","exception code  "+((HttpException)e).code());
                    getView().showSnackBar("Internet not working");
                }
                else
                {
                    if(e instanceof JSONException) {
                        Log.d("loginresponse", "Json Parsing exception");
                        getView().showSnackBar("Json Parsing exception");
                    }
                    else if(e instanceof HttpException)
                    {
                        Log.d("loginresponse", "Http exception issue");
                        getView().showSnackBar("Http exception issue");
                    }
                    else
                    {
                        Log.d("loginresponse", "other issue");
                        getView().showSnackBar("Other issue");
                    }
                }
                //e.printStackTrace();
            }

            @Override
            public void onNext(LoginResponse loginResponse) {
                // Log.d("loginresponse","api key==="+sessionValue.getApi_key());
                // getView().setAuthID(sessionValue);
                Log.d("loginresponse","api key==="+loginResponse.getAuthToken());
                Log.d("loginresponse","message==="+loginResponse.getMessage());
                Log.d("loginresponse","name==="+loginResponse.getList().get(0).getName());
                Log.d("loginresponse","user id==="+loginResponse.getList().get(0).getUserid());
                Log.d("loginresponse","photo url==="+loginResponse.getList().get(0).getPhotoUrl());
                Log.d("loginresponse","email id==="+loginResponse.getList().get(0).getEmailid());
                Log.d("loginresponse","gender==="+loginResponse.getList().get(0).getGender());
                Log.d("loginresponse","DOB==="+loginResponse.getList().get(0).getDob());


                getView().setLoginResponse(loginResponse);
            }
        });

    }
}
