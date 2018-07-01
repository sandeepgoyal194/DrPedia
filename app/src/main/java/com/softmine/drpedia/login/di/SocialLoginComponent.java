package com.softmine.drpedia.login.di;


import com.softmine.drpedia.getToken.di.GetTokenModule;
import com.softmine.drpedia.login.view.SocialLoginActivity;

import dagger.Component;
import frameworks.di.component.BaseAppComponent;

@LoginScope
@Component(modules = {GetTokenModule.class,SocialLoginModule.class},dependencies = BaseAppComponent.class)
public interface SocialLoginComponent {

    void inject(SocialLoginActivity socialLoginActivity);

}
