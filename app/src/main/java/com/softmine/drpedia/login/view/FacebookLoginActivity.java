package com.softmine.drpedia.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import frameworks.appsession.UserInfo;

public class FacebookLoginActivity extends AppCompatActivity {

    int FACEBOOK_LOGIN_REQUEST_CODE = 21;
    int FACEBOOK_LOGIN_RESPONSE_OK = 22;
    int FACEBOOK_LOGIN_RESPONSE_FAILS = 23;


    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    AccessToken accessToken;

    //Facebook login button
    private FacebookCallback<LoginResult> callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Get Facebook Access");
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                accessToken = newToken;
                //addProfile(accessToken.getToken());
                Log.d("loginresponse","changed access token==="+accessToken.getToken());
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {


            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
                Log.d("loginresponse","access token==="+accessToken.getToken());
                if(accessToken!=null)
                {
                    Log.d("loginresponse","token successful");
                    addProfile(accessToken.getToken());
                }
                else
                {
                    Log.d("loginresponse","else part");
                    setResult(FACEBOOK_LOGIN_RESPONSE_FAILS);
                }

            }

            @Override
            public void onCancel() {
                Log.d("loginresponse","called fail");
                setResult(FACEBOOK_LOGIN_RESPONSE_FAILS);
                finish();
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("loginresponse","called onError");
                Log.d("loginresponse",e.getMessage());
                Log.d("loginresponse",e.toString());
               // Log.d("loginresponse",e.);
                e.printStackTrace();
            }
        };

        LoginManager.getInstance().registerCallback(callbackManager, callback);
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends","email"));
    }


    private void addProfile(final String token)
    {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try
                        {
                            //UserInfo userInfo = null;
                            Profile profile = Profile.getCurrentProfile();
                            if(profile!=null)
                            {
                                Log.d("profile","name=="+profile.getName());
                                Log.d("profile","id=="+profile.getId());
                                Log.d("profile","photo url=="+profile.getProfilePictureUri(100,100).toString());
                                Log.d("profile","email=="+object.getString("email"));
                               /* userInfo = new UserInfo();
                                userInfo.setName(profile.getName());
                                userInfo.setUser_id(profile.getId());
                                userInfo.setProfile_picture(profile.getProfilePictureUri(300,300).toString());
                                userInfo.setEmail(object.getString("email"));
                                userInfo.setToken(token);*/
                            }
                            if(token!=null)
                            {
                                Log.d("profile","not null");
                                Intent userIntent = new Intent();
                                userIntent.putExtra("authToken", token);
                                setResult(FACEBOOK_LOGIN_RESPONSE_OK, userIntent);
                            }
                            else {
                                Log.d("profile","null");
                                setResult(FACEBOOK_LOGIN_RESPONSE_FAILS);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        finish();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("loginresponse","called");
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager.onActivityResult(requestCode, resultCode, data)) {
            Log.d("loginresponse","called1");
            return;
        }
        else {
            return;
        }
    }


}
