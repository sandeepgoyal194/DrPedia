package com.softmine.drpedia.home.domain.usecases;

import android.util.Log;

import com.softmine.drpedia.home.domain.repositry.ICaseStudyRepository;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import frameworks.network.usecases.UseCase;
import rx.Observable;

public class CreateUserInterestUseCase extends UseCase<String> {

    private final ICaseStudyRepository getCaseRepo;
    public static final String CREATE_USER_INTEREST = "CREATE_USER_INTEREST";


    @Inject
    CreateUserInterestUseCase(ICaseStudyRepository getCaseRepo)
    {
        this.getCaseRepo = getCaseRepo;
    }

    public static RequestParams createRequestParams(String user_interest_types) {
        Log.d("subTypePos","ReportFeedback params pressed");
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(CREATE_USER_INTEREST,user_interest_types);
        return requestParams;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        return this.getCaseRepo.createUserInterest(requestParams.getString(CREATE_USER_INTEREST , null));
    }
}
