package com.softmine.drpedia.home.presentor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.softmine.drpedia.home.IFeedbackView;
import com.softmine.drpedia.home.domain.usecases.FeedbackUseCase;
import com.softmine.drpedia.home.model.FeedbackItem;
import com.softmine.drpedia.utils.GsonFactory;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import rx.Subscriber;

public class FeedbackPresentor implements IFeedbackPresentor {

    IFeedbackView iFeedbackView;
    private FeedbackUseCase feedbackUseCase;

    @Inject
    public FeedbackPresentor(FeedbackUseCase feedbackUseCase)
    {
        this.feedbackUseCase = feedbackUseCase;
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
