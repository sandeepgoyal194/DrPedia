package com.softmine.drpedia.home.di;


import com.softmine.drpedia.home.fragment.CaseDiscussionDetailFragment;
import com.softmine.drpedia.home.fragment.DashBoardFragment;
import com.softmine.drpedia.home.fragment.FeedbackFragment;
import com.softmine.drpedia.home.fragment.MyPostsFragment;
import com.softmine.drpedia.home.fragment.UploadCaseFragment;
import com.softmine.drpedia.home.fragment.UserBookmarksListFragment;
import com.softmine.drpedia.profile.activity.EditProfile;
import com.softmine.drpedia.profile.activity.Profile;

import dagger.Component;
import frameworks.di.component.BaseAppComponent;

@PerActivity
@Component(dependencies = BaseAppComponent.class , modules = {ActivityModule.class, GetCaseStudyListModule.class})
public interface CaseStudyComponent extends ActivityComponent{
    void inject(DashBoardFragment dashBoardFragment);
    void inject(UserBookmarksListFragment userBookmarksListFragment);
    void inject(MyPostsFragment userPostsFragment);
    void inject(FeedbackFragment feedbackFragment);
    void inject(CaseDiscussionDetailFragment caseDiscussionDetailFragment);
    void inject(UploadCaseFragment uploadCaseFragment);
    void inject(Profile profile);
    void inject(EditProfile editProfile);
}
