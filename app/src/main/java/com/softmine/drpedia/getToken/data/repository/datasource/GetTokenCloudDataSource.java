package com.softmine.drpedia.getToken.data.repository.datasource;


import android.util.Log;


import com.softmine.drpedia.getToken.data.net.LoginAPI;
import com.softmine.drpedia.getToken.model.LoginResponse;

import javax.inject.Inject;

import frameworks.appsession.AppSessionManager;
import frameworks.appsession.SessionValue;
import frameworks.network.model.DataResponse;
import frameworks.network.usecases.RequestParams;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
/**
 * Created by sandeep on 5/5/18.
 */
public class GetTokenCloudDataSource {
    private final AppSessionManager appSessionManager;
    private LoginAPI loginAPI;

    @Inject
    public GetTokenCloudDataSource(LoginAPI loginAPI,AppSessionManager appSessionManager) {
        this.loginAPI = loginAPI;
        this.appSessionManager = appSessionManager;
    }

    public Observable<LoginResponse> getToken(RequestParams requestParams) {
        return loginAPI.login(requestParams.getParameters()).map(new Func1<Response<DataResponse<LoginResponse>>, LoginResponse>() {
            @Override
            public LoginResponse call(Response<DataResponse<LoginResponse>> dataResponseResponse) {
                Log.d("loginresponse","respopnse received");
                return dataResponseResponse.body().getData();
            }
        });

    }

    private Action1<? super SessionValue> saveSession() {
        return new Action1<SessionValue>() {
            @Override
            public void call(SessionValue sessionValue) {
                Log.d("loginresponse","saving token");
                appSessionManager.saveSession(sessionValue);
                Log.d("loginresponse","token saved");
            }
        };
    }
}
