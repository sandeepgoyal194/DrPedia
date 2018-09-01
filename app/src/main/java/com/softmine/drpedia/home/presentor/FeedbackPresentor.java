package com.softmine.drpedia.home.presentor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.softmine.drpedia.exception.DefaultErrorBundle;
import com.softmine.drpedia.exception.ErrorBundle;
import com.softmine.drpedia.exception.ErrorMessageFactory;
import com.softmine.drpedia.exception.NetworkConnectionException;
import com.softmine.drpedia.home.IFeedbackView;
import com.softmine.drpedia.home.domain.usecases.FeedbackUseCase;
import com.softmine.drpedia.home.model.FeedbackItem;
import com.softmine.drpedia.utils.GsonFactory;

import org.json.JSONException;

import java.io.IOException;

import javax.inject.Inject;

import frameworks.network.model.ResponseException;
import frameworks.network.usecases.RequestParams;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public class FeedbackPresentor implements IFeedbackPresentor {

    IFeedbackView iFeedbackView;
    private FeedbackUseCase feedbackUseCase;

    @Inject
    public FeedbackPresentor(FeedbackUseCase feedbackUseCase)
    {
        this.feedbackUseCase = feedbackUseCase;
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(iFeedbackView.getContext(),
                errorBundle.getException());
        iFeedbackView.showSnackBar(errorMessage);
    }

    public void setView(@NonNull IFeedbackView view) {
        this.iFeedbackView = view;
    }

    @Override
    public void ReportFeedback(FeedbackItem feedbackItem) {
        Log.d("feedbacklog","ReportFeedback pressed");

        String userData = GsonFactory.getGson().toJson(feedbackItem);
        RequestParams requestParams =  FeedbackUseCase.createRequestParams(userData);
        this.iFeedbackView.showProgress();
        this.feedbackUseCase.execute(requestParams, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d("feedbacklog","onCompleted called==");
                FeedbackPresentor.this.iFeedbackView.hideProgress();
            }

            @Override
            public void onError(Throwable e) {
                Log.d("feedbacklog","onerror called==");
                FeedbackPresentor.this.iFeedbackView.hideProgress();
                // EditProfilePresenter.this.editProfileView.setProfileUpdateFailed();
                e.printStackTrace();
                if(e instanceof IOException)
                {
                    if(e instanceof HttpException)
                    {
                        Log.d("bookmarkresponse","exception code  "+((HttpException)e).code());
                        FeedbackPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof ResponseException)
                    {
                        FeedbackPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof NetworkConnectionException)
                    {
                        Log.d("loginresponse","other issues");
                        FeedbackPresentor.this.showErrorMessage(new DefaultErrorBundle(new NetworkConnectionException()));
                    }
                    else
                    {
                        Log.d("loginresponse", "other issue");
                        FeedbackPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                }
                else
                {
                    if(e instanceof JSONException) {
                        Log.d("loginresponse", "Json Parsing exception");
                        FeedbackPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof HttpException)
                    {
                        Log.d("loginresponse", "Http exception issue");
                        FeedbackPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else
                    {
                        Log.d("loginresponse", "other issue");
                        FeedbackPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                }
            }

            @Override
            public void onNext(String string) {
                Log.d("feedbacklog","onNext called==");
                // EditProfilePresenter.this.editProfileView.setProfileUpatedSuccessfully();

                FeedbackPresentor.this.iFeedbackView.showToast(string);
                FeedbackPresentor.this.iFeedbackView.setEmptyView();
            }
        });
    }
}
