package com.softmine.drpedia.profile.presentor;

import android.support.annotation.NonNull;


import com.softmine.drpedia.getToken.model.LoginResponse;
import com.softmine.drpedia.profile.IProfileView;
import com.softmine.drpedia.profile.domain.usecases.UserProfileUseCase;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import rx.Subscriber;

public class ProfilePresentor implements IProfilePresentor {

    private IProfileView profileView;
    private UserProfileUseCase userProfileUseCase;

    @Inject
    public ProfilePresentor(UserProfileUseCase userProfileUseCase)
    {
        this.userProfileUseCase = userProfileUseCase;
    }

    public void setView(@NonNull IProfileView view) {
        this.profileView = view;
    }

    @Override
    public void getUserProfile() {

        this.profileView.showProgress();

        this.userProfileUseCase.execute(RequestParams.EMPTY, new Subscriber<LoginResponse>() {
            @Override
            public void onCompleted() {
                ProfilePresentor.this.profileView.hideProgress();
            }

            @Override
            public void onError(Throwable e) {
                ProfilePresentor.this.profileView.hideProgress();
                ProfilePresentor.this.profileView.onProfileSyncFailed();
            }

            @Override
            public void onNext(LoginResponse loginResponse) {

                ProfilePresentor.this.profileView.onProfileSyncedSuccessfully(loginResponse.getList().get(0));
            }
        });

    }
}
