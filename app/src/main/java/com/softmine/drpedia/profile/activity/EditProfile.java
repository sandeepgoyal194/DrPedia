package com.softmine.drpedia.profile.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.softmine.drpedia.CaseStudyAppApplication;
import com.softmine.drpedia.DoctorGuideBaseActivity;
import com.softmine.drpedia.R;
import com.softmine.drpedia.home.di.ActivityModule;
import com.softmine.drpedia.home.di.CaseStudyComponent;
import com.softmine.drpedia.home.di.DaggerCaseStudyComponent;
import com.softmine.drpedia.home.di.GetCaseStudyListModule;
import com.softmine.drpedia.login.model.User;
import com.softmine.drpedia.profile.IEditProfileView;
import com.softmine.drpedia.profile.customlayout.DatePickerEditText;
import com.softmine.drpedia.profile.customlayout.EditTextCrossImage;
import com.softmine.drpedia.profile.presentor.EditProfilePresenter;
import com.softmine.drpedia.utils.UserManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import frameworks.AppBaseApplication;

public class EditProfile extends DoctorGuideBaseActivity implements IEditProfileView {

    CaseStudyComponent caseStudyComponent;

    @BindView(R.id.edt_ci_first_name)
    EditTextCrossImage mEditTextFirstName;

    @BindView(R.id.radio_group_gender)
    RadioGroup mRadioGroupGender;

    @BindView(R.id.txt_email_value)
    TextView mEditTextEmail;

    @BindView(R.id.male_radio_button)
    RadioButton mMaleRadioButton;
    @BindView(R.id.female_radio_button)
    RadioButton mFemaleRadioButton;

    @Inject
    EditProfilePresenter mPresenter;

    @BindView(R.id.dp_edt_dob)
    DatePickerEditText mDOB;

    @BindView(R.id.edit_profile_container)
    LinearLayout editProfileContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        setmToolbar();
        setDetails();
        this.initializeInjector();
        this.caseStudyComponent.inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.mPresenter.setView(this);
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
        User user = CaseStudyAppApplication.getParentApplication().getUser();

        if(user == null)
            return;

        if (user.getName() != null) {
            mEditTextFirstName.setText(user.getName());
        }

        if (user.getEmailid() != null) {
            mEditTextEmail.setText(user.getEmailid());
        }
        if (user.getGender() != null) {
            if (user.getGender().equalsIgnoreCase("male")) {
                mMaleRadioButton.setChecked(true);
            } else {
                mFemaleRadioButton.setChecked(true);
            }
        }

        if(user.getDob()!=null)
            mDOB.setText(user.getDob());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_done, menu);
        return true;
    }
    User mUser;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_done:
                mUser = createUser();
                mPresenter.updateProfile(mUser);
        }
        return true;
    }

    private User createUser() {
        Log.d("edituserprofile","user created");
        User user = new User(CaseStudyAppApplication.getParentApplication().getUser());
        user.setUserid(CaseStudyAppApplication.getParentApplication().getUser().getUserid());
        user.setName(mEditTextFirstName.getText());
        user.setEmailid(mEditTextEmail.getText().toString());
        RadioButton selectedRadioButton = ((RadioButton) findViewById(mRadioGroupGender.getCheckedRadioButtonId()));
        if(selectedRadioButton != null) {
            user.setGender(selectedRadioButton.getText().toString());
        }
        user.setDob(mDOB.getText().toString());
        return user;
    }

    @Override
    public void setProfileUpatedSuccessfully() {
        Log.d("edituserprofile","setProfileUpatedSuccessfully called==");
        CaseStudyAppApplication.getParentApplication().setUser(mUser);
        new UserManager(this).saveUser(mUser);
        showToast("Profile has updated successfully");
        finish();
    }

    @Override
    public void setProfileUpdateFailed() {
        showSnackBar("Profile updation has failed. please try again");
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
    public void showSnackBar(String message) {

        Snackbar snackbar = Snackbar
                .make(editProfileContainer, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
