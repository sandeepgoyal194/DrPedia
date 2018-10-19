package com.softmine.drpedia.home.domain.usecases;

import com.softmine.drpedia.home.domain.repositry.ICaseStudyRepository;
import com.softmine.drpedia.home.model.CategoryMainItemResponse;

import java.util.List;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import frameworks.network.usecases.UseCase;
import rx.Observable;

public class CategoryListUseCase extends UseCase<List<CategoryMainItemResponse>> {

    private final ICaseStudyRepository getCaseRepo;

    @Inject
    CategoryListUseCase(ICaseStudyRepository getCaseRepo)
    {
        this.getCaseRepo = getCaseRepo;
    }

    @Override
    public Observable<List<CategoryMainItemResponse>> createObservable(RequestParams requestParams) {
        return this.getCaseRepo.categoryList();
    }
}
