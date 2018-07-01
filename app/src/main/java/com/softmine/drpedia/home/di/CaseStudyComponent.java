package com.softmine.drpedia.home.di;

import com.softmine.drpedia.home.fragment.CaseDiscussionDetailFragment;
import com.softmine.drpedia.home.fragment.CaseStudyUIFragment;
import com.softmine.drpedia.home.fragment.UploadCaseFragment;

import dagger.Component;
import frameworks.di.component.BaseAppComponent;

@PerActivity
@Component(dependencies = BaseAppComponent.class , modules = {ActivityModule.class,GetCaseStudyListModule.class})
public interface CaseStudyComponent extends ActivityComponent{
    void inject(CaseStudyUIFragment caseStudyUIFragment);
    void inject(CaseDiscussionDetailFragment caseDiscussionDetailFragment);
    void inject(UploadCaseFragment uploadCaseFragment);
}
