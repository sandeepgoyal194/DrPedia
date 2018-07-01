package com.softmine.drpedia.home.di;

import android.app.Activity;

import dagger.Component;
import frameworks.di.component.BaseAppComponent;

@PerActivity
@Component(dependencies = BaseAppComponent.class, modules = ActivityModule.class)
interface ActivityComponent {
    //Exposed to sub-graphs.
    Activity activity();
}