package com.softmine.drpedia.home.domain.usecases;

import android.util.Log;


import com.softmine.drpedia.home.domain.repositry.ICaseStudyRepository;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import frameworks.network.usecases.UseCase;
import rx.Observable;

public class GetCaseStudyBookmarkUseCase extends UseCase<String> {

    public static final String bookmark_Status = "status";
    public static final String post_ID = "post_id";
    private final ICaseStudyRepository getCaseRepo;

    @Inject
    public GetCaseStudyBookmarkUseCase(ICaseStudyRepository getCaseRepo) {
        this.getCaseRepo = getCaseRepo;
    }

    public static RequestParams createRequestParams(String bookmarkStatus,int postId) {
        Log.d("imagelogs","image bookmarked in createRequestParams view");
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(bookmark_Status,bookmarkStatus);
        requestParams.putInt(post_ID,postId);
        return requestParams;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        Log.d("imagelogs","image bookmarked in createObservable view");
        return this.getCaseRepo.bookmarkCaseStudy(requestParams);
    }
}
