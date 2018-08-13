package com.softmine.drpedia.home.domain.usecases;

import android.util.Log;


import com.softmine.drpedia.home.domain.repositry.ICaseStudyRepository;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import frameworks.network.usecases.UseCase;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

public class GetCaseStudyLikeUseCase  extends UseCase<String> {

    public static final String like_Status = "status";
    public static final String postID = "post_id";
    private final ICaseStudyRepository getCaseRepo;

    @Inject
    GetCaseStudyLikeUseCase(ICaseStudyRepository getCaseRepo)
    {
        this.getCaseRepo = getCaseRepo;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        Log.d("imagelogs","image liked in createObservable view");
        return this.getCaseRepo.likeorUnlikeCaseStudy(requestParams);
    }

    public static RequestParams createRequestParams(String likeStatus,int postId) {
        Log.d("imagelogs","image liked in createRequestParams view");
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(like_Status,likeStatus);
        requestParams.putInt(postID,postId);
        return requestParams;
    }
}
