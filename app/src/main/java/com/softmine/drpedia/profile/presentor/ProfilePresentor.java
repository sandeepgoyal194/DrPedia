package com.softmine.drpedia.profile.presentor;

import android.support.annotation.NonNull;
import android.util.Log;


import com.softmine.drpedia.exception.DefaultErrorBundle;
import com.softmine.drpedia.exception.ErrorBundle;
import com.softmine.drpedia.exception.ErrorMessageFactory;
import com.softmine.drpedia.exception.NetworkConnectionException;
import com.softmine.drpedia.getToken.model.LoginResponse;
import com.softmine.drpedia.home.model.CategoryMainItemResponse;
import com.softmine.drpedia.profile.IProfileView;
import com.softmine.drpedia.profile.domain.usecases.UserInterestUseCase;
import com.softmine.drpedia.profile.domain.usecases.UserProfileUseCase;
import com.softmine.drpedia.splash.presentor.SplashScreenPresentor;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import frameworks.network.model.ResponseException;
import frameworks.network.usecases.RequestParams;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public class ProfilePresentor implements IProfilePresentor {

    private IProfileView profileView;
    private UserProfileUseCase userProfileUseCase;
    private UserInterestUseCase userInterestUseCase;
    @Inject
    public ProfilePresentor(UserProfileUseCase userProfileUseCase ,UserInterestUseCase userInterestUseCase)
    {
        this.userProfileUseCase = userProfileUseCase;
        this.userInterestUseCase = userInterestUseCase;
    }

    public void setView(@NonNull IProfileView view) {
        this.profileView = view;
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        Log.d("splashresponse"," showErrorMessage called");
        String errorMessage = ErrorMessageFactory.create(this.profileView.getContext(),
                errorBundle.getException());
        Log.d("splashresponse"," showErrorMessage message "+errorMessage);
        profileView.showSnackBar(errorMessage);
    }

    @Override
    public void getUserProfile() {
        Log.d("userprofile"," getUserProfile");
        this.profileView.showProgressBar();

        this.userProfileUseCase.execute(RequestParams.EMPTY, new Subscriber<LoginResponse>() {
            @Override
            public void onCompleted() {
                Log.d("userprofile"," onCompleted called");
                ProfilePresentor.this.profileView.hideProgressBar();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d("userprofile"," onError called");
                ProfilePresentor.this.profileView.hideProgressBar();
                ProfilePresentor.this.profileView.onProfileSyncFailed();
            }

            @Override
            public void onNext(LoginResponse loginResponse) {
                Log.d("userprofile"," onNext called");
                ProfilePresentor.this.profileView.onProfileSyncedSuccessfully(loginResponse.getList().get(0));
            }
        });

    }

    @Override
    public void getUserInterest() {
        Log.d("splashresponse"," getUserInterest called");
        this.userInterestUseCase.execute(RequestParams.EMPTY, new Subscriber<List<CategoryMainItemResponse>>() {
            @Override
            public void onCompleted() {
                // SplashScreenPresentor.this.splashScreenView.hideProgressBar();
            }

            @Override
            public void onError(Throwable e) {
                Log.d("splashresponse"," onError called");
                //  SplashScreenPresentor.this.splashScreenView.hideProgressBar();
                e.printStackTrace();
                if(e instanceof IOException)
                {
                    if(e instanceof HttpException)
                    {
                        Log.d("splashresponse","exception code  "+((HttpException)e).code());
                        ProfilePresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof ResponseException)
                    {
                        Log.d("splashresponse","ResponseException");
                        ProfilePresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof NetworkConnectionException)
                    {
                        Log.d("splashresponse","NetworkConnectionExceptions");
                        ProfilePresentor.this.showErrorMessage(new DefaultErrorBundle(new NetworkConnectionException()));
                    }
                    else
                    {
                        Log.d("splashresponse", "other issue");
                        ProfilePresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                }
                else
                {
                    if(e instanceof JSONException) {
                        Log.d("splashresponse", "Json Parsing exception");
                        ProfilePresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof HttpException)
                    {
                        Log.d("splashresponse", "Http exception issue");
                        ProfilePresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else
                    {
                        Log.d("splashresponse", "other issue");
                        ProfilePresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                }
            }

            @Override
            public void onNext(List<CategoryMainItemResponse> categoryMainItemResponses) {
                Log.d("splashresponse", "onNext called");
                ProfilePresentor.this.profileView.setUserInterestSize(categoryMainItemResponses);
            }
        });

    }

}
