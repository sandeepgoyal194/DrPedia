package com.softmine.drpedia.splash.di;



import com.softmine.drpedia.home.di.ActivityModule;
import com.softmine.drpedia.home.di.GetCaseStudyListModule;
import com.softmine.drpedia.home.di.ServiceModule;
import com.softmine.drpedia.splash.activity.DoctorSGuideSplashScreen;

import dagger.Component;
import frameworks.di.component.BaseAppComponent;

@SplashScope
@Component(dependencies = BaseAppComponent.class , modules = {GetCaseStudyListModule.class})
public interface SplashComponent {

    void inject(DoctorSGuideSplashScreen doctorSGuideSplashScreen);

}
