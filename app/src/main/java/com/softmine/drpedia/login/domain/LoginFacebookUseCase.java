package com.softmine.drpedia.login.domain;


import com.softmine.drpedia.getToken.domain.GetTokenUseCase;
import com.softmine.drpedia.getToken.model.LoginResponse;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import frameworks.network.usecases.UseCase;
import rx.Observable;

public class LoginFacebookUseCase extends UseCase<LoginResponse> {

    public static final String FBTOKEN = "access_token";

    public static final String userID = "userID";
    public static final String Password = "password";

    GetTokenUseCase getTokenUseCase;

    @Inject
    public LoginFacebookUseCase(GetTokenUseCase getTokenUseCase)
    {
        this.getTokenUseCase = getTokenUseCase;
    }

    @Override
    public Observable<LoginResponse> createObservable(RequestParams requestParams) {
        return getTokenUseCase.createObservable(requestParams);
    }

    public static RequestParams createRequestParams(String token) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(FBTOKEN,token);
        return requestParams;
    }
}
