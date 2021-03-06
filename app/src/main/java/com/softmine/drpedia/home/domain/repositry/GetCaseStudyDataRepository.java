package com.softmine.drpedia.home.domain.repositry;


import android.util.Log;

import com.softmine.drpedia.getToken.model.LoginResponse;
import com.softmine.drpedia.home.domain.datasource.GetCaseStudyDataFactory;
import com.softmine.drpedia.home.model.BookmarkItem;
import com.softmine.drpedia.home.model.CaseItem;
import com.softmine.drpedia.home.model.CategoryMainItemResponse;
import com.softmine.drpedia.home.model.CommentData;

import java.util.List;
import java.util.Map;

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
    public Observable<List<CaseItem>> myCaselist() {
        return this.getCaseStudyDataFactory.createCaseStudyDataSource().myCaseStudyList();
    }

    @Override
    public Observable<List<BookmarkItem>> getBookmarklist() {
        return this.getCaseStudyDataFactory.createCaseStudyDataSource().getBookmarklist();
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
    public Observable<String> uploadCaseDetail(String uploadData) {

        return this.getCaseStudyDataFactory.createCaseStudyDataSource().uploadCaseDetail(uploadData);
    }

    @Override
    public Observable<Integer> uploadCaseImage(List<MultipartBody.Part> files) {
        return this.getCaseStudyDataFactory.createCaseStudyDataSource().uploadCaseImage(files);
    }


    @Override
    public Observable<Integer> uploadCaseVideo(List<MultipartBody.Part> files) {
        return this.getCaseStudyDataFactory.createCaseStudyDataSource().uploadCaseVideo(files);
    }

    @Override
    public Observable<List<CaseItem>> getCaseItemDetail(int postId) {
        return this.getCaseStudyDataFactory.createCaseStudyDataSource().getCaseItemDetail(postId);
    }

    @Override
    public Observable<LoginResponse> getUserProfile() {
        return this.getCaseStudyDataFactory.createCaseStudyDataSource().getUserProfile();
    }

    @Override
    public Observable<LoginResponse> updateUserProfile(String userid, String userData) {
        return this.getCaseStudyDataFactory.createCaseStudyDataSource().updateUserProfile(userid , userData);
    }

    @Override
    public Observable<String> reportFeedback(String feedbackBody) {
        Log.d("feedbacklog","ReportFeedback repo");
        return this.getCaseStudyDataFactory.createCaseStudyDataSource().reportFeedback(feedbackBody);
    }

    @Override
    public Observable<List<CategoryMainItemResponse>> categoryList() {
        return this.getCaseStudyDataFactory.createCaseStudyDataSource().categoryList();
    }

    @Override
    public Observable<String> createUserInterest(String userInterestTypes) {
        return this.getCaseStudyDataFactory.createCaseStudyDataSource().createUserInterest(userInterestTypes);
    }

    @Override
    public Observable<String> updateUserInterest(String updateUserInterestTypes) {
        return this.getCaseStudyDataFactory.createCaseStudyDataSource().updateUserInterest(updateUserInterestTypes);
    }

    @Override
    public Observable<List<CategoryMainItemResponse>> getUserInterestCount() {
        return this.getCaseStudyDataFactory.createCaseStudyDataSource().getUserInterestCount();
    }


}
