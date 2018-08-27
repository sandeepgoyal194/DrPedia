package com.softmine.drpedia.home.domain.interceptor;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.UnknownHostException;

import frameworks.network.interceptor.AppBaseInterceptor;
import frameworks.network.model.DataResponse;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ResponseInterceptor<T> extends AppBaseInterceptor {

    private static final int BYTE_COUNT = 2048;
    @Override
    protected Response getResponse(Chain chain, Request request) throws IOException {
        try {
            Response response = chain.proceed(request);
            if(response != null) {

                ResponseBody responseBody = response.peekBody(BYTE_COUNT);
                String responseBodyString = responseBody.string();
                Log.d("loginlogs","response===="+responseBodyString);
                Gson gson = new Gson();
                DataResponse<T> dataResponse = null;
                try {
                    Type collectionType = new TypeToken<DataResponse<T>>(){}.getType();
                    dataResponse = gson.fromJson(responseBodyString, collectionType);
                } catch (JsonSyntaxException e) { // the json might not be TkpdResponseError instance, so just return it
                    return response;
                }
                if (dataResponse == null) { // no error object
                    return response;
                }
                return createNewResponse(response,gson.toJson(dataResponse));
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
