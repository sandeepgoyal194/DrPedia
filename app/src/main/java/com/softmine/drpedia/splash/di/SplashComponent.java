package com.softmine.drpedia.splash.di;



import com.softmine.drpedia.splash.activity.DoctorSGuideSplashScreen;

import dagger.Component;
import frameworks.di.component.BaseAppComponent;

@SplashScope
@Component(dependencies = BaseAppComponent.class)
public interface SplashComponent {

    void inject(DoctorSGuideSplashScreen doctorSGuideSplashScreen);

}
