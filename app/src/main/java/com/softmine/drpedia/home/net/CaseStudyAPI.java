package com.softmine.drpedia.home.net;

import com.google.gson.JsonObject;
import com.softmine.drpedia.getToken.model.LoginResponse;
import com.softmine.drpedia.home.model.CaseListResponse;
import com.softmine.drpedia.home.model.CategoryListResponse;
import com.softmine.drpedia.home.model.CommentsListResponse;
import com.softmine.drpedia.home.model.FeedBackResponse;
import com.softmine.drpedia.home.model.UserBookmarkListResponse;

import java.util.List;
import java.util.Map;

import frameworks.network.model.DataResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import rx.Observable;

public interface CaseStudyAPI {

    @GET("posts")
    public Observable<Response<DataResponse<CaseListResponse>>> caseStudyList();

    @GET("posts/myPost")
    public Observable<Response<DataResponse<CaseListResponse>>> myCaseStudyList();

    @GET("bookmark")
    public Observable<Response<DataResponse<UserBookmarkListResponse>>> getBookmarkList();

    @FormUrlEncoded
    @POST("like/create/")
    public Observable<Response<DataResponse<CaseListResponse>>> likeCaseStudy(@FieldMap Map<String,Object> params);

    @FormUrlEncoded
    @POST("bookmark/create/")
    public Observable<Response<DataResponse<CaseListResponse>>> bookmarkCaseStudy(@FieldMap Map<String,Object> params);

    @FormUrlEncoded
    @POST("comment/create")
    public Observable<Response<DataResponse<CaseListResponse>>> uploadCommentOnPost(@FieldMap Map<String,Object> params);

    @GET("comment/{postid}")
    public Observable<Response<DataResponse<CommentsListResponse>>> ListofCommentsOnPost(@Path("postid") String postid);

    @Multipart
    @POST("posts/create")
    public Observable<Response<DataResponse<CaseListResponse>>> uploadcaseDetail(@PartMap() Map<String, RequestBody> partMap, @Part List<MultipartBody.Part> files);

    @GET("posts/post/{postid}")
    public Observable<Response<DataResponse<CaseListResponse>>> getCaseItemDetail(@Path("postid") String postid);

    @PUT("user/edit/{userid}")
    public Observable<Response<DataResponse<LoginResponse>>> updateProfile(@Path("userid") String postid , @Body JsonObject jsonObject );

    @POST("/problem/create")
    public Observable<Response<DataResponse<FeedBackResponse>>> reportFeedback(@Body JsonObject jsonObject );

    @GET("postType")
    public Observable<Response<DataResponse<CategoryListResponse>>> categoryList();

    @POST("/intrests/create")
    public Observable<Response<DataResponse<FeedBackResponse>>> createUserInterest(@Body JsonObject jsonObject );

}
