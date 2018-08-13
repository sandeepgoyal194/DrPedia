package com.softmine.drpedia.home.domain.usecases;

import com.softmine.drpedia.home.domain.repositry.ICaseStudyRepository;
import com.softmine.drpedia.home.model.CaseItem;

import java.util.List;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import frameworks.network.usecases.UseCase;
import rx.Observable;

public class GetMyCaseStudyListUseCase extends UseCase<List<CaseItem>> {

    private final ICaseStudyRepository getCaseRepo;

    @Inject
    GetMyCaseStudyListUseCase(ICaseStudyRepository getCaseRepo)
    {
        this.getCaseRepo = getCaseRepo;
    }

    @Override
    public Observable<List<CaseItem>> createObservable(RequestParams requestParams) {
        return this.getCaseRepo.myCaselist();
    }
}