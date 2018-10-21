package com.softmine.drpedia.home.domain.interceptor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.softmine.drpedia.CaseStudyAppApplication;
import com.softmine.drpedia.exception.NetworkConnectionException;

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

    private boolean isThereInternetConnection() {
        boolean isConnected;

        ConnectivityManager connectivityManager =
                (ConnectivityManager) CaseStudyAppApplication.getParentApplication().getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        return isConnected;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Log.d("loginresponse","request interceptor called");

        if(!isThereInternetConnection())
        {
            Log.d("loginresponse","netwrok not connected");
            throw new NetworkConnectionException();
        }
        Request request = chain.request().newBuilder().addHeader("DRPEDIA_TOKEN", appSessionManager.getSession().getApi_key()).build();
        Log.d("loginresponse","token from preference====="+appSessionManager.getSession().getApi_key());
        return chain.proceed(request);

    }
}
