package com.softmine.drpedia.getToken.model;

import com.google.gson.annotations.SerializedName;
import com.softmine.drpedia.login.model.User;

import java.util.List;

/**
 * Created by sandeepgoyal on 04/05/18.
 */

public class LoginResponse {
    String message;

    String authToken;

    @SerializedName(value="UserDetails")
    List<User> list;

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public String toString() {
        return "message = " + message +" \nauthToken " +authToken;
    }
}
