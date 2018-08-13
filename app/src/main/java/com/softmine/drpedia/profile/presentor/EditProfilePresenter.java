package com.softmine.drpedia.profile.presentor;

import android.support.annotation.NonNull;
import android.util.Log;


import com.softmine.drpedia.getToken.model.LoginResponse;
import com.softmine.drpedia.login.model.User;
import com.softmine.drpedia.profile.IEditProfileView;
import com.softmine.drpedia.profile.domain.usecases.EditUserProfileUseCase;
import com.softmine.drpedia.utils.GsonFactory;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import rx.Subscriber;

public class EditProfilePresenter implements IEditProfilePresenter {

    private IEditProfileView editProfileView;
    private EditUserProfileUseCase editUserProfileUseCase;

    @Inject
    public EditProfilePresenter(EditUserProfileUseCase editUserProfileUseCase)
    {
        this.editUserProfileUseCase = editUserProfileUseCase;
    }

    public void setView(@NonNull IEditProfileView view) {
        this.editProfileView = view;
    }

    @Override
    public void updateProfile(User user) {

        Log.d("edituserprofile","updateProfile==");
        String userData = GsonFactory.getGson().toJson(user);

        Log.d("edituserprofile","user data=="+userData);

        RequestParams requestParams =  EditUserProfileUseCase.createRequestParams( user.getUserid(), userData);
        this.editProfileView.showProgress();
        this.editUserProfileUseCase.execute(requestParams, new Subscriber<LoginResponse>() {
            @Override
            public void onCompleted() {
                Log.d("edituserprofile","onCompleted called==");
                EditProfilePresenter.this.editProfileView.hideProgress();
            }

            @Override
            public void onError(Throwable e) {
                Log.d("edituserprofile","onerror called==");
                EditProfilePresenter.this.editProfileView.hideProgress();
                EditProfilePresenter.this.editProfileView.setProfileUpdateFailed();
                e.printStackTrace();
            }

            @Override
            public void onNext(LoginResponse loginResponse) {
                Log.d("edituserprofile","onNext called==");
                EditProfilePresenter.this.editProfileView.setProfileUpatedSuccessfully();
            }
        });

    }
}
