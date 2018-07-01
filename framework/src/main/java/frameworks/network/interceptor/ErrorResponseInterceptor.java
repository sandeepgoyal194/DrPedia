package frameworks.network.interceptor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import frameworks.network.model.BaseResponseError;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by hendry on 9/15/2017.
 */

public class ErrorResponseInterceptor implements Interceptor {
    private static final int BYTE_COUNT = 6144;
    private Class<? extends BaseResponseError> responseErrorClass;

    public ErrorResponseInterceptor(@NonNull Class<? extends BaseResponseError> responseErrorClass) {

        Log.d("errorresponse","ErrorResponseInterceptor initialized");
        this.responseErrorClass = responseErrorClass;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Log.d("errorresponse","ErrorResponseInterceptor called");
        Response response = chain.proceed(chain.request());

        ResponseBody responseBody = null;
        String responseBodyString = "";
        if (mightContainCustomError(response)) {


            responseBody = response.peekBody(BYTE_COUNT);
           // responseBody =  response.body();
            responseBodyString = responseBody.string();
            Log.d("errorresponse","ErrorResponseInterceptor body===="+responseBodyString);
            Gson gson = new Gson();
            BaseResponseError responseError = null;
            try {
                responseError = gson.fromJson(responseBodyString, responseErrorClass);
                Log.d("errorresponse","ErrorResponseInterceptor responseError==="+responseError.getMessage());
            } catch (JsonSyntaxException e) { // the json might not be TkpdResponseError instance, so just return it
                return response;
            }
            if (responseError == null) { // no error object
                return response;
            } else {
                if (responseError.hasBody()) {
                    //noinspection ConstantConditions
                    Log.d("errorresponse","ErrorResponseInterceptor responseError has body");
                    response.body().close();
                    throw responseError.createException();
                } else {
                    return response;
                }
            }
        }
        return response;
    }

    protected boolean mightContainCustomError(Response response) {
        return response != null;
    }
}
