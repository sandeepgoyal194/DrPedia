package com.softmine.drpedia.getToken.data.net;


import com.softmine.drpedia.getToken.model.LoginResponse;

import java.util.Map;

import frameworks.network.model.DataResponse;
import retrofit2.Response;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by sandeepgoyal on 03/05/18.
 */

public interface LoginAPI {

    @POST(LoginAPIURL.Login_API)
    public Observable<Response<DataResponse<LoginResponse>>> login(@QueryMap Map<String,Object> params);
}
