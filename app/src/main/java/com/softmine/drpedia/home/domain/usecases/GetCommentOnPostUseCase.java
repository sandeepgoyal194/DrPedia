package com.softmine.drpedia.home.domain.usecases;

import android.util.Log;


import com.softmine.drpedia.home.domain.repositry.ICaseStudyRepository;
import com.softmine.drpedia.home.model.CommentData;

import java.util.List;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import frameworks.network.usecases.UseCase;
import rx.Observable;

public class GetCommentOnPostUseCase extends UseCase<List<CommentData>> {

    public static final String postID = "postid";

    private final ICaseStudyRepository getCaseRepo;

    @Inject
    GetCommentOnPostUseCase(ICaseStudyRepository getCaseRepo)
    {
        this.getCaseRepo = getCaseRepo;
    }

    public static RequestParams createRequestParams(int postId) {
        Log.d("imagelogs","get comments in createRequestParams view");
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(postID,postId);
        return requestParams;
    }

    @Override
    public Observable<List<CommentData>> createObservable(RequestParams requestParams) {

        Log.d("imagelogs","get comments in createObservable view");
        Log.d("imagelogs","get case id===="+requestParams.getInt(postID,0));
        return this.getCaseRepo.getListofCommentsOnPost(requestParams.getInt(postID,0));
    }
}
