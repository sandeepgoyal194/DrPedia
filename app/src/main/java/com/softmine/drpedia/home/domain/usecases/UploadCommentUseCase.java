package com.softmine.drpedia.home.domain.usecases;

import android.util.Log;

import com.softmine.drpedia.home.domain.repositry.ICaseStudyRepository;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import frameworks.network.usecases.UseCase;
import rx.Observable;

public class UploadCommentUseCase extends UseCase<String> {

    public static final String comment_post = "comment";
    public static final String postID = "post_id";
    private final ICaseStudyRepository getCaseRepo;

    @Inject
    UploadCommentUseCase(ICaseStudyRepository getCaseRepo)
    {
        this.getCaseRepo = getCaseRepo;
    }

    public static RequestParams createRequestParams(String comment,int postId) {
        Log.d("imagelogs","comment on image in createRequestParams view");
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(comment_post,comment);
        requestParams.putInt(postID,postId);
        return requestParams;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        Log.d("imagelogs","comment on image in createObservable view");
        return this.getCaseRepo.uploadCommentOnPost(requestParams);
    }
}
