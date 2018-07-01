package com.softmine.drpedia.home.domain.repositry;


import android.util.Log;

import com.softmine.drpedia.home.domain.datasource.GetCaseStudyDataFactory;
import com.softmine.drpedia.home.model.CaseItem;
import com.softmine.drpedia.home.model.CommentData;

import java.util.List;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

public class GetCaseStudyDataRepository implements ICaseStudyRepository {

    private GetCaseStudyDataFactory getCaseStudyDataFactory;

    @Inject
    public GetCaseStudyDataRepository(GetCaseStudyDataFactory getCaseStudyDataFactory)
    {
        this.getCaseStudyDataFactory = getCaseStudyDataFactory;
    }

    @Override
    public Observable<List<CaseItem>> list() {
        return this.getCaseStudyDataFactory.createCaseStudyDataSource().caseStudyList();
    }

    @Override
    public Observable<String> likeorUnlikeCaseStudy(RequestParams requestParams) {
        Log.d("imagelogs","image liked in likeorUnlikeCaseStudy  repo view");
        return this.getCaseStudyDataFactory.createCaseStudyDataSource().likeorUnlikeCaseStudy(requestParams);
    }

    @Override
    public Observable<String> bookmarkCaseStudy(RequestParams requestParams) {
        Log.d("imagelogs","image bookmarked in bookmarkCaseStudy  repo view");
        return this.getCaseStudyDataFactory.createCaseStudyDataSource().bookmarkCaseStudy(requestParams);
    }

    @Override
    public Observable<String> uploadCommentOnPost(RequestParams requestParams) {
        return this.getCaseStudyDataFactory.createCaseStudyDataSource().uploadCommentOnPost(requestParams);
    }

    @Override
    public Observable<List<CommentData>> getListofCommentsOnPost(int postid) {
        Log.d("imagelogs","getListofCommentsOnPost in repo view");
        return this.getCaseStudyDataFactory.createCaseStudyDataSource().getListofCommentsOnPost(postid);
    }

    @Override
    public Observable<String> uploadCaseDetail(RequestBody requestCaseTypeID,RequestBody requestCaseDesc1, RequestBody requestCaseDesc2, MultipartBody.Part imageBody) {
        Log.d("uploadlogs","uploadCaseDetail in repo view");

        Log.d("uploadlogs","id==="+requestCaseTypeID.toString());
        Log.d("uploadlogs","desc==="+requestCaseDesc2.toString());

        return this.getCaseStudyDataFactory.createCaseStudyDataSource().uploadCaseDetail(requestCaseTypeID,requestCaseDesc1,requestCaseDesc2,imageBody);
    }

    @Override
    public Observable<List<CaseItem>> getCaseItemDetail(int postId) {
        return this.getCaseStudyDataFactory.createCaseStudyDataSource().getCaseItemDetail(postId);
    }


}
