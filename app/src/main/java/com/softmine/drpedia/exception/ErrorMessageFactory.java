package com.softmine.drpedia.exception;

import android.content.Context;

import com.softmine.drpedia.R;

import org.json.JSONException;

import frameworks.network.model.ResponseException;
import retrofit2.adapter.rxjava.HttpException;

public class ErrorMessageFactory {

    private ErrorMessageFactory() {
        //empty
    }

    public static String create(Context context, Exception exception) {

        String message = context.getString(R.string.exception_error);

        if (exception instanceof NetworkConnectionException) {
            message = context.getString(R.string.exception_message_no_connection);
        } else if (exception instanceof HttpException) {
            message = context.getString(R.string.exception_message_http);
        } else if (exception instanceof JSONException) {
            message = context.getString(R.string.exception_message_parsing);
        }else if(exception instanceof ResponseException){
            message = exception.getMessage();
        }

        return message;
    }
}
