package com.softmine.drpedia.home.domain.usecases;


import com.softmine.drpedia.home.domain.repositry.ICaseStudyRepository;
import com.softmine.drpedia.home.model.BookmarkItem;

import java.util.List;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import frameworks.network.usecases.UseCase;
import rx.Observable;

public class GetBookmarkListUseCase extends UseCase<List<BookmarkItem>> {

    private final ICaseStudyRepository getCaseRepo;

    @Inject
    GetBookmarkListUseCase(ICaseStudyRepository getCaseRepo)
    {
        this.getCaseRepo = getCaseRepo;
    }

    @Override
    public Observable<List<BookmarkItem>> createObservable(RequestParams requestParams) {
        return this.getCaseRepo.getBookmarklist();
    }
}
