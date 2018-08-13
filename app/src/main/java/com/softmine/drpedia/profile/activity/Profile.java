package com.softmine.drpedia.profile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softmine.drpedia.CaseStudyAppApplication;
import com.softmine.drpedia.DoctorGuideBaseActivity;
import com.softmine.drpedia.R;
import com.softmine.drpedia.home.di.ActivityModule;
import com.softmine.drpedia.home.di.CaseStudyComponent;
import com.softmine.drpedia.home.di.DaggerCaseStudyComponent;
import com.softmine.drpedia.home.di.GetCaseStudyListModule;
import com.softmine.drpedia.home.glide.CircleTransform;
import com.softmine.drpedia.login.model.User;
import com.softmine.drpedia.profile.IProfileView;
import com.softmine.drpedia.profile.presentor.ProfilePresentor;
import com.softmine.drpedia.utils.UserManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import frameworks.AppBaseApplication;

public class Profile extends DoctorGuideBaseActivity implements IProfileView {

    CaseStudyComponent caseStudyComponent;

    @BindView(R.id.txt_email_value)
    TextView mEditTextEmail;

    @BindView(R.id.txt_user_full_name)
    TextView mEditTextName;

    @BindView(R.id.user_profile_pic)
    ImageView userProfilePic;

    @Inject
    ProfilePresentor profilePresentor;

    @BindView(R.id.gender_radio_btn)
    RadioButton mGenderBtn;

    @BindView(R.id.dp_edt_dob)
    TextView mDOB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setmToolbar();
        this.initializeInjector();
        this.caseStudyComponent.inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        profilePresentor.setView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDetails();
    }

    private void initializeInjector() {
        this.caseStudyComponent = DaggerCaseStudyComponent.builder()
                .baseAppComponent(((AppBaseApplication)getApplication())
                        .getBaseAppComponent())
                .getCaseStudyListModule(new GetCaseStudyListModule(this))
                .activityModule(getActivityModule())
                .build();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    private void setDetails() {
        //check if user change its details
        User user = CaseStudyAppApplication.getParentApplication().getUser();
        if (user == null) {
            user = new UserManager(this).getUser();
            if (user == null) {
                profilePresentor.getUserProfile();
                return;
            } else {
                CaseStudyAppApplication.getParentApplication().setUser(user);
            }
        }
        setUserView(user);
    }

    public void setUserView(User user) {
        if (user.getEmailid() != null) {
            mEditTextEmail.setText(user.getEmailid());
        }
        String name;
        if (user.getName() != null) {
            name = user.getName();
            mEditTextName.setText(name);
        }

        Glide.with(this)
                .load(user.getPhotoUrl()) // add your image url
                .transform(new CircleTransform(this)) // applying the image transformer
                .into(userProfilePic);

        if(user.getGender() !=null)
        {
            if (user.getGender().equalsIgnoreCase("male")) {
                mGenderBtn.setChecked(true);
                mGenderBtn.setText("Male");
            } else {
                mGenderBtn.setChecked(true);
                mGenderBtn.setText("Female");
            }
        }

        if(user.getDob()!=null)
        {
            mDOB.setText(user.getDob().toString());
        }
    }

    @Override
    public void showProgress() {
        showProgressBar();
    }

    @Override
    public void hideProgress() {
        hideProgressBar();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(this, EditProfile.class);
        startActivity(i);
        return true;
    }

    @Override
    public void onProfileSyncedSuccessfully(User user) {
        new UserManager(this).saveUser(user);
        CaseStudyAppApplication.getParentApplication().setUser(user);
        setDetails();
    }

    @Override
    public void onProfileSyncFailed() {

    }


}
