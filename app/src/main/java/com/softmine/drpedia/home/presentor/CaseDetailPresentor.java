package com.softmine.drpedia.home.presentor;

import android.support.annotation.NonNull;
import android.util.Log;


import com.softmine.drpedia.exception.DataException;
import com.softmine.drpedia.exception.DefaultErrorBundle;
import com.softmine.drpedia.exception.ErrorBundle;
import com.softmine.drpedia.exception.ErrorMessageFactory;
import com.softmine.drpedia.exception.NetworkConnectionException;
import com.softmine.drpedia.home.CaseDetailView;
import com.softmine.drpedia.home.domain.usecases.GetCaseDetailUseCase;
import com.softmine.drpedia.home.domain.usecases.GetCaseStudyBookmarkUseCase;
import com.softmine.drpedia.home.domain.usecases.GetCaseStudyLikeUseCase;
import com.softmine.drpedia.home.domain.usecases.GetCommentOnPostUseCase;
import com.softmine.drpedia.home.domain.usecases.UploadCommentUseCase;
import com.softmine.drpedia.home.model.CaseItem;
import com.softmine.drpedia.home.model.CommentData;

import org.json.JSONException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import frameworks.network.model.ResponseException;
import frameworks.network.usecases.RequestParams;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public class CaseDetailPresentor implements ICaseDetailPresentor {

    private CaseDetailView caseDetailView;

    private GetCaseStudyLikeUseCase getCaseStudyLikeUseCase;
    private UploadCommentUseCase uploadCommentUseCase;
    private GetCaseStudyBookmarkUseCase getCaseStudyBookmarkUseCase;
    private GetCommentOnPostUseCase getCommentOnPostUseCase;
    private GetCaseDetailUseCase getCaseDetailUseCase;

    @Inject
    public CaseDetailPresentor(GetCaseDetailUseCase getCaseDetailUseCase, GetCaseStudyLikeUseCase getCaseStudyLikeUseCase,GetCaseStudyBookmarkUseCase getCaseStudyBookmarkUseCase,UploadCommentUseCase uploadCommentUseCase , GetCommentOnPostUseCase getCommentOnPostUseCase)
    {
        this.getCaseStudyLikeUseCase = getCaseStudyLikeUseCase;
        this.getCaseStudyBookmarkUseCase = getCaseStudyBookmarkUseCase;
        this.uploadCommentUseCase = uploadCommentUseCase;
        this.getCommentOnPostUseCase = getCommentOnPostUseCase;
        this.getCaseDetailUseCase = getCaseDetailUseCase;
    }

    public void setView(@NonNull CaseDetailView view) {
        this.caseDetailView = view;
    }

    private  void showViewLoading()
    {
        this.caseDetailView.showProgressBar();
    }


    private void hideViewLoading() {
        Log.d("ItemDetail","hide loading====================");

        this.caseDetailView.hideProgressBar();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(caseDetailView.getContext(),
                errorBundle.getException());
        caseDetailView.showSnackBar(errorMessage);
    }

    @Override
    public void doLikeorUnlikePost(String likeStatus, int postID) {
        Log.d("ItemDetail","image liked in presentor view");
        //   Toast.makeText(this.caseDetailView.getContext(),"image liked in presentor view",Toast.LENGTH_LONG).show();
        RequestParams requestParams =  GetCaseStudyLikeUseCase.createRequestParams(likeStatus,postID);
        CaseDetailPresentor.this.showViewLoading();
        this.getCaseStudyLikeUseCase.execute(requestParams,new Subscriber<String>() {
            @Override
            public void onCompleted() {

                Log.d("ItemDetail","image liked in onCompleted view");
                CaseDetailPresentor.this.hideViewLoading();
            }

            @Override
            public void onError(Throwable e) {
                Log.d("ItemDetail","error occured while like image");
                //    CaseDetailPresentor.this.caseDetailView.updateLikeOrUnlikePost(true);
                //   Toast.makeText(CaseDetailPresentor.this.caseDetailView.getContext(),"image liked in onCompleted view",Toast.LENGTH_LONG).show();
                e.printStackTrace();
                CaseDetailPresentor.this.hideViewLoading();
               // CaseDetailPresentor.this.caseDetailView.showSnackBar("Error occured while liking Post");
                CaseDetailPresentor.this.getErrorMessage(e , "Error occured while liking Post");
            }

            @Override
            public void onNext(String string) {
                CaseDetailPresentor.this.caseDetailView.updateLikeOrUnlikePost(true);
                Log.d("ItemDetail","image liked in onNext view==========="+string);

            }
        });
    }

    @Override
    public void doBookmarkPost(String bookmarkStatus, int postID) {
        Log.d("ItemDetail","image bookmarked in presentor view");
        RequestParams requestParams =  GetCaseStudyBookmarkUseCase.createRequestParams(bookmarkStatus,postID);
        CaseDetailPresentor.this.showViewLoading();
        this.getCaseStudyBookmarkUseCase.execute(requestParams,new Subscriber<String>() {
            @Override
            public void onCompleted() {
                // Call MainActivity to show list of feedbacks
                Log.d("ItemDetail","image bookmarked in onCompleted view");
                CaseDetailPresentor.this.hideViewLoading();
                //  Toast.makeText(CaseDetailPresentor.this.caseDetailView.getContext(),"image liked in onCompleted view",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable e) {

                Log.d("ItemDetail","image bookmarked in onError view");
                CaseDetailPresentor.this.hideViewLoading();
                e.printStackTrace();
               // CaseDetailPresentor.this.caseDetailView.showSnackBar("Error occured while bookmark Post");
                CaseDetailPresentor.this.getErrorMessage(e , "Error occured while bookmark Post");
                //   Toast.makeText(CaseDetailPresentor.this.caseDetailView.getContext(),"image liked in onCompleted view",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNext(String string) {
                Log.d("ItemDetail","image bookmarked in onNext view");
                CaseDetailPresentor.this.caseDetailView.updateBookmarkUserPost(true);
            }
        });
    }

    @Override
    public void doUploadCommentOnPost(String comment , int postID) {
        Log.d("ItemDetail","comment on post in presentor view");
        RequestParams requestParams = UploadCommentUseCase.createRequestParams(comment,postID);
        CaseDetailPresentor.this.showViewLoading();
        this.uploadCommentUseCase.execute(requestParams,new Subscriber<String>() {
            @Override
            public void onCompleted() {
                // Call MainActivity to show list of feedbacks
                Log.d("ItemDetail","comment on post in onCompleted view");
                CaseDetailPresentor.this.hideViewLoading();
                //  Toast.makeText(CaseDetailPresentor.this.caseDetailView.getContext(),"image liked in onCompleted view",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable e) {

                Log.d("ItemDetail","comment on post in onError view");
                CaseDetailPresentor.this.hideViewLoading();
                e.printStackTrace();
               // CaseDetailPresentor.this.caseDetailView.showSnackBar("Error occured while uploading comment on Post");
                CaseDetailPresentor.this.getErrorMessage(e , "Error occured while uploading comments on Post");
                //   Toast.makeText(CaseDetailPresentor.this.caseDetailView.getContext(),"image liked in onCompleted view",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNext(String string) {
                Log.d("ItemDetail","comment on post in onNext view");
                CaseDetailPresentor.this.caseDetailView.UpdateCommentOnPost(true);
            }
        });

    }

    @Override
    public void loadAllComments(int postid) {
        Log.d("ItemDetail","loadAllComments in presentor view");
        Log.d("ItemDetail","case id===="+postid);
        RequestParams requestParams =  GetCommentOnPostUseCase.createRequestParams(postid);
        CaseDetailPresentor.this.showViewLoading();
        this.getCommentOnPostUseCase.execute(requestParams,new Subscriber<List<CommentData>>() {
            @Override
            public void onCompleted() {
                Log.d("ItemDetail","onCompleted");
                CaseDetailPresentor.this.hideViewLoading();
            }

            @Override
            public void onError(Throwable e) {

                Log.d("ItemDetail","onError in loadComments");
                e.printStackTrace();
                CaseDetailPresentor.this.hideViewLoading();
                //CaseDetailPresentor.this.caseDetailView.showSnackBar("Error occured while loading comments on Post");
                CaseDetailPresentor.this.getErrorMessage(e , "Error occured while loading comments on Post");
            }

            @Override
            public void onNext(List<CommentData> commentData) {
                Log.d("ItemDetail","onNext");
                CaseDetailPresentor.this.showAllCommentsInView(commentData);
            }
        });
    }

    private void showAllCommentsInView(Collection<CommentData> usersCollection) {
      /*  final Collection<CaseItem> userModelsCollection =
                this.userModelDataMapper.transform(usersCollection);*/

        this.caseDetailView.setAllCommentsOnPost(usersCollection);
    }

    @Override
    public void getCaseItemDetail(int postid) {

        RequestParams requestParams =  GetCaseDetailUseCase.createRequestParams(postid);
        CaseDetailPresentor.this.showViewLoading();
        this.getCaseDetailUseCase.execute(requestParams,new Subscriber<List<CaseItem>>() {
            @Override
            public void onCompleted() {
                Log.d("ItemDetail","onCompleted");
                CaseDetailPresentor.this.hideViewLoading();

            }

            @Override
            public void onError(Throwable e) {
                Log.d("ItemDetail","onError");
                CaseDetailPresentor.this.hideViewLoading();
                e.printStackTrace();
                CaseDetailPresentor.this.getErrorMessage(e , "Error occured while updating post");
            }

            @Override
            public void onNext(List<CaseItem> caseList) {
                Log.d("ItemDetail","onNext");
                CaseDetailPresentor.this.showCaseItemDetail(caseList);
            }
        });
    }

    private void showCaseItemDetail(List<CaseItem> caseList)
    {
        this.caseDetailView.updateCaseItemDetail(caseList.get(0));
    }


    public void getErrorMessage(Throwable e , String message)
    {
        if(e instanceof IOException)
        {
            if(e instanceof HttpException)
            {
                Log.d("bookmarkresponse","exception code  "+((HttpException)e).code());
                CaseDetailPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
            }
            else if(e instanceof ResponseException)
            {
                CaseDetailPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
            }
            else if(e instanceof NetworkConnectionException)
            {
                Log.d("loginresponse","other issues");
                CaseDetailPresentor.this.showErrorMessage(new DefaultErrorBundle(new NetworkConnectionException()));
            }
            else
            {
                Log.d("loginresponse", "other issue");
                CaseDetailPresentor.this.showErrorMessage(new DefaultErrorBundle(new DataException(message)));
            }
        }
        else
        {
            if(e instanceof JSONException) {
                Log.d("loginresponse", "Json Parsing exception");
                CaseDetailPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
            }
            else if(e instanceof HttpException)
            {
                Log.d("loginresponse", "Http exception issue");
                CaseDetailPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
            }
            else
            {
                Log.d("loginresponse", "other issue");
                CaseDetailPresentor.this.showErrorMessage(new DefaultErrorBundle(new DataException(message)));
            }
        }
    }

}
