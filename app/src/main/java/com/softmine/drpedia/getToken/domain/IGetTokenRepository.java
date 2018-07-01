package com.softmine.drpedia.getToken.domain;

import com.softmine.drpedia.getToken.model.LoginResponse;

import frameworks.network.usecases.RequestParams;
import rx.Observable;
/**
 * Created by sandeep on 5/5/18.
 */
public interface IGetTokenRepository {
    public Observable<LoginResponse> getToken(RequestParams requestParams);
}
