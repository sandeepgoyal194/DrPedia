package com.softmine.drpedia.profile.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.greenfrvr.hashtagview.HashtagView;
import com.softmine.drpedia.CaseStudyAppApplication;
import com.softmine.drpedia.DoctorGuideBaseActivity;
import com.softmine.drpedia.R;
import com.softmine.drpedia.home.di.ActivityModule;
import com.softmine.drpedia.home.di.CaseStudyComponent;
import com.softmine.drpedia.home.di.DaggerCaseStudyComponent;
import com.softmine.drpedia.home.di.GetCaseStudyListModule;
import com.softmine.drpedia.home.glide.CircleTransform;
import com.softmine.drpedia.home.model.CategoryMainItemResponse;
import com.softmine.drpedia.home.model.SubCategoryItem;
import com.softmine.drpedia.login.model.User;
import com.softmine.drpedia.profile.IProfileView;
import com.softmine.drpedia.profile.presentor.ProfilePresentor;
import com.softmine.drpedia.utils.UserManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    protected @BindView(R.id.hashtags2)
    HashtagView hashtagView2;

    @BindView(R.id.userProfileContainer)
    LinearLayout user_profile_container;

    @BindView(R.id.no_interest_data_container)
    LinearLayout no_interest_data_container;

    List<String> TAGS = Arrays.asList("cupcake", "donut", "eclair", "froyo",
            "gingerbread", "honeycomb", "icecreamsandwich", "jellybean", "kitkat", "lollipop", "marshmallow");

    List<String> TAGS1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setmToolbar();
        this.initializeInjector();

        this.caseStudyComponent.inject(this);
    }

    public static final HashtagView.DataTransform<String> HASH = new HashtagView.DataTransform<String>() {
        @Override
        public CharSequence prepare(String item) {
            SpannableString spannableString = new SpannableString("#" + item);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#85F5F5F5")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        TAGS1 = new ArrayList<>();
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

        Log.d("userprofile"," set details called");
        User user = CaseStudyAppApplication.getParentApplication().getUser();
        if (user == null) {
            Log.d("userprofile"," user null");
            user = new UserManager(this).getUser();
            if (user == null) {
                Log.d("userprofile"," user null in user manager");
                profilePresentor.getUserProfile();
                return;
            } else {
                CaseStudyAppApplication.getParentApplication().setUser(user);
            }
        }
        setUserView(user);
    }

    public void setUserView(User user) {

        Log.d("userprofile"," setUserView called");
        Log.d("userprofile","api key==="+user.getAuthToken());
        Log.d("userprofile","name==="+user.getName());
        Log.d("userprofile","user id==="+user.getUserid());
        Log.d("userprofile","photo url==="+user.getPhotoUrl());
        Log.d("userprofile","email id==="+user.getEmailid());
        Log.d("userprofile","gender==="+user.getGender());
        Log.d("userprofile","DOB==="+user.getDob());

        List<CategoryMainItemResponse> res = user.getData();
        TAGS1.clear();
        if(res!=null)
        {
            Log.d("userprofile"," category size==="+res.size());
            for(CategoryMainItemResponse res1 : res)
            {
                Log.d("userprofile","Main Category name==="+res1.getCategoryName());
                Log.d("userprofile","Main Category ID==="+res1.getCategoryID());
                for(SubCategoryItem item1 : res1.getSubCategory())
                {
                    TAGS1.add(item1.getSubtype());
                    Log.d("userprofile","sub Category name==="+item1.getSubtype());
                    Log.d("userprofile","sub Category ID==="+item1.getSubtype_id());
                    Log.d("userprofile","sub Category interest ID==="+item1.getIntrest_id());
                }
            }
        }

        if(TAGS1.size()>0)
        {

            hashtagView2.setVisibility(View.VISIBLE);
            hashtagView2.setData(TAGS1, HASH);
            hashtagView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Profile.this, EditProfile.class);
                    startActivity(i);
                }
            });
        }
        else
        {

            profilePresentor.getUserInterest();

        }


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
        Log.d("userprofile"," onProfileSyncedSuccessfully called");
        new UserManager(this).saveUser(user);
        CaseStudyAppApplication.getParentApplication().setUser(user);
        setDetails();
    }

    @Override
    public void onProfileSyncFailed() {

    }

    @Override
    public void setUserInterestSize(List<CategoryMainItemResponse> categoryMainItemResponses) {

        TAGS1.clear();

        if(categoryMainItemResponses.size()>0)
        {
            for(CategoryMainItemResponse res1 : categoryMainItemResponses) {
                Log.d("userprofile", "Main Category name===" + res1.getCategoryName());
                Log.d("userprofile", "Main Category ID===" + res1.getCategoryID());
                for (SubCategoryItem item1 : res1.getSubCategory()) {
                    TAGS1.add(item1.getSubtype());
                    Log.d("userprofile", "sub Category name===" + item1.getSubtype());
                    Log.d("userprofile", "sub Category ID===" + item1.getSubtype_id());
                    Log.d("userprofile", "sub Category interest ID===" + item1.getIntrest_id());
                }
            }
            hashtagView2.setVisibility(View.VISIBLE);
            hashtagView2.setData(TAGS1, HASH);
            hashtagView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Profile.this, EditProfile.class);
                    startActivity(i);
                }
            });
        }
        else
        {
            no_interest_data_container.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void showToast(String message) {

    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void showSnackBar(String message) {
        Log.d("userprofile"," showSnackBar called");
        Log.d("userprofile"," message  === "+message);
        Snackbar snackbar = Snackbar
                .make(user_profile_container, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void addEmptyLayout() {

    }
}
