package com.softmine.drpedia.home.domain.usecases;

import android.util.Log;


import com.softmine.drpedia.home.domain.repositry.ICaseStudyRepository;
import com.softmine.drpedia.home.model.CaseItem;

import java.util.List;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import frameworks.network.usecases.UseCase;
import rx.Observable;

public class GetCaseDetailUseCase extends UseCase<List<CaseItem>>
{
    public static final String postID = "postid";
    private final ICaseStudyRepository getCaseRepo;

    @Inject
    GetCaseDetailUseCase(ICaseStudyRepository getCaseRepo)
    {
        this.getCaseRepo = getCaseRepo;
    }

    public static RequestParams createRequestParams(int postId) {
        Log.d("imagelogs","get case detail in createRequestParams view");
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(postID,postId);
        return requestParams;
    }

    @Override
    public Observable<List<CaseItem>> createObservable(RequestParams requestParams) {
        return this.getCaseRepo.getCaseItemDetail(requestParams.getInt(postID,0));
    }
}
