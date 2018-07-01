package frameworks.network.model;

import android.text.TextUtils;

import java.io.IOException;

/**
 * Created by User on 9/15/2017.
 */

public class BaseResponseError {

    String status;
    String message;
    String statusCode;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return the String key if the json Response indicated that is an error response.
     *
     *

     */
    public String getErrorKey(){
        return statusCode;
    }

    /**
     * Check if this response error is valid
     *
     * @return
     */
    public boolean isResponseErrorValid() {
        return hasBody();
    }

    /**
     * @return if the error is filled, return true
     */
    public boolean hasBody() {
        if(message!= null && !TextUtils.isEmpty(message)) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * @return the exception from this Error
     */
    public ResponseException createException() {
        return new ResponseException(message);
    }

}
