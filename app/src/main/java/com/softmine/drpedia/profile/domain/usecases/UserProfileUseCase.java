package com.softmine.drpedia.profile.domain.usecases;

import android.util.Log;

import com.softmine.drpedia.getToken.model.LoginResponse;
import com.softmine.drpedia.home.domain.repositry.ICaseStudyRepository;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import frameworks.network.usecases.UseCase;
import rx.Observable;

public class UserProfileUseCase extends UseCase<LoginResponse> {

    private final ICaseStudyRepository getCaseRepo;

    @Inject
    UserProfileUseCase(ICaseStudyRepository getCaseRepo)
    {
        this.getCaseRepo = getCaseRepo;
    }


    @Override
    public Observable<LoginResponse> createObservable(RequestParams requestParams) {
        Log.d("userprofile"," createObservable called");
        return this.getCaseRepo.getUserProfile();
    }
}
