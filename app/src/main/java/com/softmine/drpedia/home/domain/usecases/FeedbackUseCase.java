package com.softmine.drpedia.home.domain.usecases;

import android.util.Log;

import com.softmine.drpedia.home.domain.repositry.ICaseStudyRepository;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import frameworks.network.usecases.UseCase;
import rx.Observable;

public class FeedbackUseCase extends UseCase<String> {

    private final ICaseStudyRepository getCaseRepo;
    public static final String fb_body = "feedbackBody";

    @Inject
    FeedbackUseCase(ICaseStudyRepository getCaseRepo) {
        this.getCaseRepo = getCaseRepo;
    }

    public static RequestParams createRequestParams(String feedbackBody) {
        Log.d("feedbacklog","ReportFeedback params pressed");
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(fb_body,feedbackBody);
        return requestParams;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        Log.d("feedbacklog","feedback body==="+requestParams.getString(fb_body , null));
        return this.getCaseRepo.reportFeedback(requestParams.getString(fb_body , null));
    }
}
