package com.softmine.drpedia.home.domain.repositry;


import com.softmine.drpedia.getToken.model.LoginResponse;
import com.softmine.drpedia.home.model.BookmarkItem;
import com.softmine.drpedia.home.model.CaseItem;
import com.softmine.drpedia.home.model.CategoryMainItemResponse;
import com.softmine.drpedia.home.model.CommentData;

import java.util.List;
import java.util.Map;

import frameworks.network.usecases.RequestParams;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

public interface ICaseStudyRepository {


    Observable<List<CaseItem>> list();
    Observable<List<CaseItem>> myCaselist();
    Observable<List<BookmarkItem>> getBookmarklist();
    Observable<String> likeorUnlikeCaseStudy(RequestParams requestParams);
    Observable<String> bookmarkCaseStudy(RequestParams requestParams);
    Observable<String> uploadCommentOnPost(RequestParams requestParams);
    Observable<List<CommentData>> getListofCommentsOnPost(int postid);
    Observable<String> uploadCaseDetail(String uploadData);
    Observable<Integer> uploadCaseImage(List<MultipartBody.Part> files);
    Observable<Integer> uploadCaseVideo(List<MultipartBody.Part> files);
    Observable<List<CaseItem>> getCaseItemDetail(int postId);
    Observable<LoginResponse> getUserProfile();
    Observable<LoginResponse> updateUserProfile(String userid , String userData);
    Observable<String> reportFeedback(String feedbackBody);
    Observable<List<CategoryMainItemResponse>> categoryList();
    Observable<String> createUserInterest(String userInterestTypes);
    Observable<String> updateUserInterest(String updateUserInterestTypes);
    Observable<List<CategoryMainItemResponse>> getUserInterestCount();
}
