package com.softmine.drpedia.home.net;

import com.softmine.drpedia.home.model.CaseListResponse;
import com.softmine.drpedia.home.model.CommentsListResponse;

import java.util.Map;

import frameworks.network.model.DataResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

public interface CaseStudyAPI {

    @GET("posts")
    public Observable<Response<DataResponse<CaseListResponse>>> caseStudyList();

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
    public Observable<Response<DataResponse<CaseListResponse>>> uploadcaseDetail(@Part("postType_id") RequestBody caseTypeID ,@Part("short_description") RequestBody shortDesc,@Part("long_description") RequestBody longDesc, @Part MultipartBody.Part file);

    @GET("posts/post/{postid}")
    public Observable<Response<DataResponse<CaseListResponse>>> getCaseItemDetail(@Path("postid") String postid);


}
