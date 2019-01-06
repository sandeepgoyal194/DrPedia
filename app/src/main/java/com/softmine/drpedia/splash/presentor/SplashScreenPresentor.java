package com.softmine.drpedia.splash.presentor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.softmine.drpedia.exception.DefaultErrorBundle;
import com.softmine.drpedia.exception.ErrorBundle;
import com.softmine.drpedia.exception.ErrorMessageFactory;
import com.softmine.drpedia.exception.NetworkConnectionException;
import com.softmine.drpedia.home.CaseListView;
import com.softmine.drpedia.home.model.CategoryMainItemResponse;
import com.softmine.drpedia.home.presentor.CategoryListPresentor;
import com.softmine.drpedia.splash.activity.SplashScreenView;
import com.softmine.drpedia.splash.di.usecase.SplashScreenUseCase;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import frameworks.network.model.ResponseException;
import frameworks.network.usecases.RequestParams;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public class SplashScreenPresentor implements ISplashScreenPresentor{

    private SplashScreenView splashScreenView;
    private SplashScreenUseCase splashScreenUseCase;

    @Inject
    public SplashScreenPresentor(SplashScreenUseCase splashScreenUseCase) {
        this.splashScreenUseCase = splashScreenUseCase;
    }

    public void setView(@NonNull SplashScreenView splashScreenView) {

        Log.d("splashresponse"," setView called");
        this.splashScreenView = splashScreenView;

        if(this.splashScreenView.getContext()!=null)
        {
            Log.d("splashresponse"," context not null.. proceed to show toast");
        }
        else
        {
            Log.d("splashresponse"," setView called with null");
        }
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        Log.d("splashresponse"," showErrorMessage called");

        if(this.splashScreenView.getContext()!=null)
        {
            Log.d("splashresponse"," context not null.. proceed to show toast");
        }

        String errorMessage = ErrorMessageFactory.create(this.splashScreenView.getContext(),
                errorBundle.getException());
        Log.d("splashresponse"," showErrorMessage message "+errorMessage);
        splashScreenView.showSnackBar(errorMessage);
    }

    @Override
    public void getUserInterest() {
        Log.d("splashresponse"," getUserInterest called");

        if(this.splashScreenView.getContext()!=null)
        {
            Log.d("splashresponse"," context not null.. proceed to show toast");
        }

      this.splashScreenUseCase.execute(RequestParams.EMPTY, new Subscriber<List<CategoryMainItemResponse>>() {
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
                      SplashScreenPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                  }
                  else if(e instanceof ResponseException)
                  {
                      Log.d("splashresponse","ResponseException");
                      SplashScreenPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                  }
                  else if(e instanceof NetworkConnectionException)
                  {
                      Log.d("splashresponse","NetworkConnectionExceptions");

                      SplashScreenPresentor.this.showErrorMessage(new DefaultErrorBundle(new NetworkConnectionException()));
                  }
                  else
                  {
                      Log.d("splashresponse", "other issue");
                      SplashScreenPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                  }
              }
              else
              {
                  if(e instanceof JSONException) {
                      Log.d("splashresponse", "Json Parsing exception");
                      SplashScreenPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                  }
                  else if(e instanceof HttpException)
                  {
                      Log.d("splashresponse", "Http exception issue");
                      SplashScreenPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                  }
                  else
                  {
                      Log.d("splashresponse", "other issue");
                      SplashScreenPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                  }
              }
          }

          @Override
          public void onNext(List<CategoryMainItemResponse> categoryMainItemResponses) {
              Log.d("splashresponse", "onNext called");
            SplashScreenPresentor.this.splashScreenView.setUserInterestSize(categoryMainItemResponses.size());
          }
      });

    }
}
