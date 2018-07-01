package frameworks.basemvp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import frameworks.apppermission.PermissionActivity;
import frameworks.customlayout.EmptyLayout;
import in.healthhunt.framework.R;

/**
 * Created by sandeep.g9 on 3/7/2017.
 */

public abstract class AppBaseActivity<T extends IPresenter> extends PermissionActivity implements IActivityView {


    public static final String TAG = "Villeger";
    protected Fragment mAddedFragment;
    protected ActionBarDrawerToggle mActionBarDrawerToggle;
    public Toolbar mToolbar;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    IPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewToCreate());
        initInjector();
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    protected abstract void initInjector();

    public abstract int getViewToCreate();


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        //Activity don't want to setContent View;
        if (layoutResID == -1) {
            return;
        }
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        setmToolbar();
        if (mPresenter != null) {
            mPresenter.onViewCreated();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onViewStarted();
        }
    }


    protected void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void showProgressBar(String message) {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(getWindow().getDecorView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public abstract T getPresenter();

    public void setmToolbar() {
        setmToolbar(false);
    }

    public void setmToolbar(boolean needBackButton) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            if (needBackButton) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow, null));
                } else {
                    mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow));
                }
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }
    }


    protected void addFrgment(Fragment fragment, String fragmentTag, int containerView, boolean addToBackStack) {
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
            if (addToBackStack) {
                if (mFragmentManager.getFragments() != null && mFragmentManager.getFragments().size() > 0)
                    mOwnerTransaction.addToBackStack(null);
            }
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

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setTitle(CharSequence title) {
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText(title);
        textView.setVisibility(View.VISIBLE);
        getSupportActionBar().setTitle("");
    }

    @Override
    public void setPermission(int requestCode, boolean isGranted) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getPresenter() != null) {
            getPresenter().onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setLeftIcon(int leftIcon) {
        ImageView leftIconView = (ImageView) findViewById(R.id.left_icon);
        View left_menu = findViewById(R.id.left_menu);
        if (leftIconView != null) {
            leftIconView.setBackgroundResource(leftIcon);
            leftIconView.setVisibility(View.VISIBLE);
            left_menu.setVisibility(View.VISIBLE);
            left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mAddedFragment instanceof AppBaseFragment) {
                        if(!((AppBaseFragment) mAddedFragment).onLeftMenuClick()){
                            onLeftMenuClick();
                        }
                    }

                }
            });
        }
    }

    public void onLeftMenuClick() {

    }


    public void addEmptyLayout() {
        setContentView(getEmptyLayout());

    }

    public EmptyLayout getEmptyLayout() {
        return null;
    }

    public void setRightIcon(int drawableId) {
        ImageView rightIconView = (ImageView) findViewById(R.id.right_icon);
        View left_menu = findViewById(R.id.left_menu);
        if (rightIconView != null) {
            rightIconView.setBackgroundResource(drawableId);
            rightIconView.setVisibility(View.VISIBLE);
            left_menu.setVisibility(View.VISIBLE);
            left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mAddedFragment instanceof AppBaseFragment) {
                        if(!((AppBaseFragment) mAddedFragment).onRightMenuClick()){
                            onRightMenuClick();
                        }
                    }
                }
            });
        }
    }

    public void setRightText(String text) {
        TextView rightTitle = (TextView) findViewById(R.id.right_title);
        View right_menu = findViewById(R.id.right_menu);
        if (rightTitle != null) {
            rightTitle.setVisibility(View.VISIBLE);
            right_menu.setVisibility(View.VISIBLE);
            rightTitle.setText(text);
            right_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mAddedFragment instanceof AppBaseFragment) {
                        if(!((AppBaseFragment) mAddedFragment).onRightMenuClick()){
                            onRightMenuClick();
                        }
                    }
                }
            });
        }
    }

    private void  onRightMenuClick() {
    }

    public void setRightMenuEnable(boolean enable) {
            View menu = findViewById(R.id.right_menu);
            menu.setEnabled(enable);
    }

    public void setLeftMenuEnable(boolean enable) {
        View menu = findViewById(R.id.left_menu);
        menu.setEnabled(enable);
    }

    public void setLeftText(String text) {
        TextView leftTitle = (TextView) findViewById(R.id.left_title);
        View left_menu = findViewById(R.id.left_menu);
        if (leftTitle != null) {
            leftTitle.setVisibility(View.VISIBLE);
            left_menu.setVisibility(View.VISIBLE);
            leftTitle.setText(text);
            left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mAddedFragment instanceof AppBaseFragment) {
                        if(!((AppBaseFragment) mAddedFragment).onLeftMenuClick()){
                            onLeftMenuClick();
                        }
                    }
                }
            });
        }
    }

    public void setmAddedFragment(Fragment mAddedFragment) {
        this.mAddedFragment = mAddedFragment;
    }
}
