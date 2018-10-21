package com.softmine.drpedia.login.view;

import android.util.Log;

import com.softmine.drpedia.exception.DefaultErrorBundle;
import com.softmine.drpedia.exception.ErrorBundle;
import com.softmine.drpedia.exception.ErrorMessageFactory;
import com.softmine.drpedia.exception.NetworkConnectionException;
import com.softmine.drpedia.getToken.model.LoginResponse;
import com.softmine.drpedia.home.model.CategoryMainItemResponse;
import com.softmine.drpedia.home.model.SubCategoryItem;
import com.softmine.drpedia.login.domain.LoginFacebookUseCase;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

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

                Log.d("socialloginresponse","onCompleted called");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("socialloginresponse","error");
                e.printStackTrace();
                if(e instanceof IOException)
                {
                    if(e instanceof HttpException)
                    {
                        Log.d("socialloginresponse","exception code  "+((HttpException)e).code());
                        SocialLoginPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof ResponseException)
                    {
                        SocialLoginPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof NetworkConnectionException)
                    {
                        Log.d("socialloginresponse","other issues");
                        SocialLoginPresentor.this.showErrorMessage(new DefaultErrorBundle(new NetworkConnectionException()));
                    }
                    else
                    {
                        Log.d("socialloginresponse", "other issue");
                        SocialLoginPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                }
                else
                {
                    if(e instanceof JSONException) {
                        Log.d("socialloginresponse", "Json Parsing exception");
                        SocialLoginPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof HttpException)
                    {
                        Log.d("socialloginresponse", "Http exception issue");
                        SocialLoginPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else
                    {
                        Log.d("socialloginresponse", "other issue");
                        SocialLoginPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                }
            }

            @Override
            public void onNext(LoginResponse loginResponse) {

                Log.d("socialloginresponse","api key==="+loginResponse.getAuthToken());
                Log.d("socialloginresponse","message==="+loginResponse.getMessage());
                Log.d("socialloginresponse","name==="+loginResponse.getList().get(0).getName());
                Log.d("socialloginresponse","user id==="+loginResponse.getList().get(0).getUserid());
                Log.d("socialloginresponse","photo url==="+loginResponse.getList().get(0).getPhotoUrl());
                Log.d("socialloginresponse","email id==="+loginResponse.getList().get(0).getEmailid());
                Log.d("socialloginresponse","gender==="+loginResponse.getList().get(0).getGender());
                Log.d("socialloginresponse","DOB==="+loginResponse.getList().get(0).getDob());

                List<CategoryMainItemResponse> res = loginResponse.getList().get(0).getData();

                for(CategoryMainItemResponse res1 : res)
                {
                    Log.d("socialloginresponse","Main Category name==="+res1.getCategoryName());
                    Log.d("socialloginresponse","Main Category ID==="+res1.getCategoryID());
                    for(SubCategoryItem item1 : res1.getSubCategory())
                    {
                        Log.d("socialloginresponse","sub Category name==="+item1.getSubtype());
                        Log.d("socialloginresponse","sub Category ID==="+item1.getSubtype_id());
                        Log.d("socialloginresponse","sub Category interest ID==="+item1.getIntrest_id());
                    }
                }

                getView().setLoginResponse(loginResponse);
                getView().startActivity();
            }
        });
    }
}
