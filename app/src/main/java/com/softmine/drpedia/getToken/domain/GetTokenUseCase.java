package com.softmine.drpedia.getToken.domain;


import com.softmine.drpedia.getToken.model.LoginResponse;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import frameworks.network.usecases.UseCase;
import rx.Observable;

/**
 * Created by sandeepgoyal on 04/05/18.
 */

public class GetTokenUseCase extends UseCase<LoginResponse> {

    IGetTokenRepository getTokenRepository;

    @Inject
    public GetTokenUseCase(IGetTokenRepository getTokenRepository) {
        this.getTokenRepository = getTokenRepository;
    }

    @Override
    public Observable<LoginResponse> createObservable(RequestParams requestParams) {
        return getTokenRepository.getToken(requestParams);
    }
}
