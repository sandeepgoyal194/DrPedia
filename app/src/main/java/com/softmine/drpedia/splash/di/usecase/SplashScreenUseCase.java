package com.softmine.drpedia.splash.di.usecase;

import android.util.Log;

import com.softmine.drpedia.home.domain.repositry.ICaseStudyRepository;
import com.softmine.drpedia.home.model.CategoryMainItemResponse;

import java.util.List;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import frameworks.network.usecases.UseCase;
import rx.Observable;

public class SplashScreenUseCase extends UseCase<List<CategoryMainItemResponse>> {

    private final ICaseStudyRepository getCaseRepo;

    @Inject
    SplashScreenUseCase(ICaseStudyRepository getCaseRepo)
    {
        this.getCaseRepo = getCaseRepo;
    }

    @Override
    public Observable<List<CategoryMainItemResponse>> createObservable(RequestParams requestParams) {
        Log.d("splashresponse"," SplashScreenUseCase called");
        return this.getCaseRepo.getUserInterestCount();
    }


}
