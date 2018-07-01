package com.softmine.drpedia.getToken.data.repository;


import com.softmine.drpedia.getToken.data.repository.datasource.GetTokenDataFactory;
import com.softmine.drpedia.getToken.domain.IGetTokenRepository;
import com.softmine.drpedia.getToken.model.LoginResponse;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import rx.Observable;
/**
 * Created by sandeep on 4/5/18.
 */
public class GetTokenDataRepository implements IGetTokenRepository {

    GetTokenDataFactory getTokenDataFactory;

    @Inject
    public GetTokenDataRepository(GetTokenDataFactory getTokenDataFactory) {
        this.getTokenDataFactory = getTokenDataFactory;
    }

    @Override
    public Observable<LoginResponse> getToken(RequestParams requestParams) {
        return getTokenDataFactory.getToken(requestParams);
    }
}
