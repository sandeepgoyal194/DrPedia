package com.softmine.drpedia.home.domain.datasource;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.softmine.drpedia.getToken.model.LoginResponse;
import com.softmine.drpedia.home.model.BookmarkItem;
import com.softmine.drpedia.home.model.CaseItem;
import com.softmine.drpedia.home.model.CaseListResponse;
import com.softmine.drpedia.home.model.CategoryListResponse;
import com.softmine.drpedia.home.model.CategoryMainItemResponse;
import com.softmine.drpedia.home.model.CommentData;
import com.softmine.drpedia.home.model.CommentsListResponse;
import com.softmine.drpedia.home.model.FeedBackResponse;
import com.softmine.drpedia.home.model.UserBookmarkListResponse;
import com.softmine.drpedia.home.net.CaseStudyAPI;
import com.softmine.drpedia.splash.model.UserInterestCategoryListResponse;
import com.softmine.drpedia.utils.GsonFactory;

import java.util.List;
import java.util.Map;

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
    public Observable<List<CaseItem>> myCaseStudyList() {
        Log.d("loginresponse","caseStudyList data source");
        return  this.caseStudyAPI.myCaseStudyList().map(new Func1<Response<DataResponse<CaseListResponse>>, CaseListResponse>() {
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
    public Observable<List<BookmarkItem>> getBookmarklist() {
        return  this.caseStudyAPI.getBookmarkList().map(new Func1<Response<DataResponse<UserBookmarkListResponse>>, UserBookmarkListResponse>() {
            @Override
            public UserBookmarkListResponse call(Response<DataResponse<UserBookmarkListResponse>> caseResponseResponse) {
                Log.d("bookmarkresponse","caseStudyList call source");
                return caseResponseResponse.body().getData();
            }
        }).map(new Func1<UserBookmarkListResponse, List<BookmarkItem>>() {
            @Override
            public List<BookmarkItem> call(UserBookmarkListResponse userBookmarkListResponse) {
                Log.d("bookmarkresponse","caseStudyList list return");
                return userBookmarkListResponse.getBookmarkData();
            }
        });
    }

    @Override
    public Observable<String> likeorUnlikeCaseStudy(RequestParams requestParams) {



        Log.d("ItemDetail","image liked in likeorUnlikeCaseStudy  data source view");
        return this.caseStudyAPI.likeCaseStudy(requestParams.getParameters()).map(new Func1<Response<DataResponse<CaseListResponse>>, CaseListResponse>() {
            @Override
            public CaseListResponse call(Response<DataResponse<CaseListResponse>> caseResponseResponse) {

                Log.d("ItemDetail","like data=="+caseResponseResponse.body().getData().getMessage());
                return caseResponseResponse.body().getData();
            }
        }).map(new Func1<CaseListResponse, String>() {
            @Override
            public String call(CaseListResponse caseListResponse) {
                Log.d("ItemDetail","like message=="+caseListResponse.getMessage());
                return caseListResponse.getMessage();
            }
        });
    }

    @Override
    public Observable<String> bookmarkCaseStudy(RequestParams requestParams) {
        Log.d("ItemDetail","image bookmarked in bookmarkCaseStudy  data source view");
        return this.caseStudyAPI.bookmarkCaseStudy(requestParams.getParameters()).map(new Func1<Response<DataResponse<CaseListResponse>>, CaseListResponse>() {
            @Override
            public CaseListResponse call(Response<DataResponse<CaseListResponse>> caseResponseResponse) {
                Log.d("ItemDetail","bookmark data=="+caseResponseResponse.body().getData().getMessage());
                return caseResponseResponse.body().getData();
            }
        }).map(new Func1<CaseListResponse, String>() {
            @Override
            public String call(CaseListResponse caseListResponse) {
                Log.d("ItemDetail","bookmark message=="+caseListResponse.getMessage());
                return caseListResponse.getMessage();
            }
        });
    }

    @Override
    public Observable<String> uploadCommentOnPost(RequestParams requestParams) {
        Log.d("ItemDetail","image commented in uploadCommentOnPost  data source view");
        return this.caseStudyAPI.uploadCommentOnPost(requestParams.getParameters()).map(new Func1<Response<DataResponse<CaseListResponse>>, CaseListResponse>() {
            @Override
            public CaseListResponse call(Response<DataResponse<CaseListResponse>> caseResponseResponse) {
                Log.d("ItemDetail","comment on post response=="+caseResponseResponse.body().getData().getMessage());
                return caseResponseResponse.body().getData();
            }
        }).map(new Func1<CaseListResponse, String>() {
            @Override
            public String call(CaseListResponse caseListResponse) {
                Log.d("ItemDetail","comment on post response=="+caseListResponse.getMessage());
                return caseListResponse.getMessage();
            }
        });
    }

    @Override
    public Observable<List<CommentData>> getListofCommentsOnPost(int postid) {
        Log.d("ItemDetail","getListofCommentsOnPost data source");
        return  this.caseStudyAPI.ListofCommentsOnPost(Integer.toString(postid)).map(new Func1<Response<DataResponse<CommentsListResponse>>, CommentsListResponse>() {
            @Override
            public CommentsListResponse call(Response<DataResponse<CommentsListResponse>> caseResponseResponse) {
                Log.d("ItemDetail","caseStudyList call source");
                return caseResponseResponse.body().getData();
            }
        }).map(new Func1<CommentsListResponse, List<CommentData>>() {
            @Override
            public List<CommentData> call(CommentsListResponse caseListResponse) {
                Log.d("ItemDetail","caseStudyList list return");
                return caseListResponse.getData();
            }
        });
    }

    @Override
    public Observable<String> uploadCaseDetail(Map<String, RequestBody> partMap, List<MultipartBody.Part> files) {
        Log.d("uploadlogs","uploadCaseDetail in data source view");
        Log.d("uploadlogs","data map size==="+partMap.size());
        Log.d("uploadlogs","list size==="+files.size());
        return this.caseStudyAPI.uploadcaseDetail(partMap,files).map(new Func1<Response<DataResponse<CaseListResponse>>, CaseListResponse>() {
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
        Log.d("ItemDetail","getCaseItemDetail data source");
        return  this.caseStudyAPI.getCaseItemDetail(Integer.toString(postId)).map(new Func1<Response<DataResponse<CaseListResponse>>, CaseListResponse>() {
            @Override
            public CaseListResponse call(Response<DataResponse<CaseListResponse>> caseResponseResponse) {
                Log.d("ItemDetail","caseStudyList call source");
                return caseResponseResponse.body().getData();
            }
        }).map(new Func1<CaseListResponse, List<CaseItem>>() {
            @Override
            public List<CaseItem> call(CaseListResponse caseListResponse) {
                Log.d("ItemDetail","caseStudyList list return");
                return caseListResponse.getData();
            }
        });
    }

    @Override
    public Observable<LoginResponse> getUserProfile() {
        return null;
    }

    @Override
    public Observable<LoginResponse> updateUserProfile(String userid, String userData) {


        JsonElement element = GsonFactory.getGson().fromJson(userData, JsonElement.class);
        JsonObject jsonObject = element.getAsJsonObject();
        Log.d("edituserprofile","data sent===");
        Log.d("edituserprofile",jsonObject.toString());
        return caseStudyAPI.updateProfile(userid , jsonObject).map(new Func1<Response<DataResponse<LoginResponse>>, LoginResponse>() {
            @Override
            public LoginResponse call(Response<DataResponse<LoginResponse>> dataResponseResponse) {

                LoginResponse loginResponse = dataResponseResponse.body().getData();

                Log.d("edituserprofile","respopnse received");
                Log.d("edituserprofile","api key==="+loginResponse.getAuthToken());
                Log.d("edituserprofile","message==="+loginResponse.getMessage());
                Log.d("edituserprofile","name==="+loginResponse.getList().get(0).getName());
                Log.d("edituserprofile","user id==="+loginResponse.getList().get(0).getUserid());
                Log.d("edituserprofile","photo url==="+loginResponse.getList().get(0).getPhotoUrl());
                Log.d("edituserprofile","email id==="+loginResponse.getList().get(0).getEmailid());
                Log.d("edituserprofile","gender==="+loginResponse.getList().get(0).getGender());
                Log.d("edituserprofile","DOB==="+loginResponse.getList().get(0).getDob());
                return dataResponseResponse.body().getData();
            }
        });
    }

    @Override
    public Observable<String> reportFeedback(String feedbackBody) {
        Log.d("feedbacklog","ReportFeedback dat source");
        JsonElement element = GsonFactory.getGson().fromJson(feedbackBody, JsonElement.class);
        JsonObject jsonObject = element.getAsJsonObject();
        return this.caseStudyAPI.reportFeedback(jsonObject).map(new Func1<Response<DataResponse<FeedBackResponse>>, FeedBackResponse>() {
            @Override
            public FeedBackResponse call(Response<DataResponse<FeedBackResponse>> feedbackResponse) {

                Log.d("feedbacklog","like data=="+feedbackResponse.body().getData().getMessage());
                return feedbackResponse.body().getData();
            }
        }).map(new Func1<FeedBackResponse, String>() {
            @Override
            public String call(FeedBackResponse feedBackResponse) {
                Log.d("feedbacklog","like message=="+feedBackResponse.getMessage());
                return feedBackResponse.getMessage();
            }
        });
    }

    @Override
    public Observable<List<CategoryMainItemResponse>> categoryList() {
        Log.d("loginresponse","caseStudyList data source");
        return  this.caseStudyAPI.categoryList().map(new Func1<Response<DataResponse<CategoryListResponse>>, CategoryListResponse>() {
            @Override
            public CategoryListResponse call(Response<DataResponse<CategoryListResponse>> categoryListResponse) {
                Log.d("loginresponse","caseStudyList call source");
                return categoryListResponse.body().getData();
            }
        }).map(new Func1<CategoryListResponse, List<CategoryMainItemResponse>>() {
            @Override
            public List<CategoryMainItemResponse> call(CategoryListResponse caseListResponse) {
                Log.d("loginresponse","caseStudyList list return");
                return caseListResponse.getData();
            }
        });
    }

    @Override
    public Observable<String> createUserInterest(String userInterestTypes) {

        JsonElement element = GsonFactory.getGson().fromJson(userInterestTypes, JsonElement.class);
        JsonObject jsonObject = element.getAsJsonObject();
        return this.caseStudyAPI.createUserInterest(jsonObject).map(new Func1<Response<DataResponse<FeedBackResponse>>, FeedBackResponse>() {
            @Override
            public FeedBackResponse call(Response<DataResponse<FeedBackResponse>> feedbackResponse) {

                Log.d("subTypePos","like data=="+feedbackResponse.body().getData().getMessage());
                return feedbackResponse.body().getData();
            }
        }).map(new Func1<FeedBackResponse, String>() {
            @Override
            public String call(FeedBackResponse feedBackResponse) {
                Log.d("subTypePos","like message=="+feedBackResponse.getMessage());
                return feedBackResponse.getMessage();
            }
        });
    }

    @Override
    public Observable<List<CategoryMainItemResponse>> getUserInterestCount() {
        Log.d("loginresponse","caseStudyList data source");
        return  this.caseStudyAPI.getUserInterestCategoryList().map(new Func1<Response<DataResponse<UserInterestCategoryListResponse>>, UserInterestCategoryListResponse>() {
            @Override
            public UserInterestCategoryListResponse call(Response<DataResponse<UserInterestCategoryListResponse>> categoryListResponse) {
                Log.d("loginresponse","caseStudyList call source");
                return categoryListResponse.body().getData();
            }
        }).map(new Func1<UserInterestCategoryListResponse, List<CategoryMainItemResponse>>() {
            @Override
            public List<CategoryMainItemResponse> call(UserInterestCategoryListResponse caseListResponse) {
                Log.d("loginresponse","caseStudyList list return");
                return caseListResponse.getData();
            }
        });
    }


}
