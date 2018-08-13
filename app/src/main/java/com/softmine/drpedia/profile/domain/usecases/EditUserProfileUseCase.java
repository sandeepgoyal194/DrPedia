package com.softmine.drpedia.profile.domain.usecases;

import android.util.Log;


import com.softmine.drpedia.getToken.model.LoginResponse;
import com.softmine.drpedia.home.domain.repositry.ICaseStudyRepository;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import frameworks.network.usecases.UseCase;
import rx.Observable;

public class EditUserProfileUseCase extends UseCase<LoginResponse> {

    public static final String userID = "userid";
    public static final String body = "userbody";

    private final ICaseStudyRepository getCaseRepo;

    @Inject
    EditUserProfileUseCase(ICaseStudyRepository getCaseRepo)
    {
        this.getCaseRepo = getCaseRepo;
    }

    public static RequestParams createRequestParams(String userId, String userProfileBody) {
        Log.d("edituserprofile"," createRequestParams view");
        Log.d("edituserprofile"," user id=="+userId);
        Log.d("edituserprofile"," body=="+userProfileBody);

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(userID,userId);
        requestParams.putString(body,userProfileBody);
        return requestParams;
    }

    @Override
    public Observable<LoginResponse> createObservable(RequestParams requestParams) {
        return this.getCaseRepo.updateUserProfile(requestParams.getString(userID , null), requestParams.getString(body , null));
    }
}
