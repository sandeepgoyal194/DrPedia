package com.softmine.drpedia.login.view;

import com.softmine.drpedia.getToken.model.LoginResponse;

import frameworks.appsession.SessionValue;
import frameworks.basemvp.IPresenter;
import frameworks.basemvp.IView;

public interface ILoginViewContractor {

    public interface View extends IView {
        public void setLoginResponse(LoginResponse loginResponse);
        public void startActivity();
    }

    public interface Presenter extends IPresenter<View> {
        public void getAuthID(String token);
    }

}
