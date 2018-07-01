package com.softmine.drpedia.home.domain.datasource;

import android.util.Log;

import com.softmine.drpedia.home.model.CaseItem;
import com.softmine.drpedia.home.model.CaseListResponse;
import com.softmine.drpedia.home.model.CommentData;
import com.softmine.drpedia.home.model.CommentsListResponse;
import com.softmine.drpedia.home.net.CaseStudyAPI;

import java.util.List;

import javax.inject.Inject;

import frameworks.network.model.DataResponse;
import frameworks.network.usecases.RequestParams;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

public class GetCaseStudyDataSource implements ICaseStudyDataSource {

    private CaseStudyAPI caseStudyAPI;

    @Inject
    public GetCaseStudyDataSource(CaseStudyAPI caseStudyAPI)
    {
        this.caseStudyAPI = caseStudyAPI;
    }

    @Override
    public Observable<List<CaseItem>> caseStudyList() {
        Log.d("loginresponse","caseStudyList data source");
        return  this.caseStudyAPI.caseStudyList().map(new Func1<Response<DataResponse<CaseListResponse>>, CaseListResponse>() {
            @Override
            public CaseListResponse call(Response<DataResponse<CaseListResponse>> caseResponseResponse) {
                Log.d("loginresponse","caseStudyList call source");
                return caseResponseResponse.body().getData();
            }
        }).map(new Func1<CaseListResponse, List<CaseItem>>() {
            @Override
            public List<CaseItem> call(CaseListResponse caseListResponse) {
                Log.d("loginresponse","caseStudyList list return");
                return caseListResponse.getData();
            }
        });
    }

    @Override
    public Observable<String> likeorUnlikeCaseStudy(RequestParams requestParams) {



        Log.d("imagelogs","image liked in likeorUnlikeCaseStudy  data source view");
        return this.caseStudyAPI.likeCaseStudy(requestParams.getParameters()).map(new Func1<Response<DataResponse<CaseListResponse>>, CaseListResponse>() {
            @Override
            public CaseListResponse call(Response<DataResponse<CaseListResponse>> caseResponseResponse) {

                Log.d("imagelogs","like data=="+caseResponseResponse.body().getData().getMessage());
                return caseResponseResponse.body().getData();
            }
        }).map(new Func1<CaseListResponse, String>() {
            @Override
            public String call(CaseListResponse caseListResponse) {
                Log.d("imagelogs","like message=="+caseListResponse.getMessage());
                return caseListResponse.getMessage();
            }
        });
    }

    @Override
    public Observable<String> bookmarkCaseStudy(RequestParams requestParams) {
        Log.d("loginresponse","image bookmarked in bookmarkCaseStudy  data source view");
        return this.caseStudyAPI.bookmarkCaseStudy(requestParams.getParameters()).map(new Func1<Response<DataResponse<CaseListResponse>>, CaseListResponse>() {
            @Override
            public CaseListResponse call(Response<DataResponse<CaseListResponse>> caseResponseResponse) {
                Log.d("imagelogs","bookmark data=="+caseResponseResponse.body().getData().getMessage());
                return caseResponseResponse.body().getData();
            }
        }).map(new Func1<CaseListResponse, String>() {
            @Override
            public String call(CaseListResponse caseListResponse) {
                Log.d("imagelogs","bookmark message=="+caseListResponse.getMessage());
                return caseListResponse.getMessage();
            }
        });
    }

    @Override
    public Observable<String> uploadCommentOnPost(RequestParams requestParams) {
        Log.d("imagelogs","image commented in uploadCommentOnPost  data source view");
        return this.caseStudyAPI.uploadCommentOnPost(requestParams.getParameters()).map(new Func1<Response<DataResponse<CaseListResponse>>, CaseListResponse>() {
            @Override
            public CaseListResponse call(Response<DataResponse<CaseListResponse>> caseResponseResponse) {
                Log.d("imagelogs","comment on post response=="+caseResponseResponse.body().getData().getMessage());
                return caseResponseResponse.body().getData();
            }
        }).map(new Func1<CaseListResponse, String>() {
            @Override
            public String call(CaseListResponse caseListResponse) {
                Log.d("imagelogs","comment on post response=="+caseListResponse.getMessage());
                return caseListResponse.getMessage();
            }
        });
    }

    @Override
    public Observable<List<CommentData>> getListofCommentsOnPost(int postid) {
        Log.d("imagelogs","getListofCommentsOnPost data source");
        return  this.caseStudyAPI.ListofCommentsOnPost(Integer.toString(postid)).map(new Func1<Response<DataResponse<CommentsListResponse>>, CommentsListResponse>() {
            @Override
            public CommentsListResponse call(Response<DataResponse<CommentsListResponse>> caseResponseResponse) {
                Log.d("imagelogs","caseStudyList call source");
                return caseResponseResponse.body().getData();
            }
        }).map(new Func1<CommentsListResponse, List<CommentData>>() {
            @Override
            public List<CommentData> call(CommentsListResponse caseListResponse) {
                Log.d("imagelogs","caseStudyList list return");
                return caseListResponse.getData();
            }
        });
    }

    @Override
    public Observable<String> uploadCaseDetail(RequestBody requestCaseTypeID,RequestBody requestCaseDesc1, RequestBody requestCaseDesc2, MultipartBody.Part imageBody) {
        Log.d("uploadlogs","detail uploaded in uploadCaseDetail  data source view");
        Log.d("uploadlogs","id==="+requestCaseTypeID.toString());
        Log.d("uploadlogs","desc==="+requestCaseDesc2.toString());


        return this.caseStudyAPI.uploadcaseDetail(requestCaseTypeID,requestCaseDesc1,requestCaseDesc2,imageBody).map(new Func1<Response<DataResponse<CaseListResponse>>, CaseListResponse>() {
            @Override
            public CaseListResponse call(Response<DataResponse<CaseListResponse>> caseResponseResponse) {
                Log.d("uploadlogs","comment on post response=="+caseResponseResponse.body().getData().getMessage());
                return caseResponseResponse.body().getData();
            }
        }).map(new Func1<CaseListResponse, String>() {
            @Override
            public String call(CaseListResponse caseListResponse) {
                Log.d("uploadlogs","comment on post response=="+caseListResponse.getMessage());
                return caseListResponse.getMessage();
            }
        });
    }

    @Override
    public Observable<List<CaseItem>> getCaseItemDetail(int postId) {
        Log.d("loginresponse","getCaseItemDetail data source");
        return  this.caseStudyAPI.getCaseItemDetail(Integer.toString(postId)).map(new Func1<Response<DataResponse<CaseListResponse>>, CaseListResponse>() {
            @Override
            public CaseListResponse call(Response<DataResponse<CaseListResponse>> caseResponseResponse) {
                Log.d("loginresponse","caseStudyList call source");
                return caseResponseResponse.body().getData();
            }
        }).map(new Func1<CaseListResponse, List<CaseItem>>() {
            @Override
            public List<CaseItem> call(CaseListResponse caseListResponse) {
                Log.d("loginresponse","caseStudyList list return");
                return caseListResponse.getData();
            }
        });
    }


}
