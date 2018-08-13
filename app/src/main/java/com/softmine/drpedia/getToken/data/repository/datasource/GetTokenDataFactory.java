package com.softmine.drpedia.getToken.data.repository.datasource;

import com.softmine.drpedia.getToken.data.repository.datasource.GetTokenCloudDataSource;
import com.softmine.drpedia.getToken.model.LoginResponse;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import rx.Observable;
/**
 * Created by sandeep on 5/5/18.
 */
public class GetTokenDataFactory {

    GetTokenCloudDataSource getTokenCloudDataSource;

    @Inject
    public GetTokenDataFactory(GetTokenCloudDataSource getTokenCloudDataSource) {
        this.getTokenCloudDataSource = getTokenCloudDataSource;
    }

    public GetTokenCloudDataSource getGetTokenCloudDataSource() {
        return getTokenCloudDataSource;
    }

    public Observable<LoginResponse> getToken(RequestParams requestParams) {
        return getTokenCloudDataSource.getToken(requestParams);
    }

}
