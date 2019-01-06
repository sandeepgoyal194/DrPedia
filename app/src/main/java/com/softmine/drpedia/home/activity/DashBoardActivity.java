package com.softmine.drpedia.home.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softmine.drpedia.CaseStudyAppApplication;
import com.softmine.drpedia.R;
import com.softmine.drpedia.home.di.ActivityModule;
import com.softmine.drpedia.home.di.CaseStudyComponent;
import com.softmine.drpedia.home.di.DaggerCaseStudyComponent;
import com.softmine.drpedia.home.di.GetCaseStudyListModule;
import com.softmine.drpedia.home.fragment.DashBoardFragment;
import com.softmine.drpedia.home.fragment.FeedbackFragment;
import com.softmine.drpedia.home.fragment.MyPostsFragment;
import com.softmine.drpedia.home.fragment.UserBookmarksListFragment;
import com.softmine.drpedia.home.glide.CircleTransform;
import com.softmine.drpedia.home.model.CaseItem;
import com.softmine.drpedia.login.model.User;
import com.softmine.drpedia.login.view.SocialLoginActivity;
import com.softmine.drpedia.profile.activity.Profile;
import com.softmine.drpedia.utils.UserManager;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import frameworks.AppBaseApplication;
import frameworks.appsession.AppSessionManager;
import frameworks.di.component.BaseAppComponent;
import frameworks.di.component.HasComponent;
import frameworks.imageloader.ImageLoader;

public class DashBoardActivity extends AppCompatActivity implements DashBoardFragment.CaseListListener ,UserBookmarksListFragment.BookmarkCaseListListener,MyPostsFragment.IMyPostListListener,HasComponent<CaseStudyComponent>{


    static final String CASE_DETAIL = "CASE_DETAIL";
    static final String CASE_DETAIL_INTENT = "CASE_DETAIL_INTENT";
    private static final String TAG_HOME = "home";
    private static final String TAG_BOOKMARK = "bookmark";
    private static final String TAG_MYPOSTS = "myposts";
    private static final String TAG_MYFEEDBACK = "myfeedback";
    public static String CURRENT_TAG = TAG_HOME;
    public static int navItemIndex = 0;

    private String[] activityTitles;

    CaseStudyComponent caseStudyComponent;

    @Inject
    AppSessionManager appSessionManager;

    User userInfo;
    @Inject
    ImageLoader mImageLoader;

    String name;
    String picPath;
    DrawerLayout drawer;
    NavigationView navigationView;
    private View navHeader;
    FloatingActionButton fab;
    TextView username;
    ImageView profilePic;
    private Handler mHandler;
    Toolbar toolbar;
    final int CASE_UPLOAD_REQUEST_CODE=101;
    final int CASE_UPLOAD_RESPONSE_OK=102;
    final int CASE_UPLOAD_REQUEST_FAILS=103;
    private boolean shouldLoadHomeFragOnBackPress = true;
    Button signOut;
    final int MY_PERMISSIONS_REQUEST_READ_MEDIA=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.initializeInjector();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(this);

        Log.d("navupdate","oncreate");


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Initializing the tablayout
        toolbar.setTitle(R.string.app_name);
        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = findViewById(R.id.fab);
        signOut = navigationView.findViewById(R.id.btn_sing_out);
        navHeader = navigationView.getHeaderView(0);

        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserProfile();
            }
        });

        ButterKnife.bind(navHeader,this);

        username = navHeader.findViewById(R.id.userName);
        profilePic = navHeader.findViewById(R.id.user_profile_photo);
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, UploadCaseActivity.class);
                startActivityForResult(intent , CASE_UPLOAD_REQUEST_CODE);
            }
        });

        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                }
                break;

            default:
                break;
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("navupdate","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        userInfo = CaseStudyAppApplication.getParentApplication().getUser();
        Log.d("navupdate","onResume");
        loadNavHeader(userInfo);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("navupdate","onpause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("navupdate","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("navupdate","onDestroy");
    }

    private void loadNavHeader(User user) {

        username.setText(user.getName());
        Glide.with(this)
                .load(user.getPhotoUrl()) // add your image url
                .transform(new CircleTransform(this)) // applying the image transformer
                .into(profilePic);
    }


    public void openUserProfile()
    {
        Intent i = new Intent(this, Profile.class);
        startActivity(i);
    }


    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.bookmark:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_BOOKMARK;
                        break;
                    case R.id.myposts:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MYPOSTS;
                        break;
                    case R.id.myFeedback:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_MYFEEDBACK;
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });



        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void loadHomeFragment() {

        selectNavMenu();

        setToolbarTitle();
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
               /* fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);*/
                fragmentTransaction.replace(R.id.fragmentContainer, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        toggleFab();
        drawer.closeDrawers();
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment()
    {
        switch (navItemIndex) {
            case 0:
                DashBoardFragment dashBoardFragment = new DashBoardFragment();
                return dashBoardFragment;
            case 1:
                UserBookmarksListFragment userBookmarksListFragment = new UserBookmarksListFragment();
                return userBookmarksListFragment;
            case 2:
                MyPostsFragment myPostsFragment = new MyPostsFragment();
                return myPostsFragment;
            case 3:
                FeedbackFragment feedbackFragment = new FeedbackFragment();
                return feedbackFragment;
            default:
                return new DashBoardFragment();
        }
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
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
        }
        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
    }

    String uploadresult;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CASE_UPLOAD_REQUEST_CODE) {
            if (resultCode == CASE_UPLOAD_RESPONSE_OK) {
                if (data != null)
                {
                    uploadresult = (String) data.getExtras().get("uploadMsg");
                    showSnackBar(uploadresult);

                }
                else
                {
                    Log.d("loginresponse","data is null");
                }
            }
            else
            {
                Log.d("loginresponse","failed");
            }
        }
    }

    public void showSnackBar(String message) {

        Snackbar snackbar = Snackbar
                .make(drawer, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @OnClick(R.id.btn_sing_out)
    public void setSignOutSuccessfull() {
        new UserManager(this).clearUser();
        new AppSessionManager(this).clearSession();
        CaseStudyAppApplication.getParentApplication().setUser(null);
        CaseStudyAppApplication.getParentApplication().setAuthId(null);
        Intent i = new Intent(this, SocialLoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onBookmarkCaseClicked(CaseItem caseItem) {
        Intent callingIntent = new Intent(this, CaseDiscussionDetail.class);
       /* final Bundle arg = new Bundle();
        arg.putSerializable(CASE_DETAIL,userModel);*/
        callingIntent.putExtra(CASE_DETAIL,caseItem);
        startActivity(callingIntent);
    }

    @Override
    public void onMyCaseClicked(CaseItem caseItem) {
        Intent callingIntent = new Intent(this, CaseDiscussionDetail.class);
       /* final Bundle arg = new Bundle();
        arg.putSerializable(CASE_DETAIL,userModel);*/
        callingIntent.putExtra(CASE_DETAIL,caseItem);
        startActivity(callingIntent);
    }
}
