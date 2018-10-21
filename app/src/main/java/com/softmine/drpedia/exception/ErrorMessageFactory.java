package com.softmine.drpedia.exception;

import android.content.Context;
import android.util.Log;

import com.softmine.drpedia.R;

import org.json.JSONException;

import frameworks.network.model.ResponseException;
import retrofit2.adapter.rxjava.HttpException;

public class ErrorMessageFactory {

    private ErrorMessageFactory() {
        //empty
    }

    public static String create(Context context, Exception exception) {
        Log.d("splashresponse"," ErrorMessageFactory create called");
      //  String message = context.getString(R.string.exception_error);
        String message = null;
        Log.d("splashresponse"," ErrorMessageFactory create11 called");
        if (exception instanceof NetworkConnectionException) {
            Log.d("splashresponse"," ErrorMessageFactory create1 called");
            if(context!=null)
            message = context.getString(R.string.exception_message_no_connection);
            else
                Log.d("splashresponse"," context is null");
        } else if (exception instanceof HttpException) {
            Log.d("splashresponse"," ErrorMessageFactory create2 called");
            message = context.getString(R.string.exception_message_http);
        } else if (exception instanceof JSONException) {
            Log.d("splashresponse"," ErrorMessageFactory create3 called");
            message = context.getString(R.string.exception_message_parsing);
        }else if(exception instanceof ResponseException){
            Log.d("splashresponse"," ErrorMessageFactory create4 called");
            message = exception.getMessage();
        }
        else
        {
            Log.d("splashresponse"," ErrorMessageFactory create5 called");
            message = exception.getMessage();
        }
        Log.d("splashresponse"," exception message "+message);
        return message;
    }
}
