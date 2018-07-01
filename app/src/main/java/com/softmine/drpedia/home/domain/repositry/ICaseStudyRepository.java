package com.softmine.drpedia.home.domain.repositry;

import com.softmine.drpedia.home.model.CaseItem;
import com.softmine.drpedia.home.model.CommentData;

import java.util.List;

import frameworks.network.usecases.RequestParams;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

public interface ICaseStudyRepository {

    Observable<List<CaseItem>> list();
    Observable<String> likeorUnlikeCaseStudy(RequestParams requestParams);
    Observable<String> bookmarkCaseStudy(RequestParams requestParams);
    Observable<String> uploadCommentOnPost(RequestParams requestParams);
    Observable<List<CommentData>> getListofCommentsOnPost(int postid);
    Observable<String> uploadCaseDetail(RequestBody requestCaseTypeID,RequestBody requestCaseType,RequestBody requestCaseDesc , MultipartBody.Part imageBody);
    Observable<List<CaseItem>> getCaseItemDetail(int postId);

}
