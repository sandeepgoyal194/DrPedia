package com.softmine.drpedia.getToken.data.interceptor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.softmine.drpedia.CaseStudyAppApplication;
import com.softmine.drpedia.exception.NetworkConnectionException;
import com.softmine.drpedia.getToken.model.LoginResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.UnknownHostException;

import frameworks.network.interceptor.AppBaseInterceptor;
import frameworks.network.model.DataResponse;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
/**
 * Created by sandeep on 5/5/18.
 */
public class GetTokenInterceptor extends AppBaseInterceptor {
    private static final int BYTE_COUNT = 2048;


    private boolean isThereInternetConnection() {
        boolean isConnected;

        ConnectivityManager connectivityManager =
                (ConnectivityManager) CaseStudyAppApplication.getParentApplication().getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        return isConnected;
    }

    @Override
    protected Response getResponse(Chain chain, Request request) throws IOException {

        if(!isThereInternetConnection())
        {
            throw new NetworkConnectionException();
        }

        try {

            Log.d("loginresponse","url===="+  chain.request().url());

            Response response = chain.proceed(request);
            if(response != null) {
                Log.d("loginresponse","raw response ===="+response.body());
                ResponseBody responseBody = response.peekBody(BYTE_COUNT);
                String responseBodyString = responseBody.string();
                Log.d("loginresponse","response===="+responseBodyString);
                String token = response.header("DRPEDIA_TOKEN");
                Log.d("loginresponse","token==="+token);
                Gson gson = new Gson();
                DataResponse<LoginResponse> dataResponse = null;
                try {
                    Type collectionType = new TypeToken<DataResponse<LoginResponse>>(){}.getType();
                    dataResponse = gson.fromJson(responseBodyString, collectionType);
                } catch (JsonSyntaxException e) { // the json might not be TkpdResponseError instance, so just return it
                    return response;
                }
                if (dataResponse == null) { // no error object
                    return response;
                } else {
                    dataResponse.getData().setAuthToken(token);
                }
                return createNewResponse(response,gson.toJson(dataResponse));
            }
            else
            {
                Log.d("loginresponse","response null");
            }
            return response;
        } catch (Error e) {
            throw new UnknownHostException();
        }
    }

    protected Response createNewResponse(Response oldResponse, String oldBodyResponse) {
        ResponseBody body = ResponseBody.create(oldResponse.body().contentType(), oldBodyResponse);
        Response.Builder builder = new Response.Builder();
        builder.body(body)
                .headers(oldResponse.headers())
                .message(oldResponse.message())
                .handshake(oldResponse.handshake())
                .protocol(oldResponse.protocol())
                .cacheResponse(oldResponse.cacheResponse())
                .priorResponse(oldResponse.priorResponse())
                .code(oldResponse.code())
                .request(oldResponse.request())
                .networkResponse(oldResponse.networkResponse());
        return builder.build();
    }
}
