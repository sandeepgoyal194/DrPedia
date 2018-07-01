package com.softmine.drpedia.home.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sachin.doctorsguide.R;
import com.softmine.drpedia.home.di.ActivityModule;
import com.softmine.drpedia.home.di.CaseStudyComponent;
import com.softmine.drpedia.home.di.DaggerCaseStudyComponent;
import com.softmine.drpedia.home.di.GetCaseStudyListModule;
import com.softmine.drpedia.home.fragment.CaseStudyUIFragment;
import com.softmine.drpedia.home.glide.CircleTransform;
import com.softmine.drpedia.home.model.CaseItem;
import com.softmine.drpedia.login.model.User;

import javax.inject.Inject;

import butterknife.ButterKnife;
import frameworks.AppBaseApplication;
import frameworks.appsession.AppSessionManager;
import frameworks.appsession.UserInfo;
import frameworks.di.component.BaseAppComponent;
import frameworks.di.component.HasComponent;
import frameworks.imageloader.ImageLoader;

public class DashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , CaseStudyUIFragment.CaseListListener ,HasComponent<CaseStudyComponent> {

    CaseStudyComponent caseStudyComponent;
     static final String CASE_DETAIL = "CASE_DETAIL";
     static final String CASE_DETAIL_INTENT = "CASE_DETAIL_INTENT";

    @Inject
    AppSessionManager appSessionManager;

    User userInfo;
    @Inject
    ImageLoader mImageLoader;

    String name;
    String picPath;
    DrawerLayout drawer;

    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(this);
        this.initializeInjector();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Initializing the tablayout
        toolbar.setTitle(R.string.app_name);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            final FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragmentContainer, new CaseStudyUIFragment());
            fragmentTransaction.commit();
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, UploadCaseActivity.class);
                startActivity(intent);
            }
        });

        userInfo = (User) getIntent().getExtras().get("user");
        username = navigationView.getHeaderView(0).findViewById(R.id.userName);
      //  userInfo = appSessionManager.getSession().getUserInfo();
        name = userInfo.getName();
        picPath = userInfo.getPhotoUrl();
        Log.d("profile","name==="+name);
        Log.d("profile","pic path==="+picPath);

        username.setText(name);

        ImageView profilePic = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.user_profile_photo);

        Glide.with(this)
                .load(picPath) // add your image url
                .transform(new CircleTransform(this)) // applying the image transformer
                .into(profilePic);

    }

    private void setHeaderData()
    {

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

    @Override
    public CaseStudyComponent getComponent() {
        return caseStudyComponent;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected BaseAppComponent getApplicationComponent() {
        return ((AppBaseApplication) getApplication()).getBaseAppComponent();
    }

    @Override
    public void onCaseClicked(CaseItem caseItem) {

        Intent callingIntent = new Intent(this, CaseDiscussionDetail.class);
       /* final Bundle arg = new Bundle();
        arg.putSerializable(CASE_DETAIL,userModel);*/
        callingIntent.putExtra(CASE_DETAIL,caseItem);
        startActivity(callingIntent);
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
}
