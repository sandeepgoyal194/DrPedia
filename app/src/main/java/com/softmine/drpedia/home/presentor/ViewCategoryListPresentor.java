package com.softmine.drpedia.home.presentor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.softmine.drpedia.exception.DefaultErrorBundle;
import com.softmine.drpedia.exception.ErrorBundle;
import com.softmine.drpedia.exception.ErrorMessageFactory;
import com.softmine.drpedia.exception.NetworkConnectionException;
import com.softmine.drpedia.home.IViewCategoryListView;
import com.softmine.drpedia.home.model.CategoryMainItemResponse;
import com.softmine.drpedia.profile.IProfileView;
import com.softmine.drpedia.profile.domain.usecases.UserInterestUseCase;
import com.softmine.drpedia.profile.domain.usecases.UserProfileUseCase;
import com.softmine.drpedia.profile.presentor.ProfilePresentor;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import frameworks.network.model.ResponseException;
import frameworks.network.usecases.RequestParams;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public class ViewCategoryListPresentor implements IViewCategoryListPresentor{

    private IViewCategoryListView categoryListView;
    private UserInterestUseCase userInterestUseCase;

    @Inject
    public ViewCategoryListPresentor(UserInterestUseCase userInterestUseCase)
    {
        this.userInterestUseCase = userInterestUseCase;
    }

    public void setView(@NonNull IViewCategoryListView view) {
        this.categoryListView = view;
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        Log.d("splashresponse"," showErrorMessage called");
        String errorMessage = ErrorMessageFactory.create(this.categoryListView.getContext(),
                errorBundle.getException());
        Log.d("splashresponse"," showErrorMessage message "+errorMessage);
        categoryListView.showSnackBar(errorMessage);
    }

    @Override
    public void getUserInterest() {

        categoryListView.showProgressBar();
        Log.d("splashresponse"," getUserInterest called");
        this.userInterestUseCase.execute(RequestParams.EMPTY, new Subscriber<List<CategoryMainItemResponse>>() {
            @Override
            public void onCompleted() {
                categoryListView.hideProgressBar();
                // SplashScreenPresentor.this.splashScreenView.hideProgressBar();
            }

            @Override
            public void onError(Throwable e) {
                Log.d("splashresponse"," onError called");
                //  SplashScreenPresentor.this.splashScreenView.hideProgressBar();
                categoryListView.hideProgressBar();
                e.printStackTrace();
                if(e instanceof IOException)
                {
                    if(e instanceof HttpException)
                    {
                        Log.d("splashresponse","exception code  "+((HttpException)e).code());
                        ViewCategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof ResponseException)
                    {
                        Log.d("splashresponse","ResponseException");
                        ViewCategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof NetworkConnectionException)
                    {
                        Log.d("splashresponse","NetworkConnectionExceptions");
                        ViewCategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle(new NetworkConnectionException()));
                    }
                    else
                    {
                        Log.d("splashresponse", "other issue");
                        ViewCategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                }
                else
                {
                    if(e instanceof JSONException) {
                        Log.d("splashresponse", "Json Parsing exception");
                        ViewCategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof HttpException)
                    {
                        Log.d("splashresponse", "Http exception issue");
                        ViewCategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else
                    {
                        Log.d("splashresponse", "other issue");
                        ViewCategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                }
            }

            @Override
            public void onNext(List<CategoryMainItemResponse> categoryMainItemResponses) {
                Log.d("splashresponse", "onNext called");

                ViewCategoryListPresentor.this.categoryListView.setUserInterestSize(categoryMainItemResponses);
            }
        });

    }
}
