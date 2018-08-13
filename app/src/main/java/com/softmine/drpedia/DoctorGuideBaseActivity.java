package com.softmine.drpedia;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DoctorGuideBaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    protected Fragment mAddedFragment;
    FragmentManager mFragmentManager;
    protected Toolbar mToolbar;
    boolean isFullScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View mDecorView;

    public void setFullScreen(boolean setFullScreen) {
        mDecorView = getWindow().getDecorView();
        if (setFullScreen) {
            hideSystemUI();
        } else {
            showSystemUI();
        }
        isFullScreen = setFullScreen;
        mDecorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0 && isFullScreen) {
                    hideSystemUI();
                }
            }
        });
    }

    public void hideSystemUI() {
        mDecorView.setSystemUiVisibility(getHideSystemUIFlag());
    }

    public void showSystemUI() {
        mDecorView.setSystemUiVisibility(getShowSystemUIFlag());
    }

    private int getHideSystemUIFlag() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    }

    private int getShowSystemUIFlag() {
        return 0;
    }

    public void hideProgressBar() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }

    public void showProgressBar() {
        showProgressBar(null);
    }

    public void showProgressBar(String title) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this, R.style.ProgressBar);
            //  mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//            mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            if (title != null) {
                mProgressDialog.setMessage(title);
            }
        }
        mProgressDialog.show();
    }


    protected void addFrgment(Fragment fragment, String fragmentTag, int containerView) {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction mOwnerTransaction = mFragmentManager.beginTransaction();
        Fragment fragment1 = mFragmentManager.findFragmentByTag(fragmentTag);
        if (fragment1 != null) {
            fragment = fragment1;
        }
        if (!mFragmentManager.popBackStackImmediate(fragment.getClass().getName(), 0)) {
            mOwnerTransaction.replace(containerView, fragment, fragmentTag);
            if (mFragmentManager.getFragments() != null && mFragmentManager.getFragments().size() > 0)
                mOwnerTransaction.addToBackStack(null);
            mAddedFragment = fragment;
            mOwnerTransaction.commit();
        }
    }

    protected void popFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(fragment);
        trans.commit();
        manager.popBackStack();
    }

    public void setmToolbar() {
        setmToolbar(true);
    }

    public void setmToolbar(boolean showBackButton) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        if (showBackButton) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            Drawable drawable = mToolbar.getNavigationIcon();
            drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            mToolbar.setNavigationIcon(drawable);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }
}
