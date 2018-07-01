package com.softmine.drpedia.home.domain.interceptor;

import android.util.Log;

import java.io.IOException;

import javax.inject.Inject;

import frameworks.appsession.AppSessionManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {

    @Inject
    AppSessionManager appSessionManager;

    @Inject
    public RequestInterceptor(AppSessionManager appSessionManager)
    {
        this.appSessionManager = appSessionManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Log.d("loginresponse","request interceptor called");

        Request request = chain.request().newBuilder().addHeader("DRPEDIA_TOKEN", appSessionManager.getSession().getApi_key()).build();
        Log.d("loginresponse","token from preference====="+appSessionManager.getSession().getApi_key());
        return chain.proceed(request);
    }
}
