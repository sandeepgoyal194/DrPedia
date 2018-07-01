package frameworks.basemvp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import butterknife.ButterKnife;
import frameworks.customlayout.EmptyLayout;
import in.healthhunt.framework.R;

/**
 * Created by sandeep.g9 on 3/7/2017.
 */

public abstract class AppBaseFragment<T extends IPresenter> extends Fragment implements IView {
    AppBaseActivity mBaseActivity = null;
    IPresenter mPresenter;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = getView(inflater, container, savedInstanceState);
        initInjector();
        ButterKnife.bind(this, view);
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.onViewStarted();
            mPresenter.attachView(this);
        }
        setHasOptionsMenu(true);
        return view;
    }

    protected abstract void initInjector();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null) {
            mPresenter.onViewCreated();
        }
        setMenuIcons();
    }

    public boolean onLeftMenuClick() {
        return false;
    }

    public boolean onRightMenuClick() {
        return false;
    }


    public void setMenuIcons() {
        if (getView() != null) {
            View menu = getView().findViewById(R.id.left_menu);
            View rightMenu = getView().findViewById(R.id.right_menu);
            if (menu != null) {
                menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onLeftMenuClick();
                    }
                });
            }
            if (rightMenu != null) {
                rightMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onRightMenuClick();
                    }
                });
            }
        }
    }

    public abstract View getView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppBaseActivity) {
            mBaseActivity = (AppBaseActivity) context;
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }


    public void setLeftIcon(int drawableId) {
        if (getView() != null) {
            ImageView leftIcon = getView().findViewById(R.id.left_icon);
            if (leftIcon == null){
                mBaseActivity.setLeftIcon(drawableId);
            }else {
                leftIcon.setBackgroundResource(drawableId);
                leftIcon.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setLeftText(String text) {

        if (getView() != null) {
            TextView leftTitle = getView().findViewById(R.id.left_title);
            if(leftTitle == null) {
                mBaseActivity.setLeftText(text);
            }else {
                leftTitle.setText(text);
                leftTitle.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setRightIcon(int drawableId) {
        if (getView() != null) {
            ImageView rightIcon = getView().findViewById(R.id.right_icon);
            if(rightIcon == null) {
                mBaseActivity.setRightIcon(drawableId);
            }else {
                rightIcon.setBackgroundResource(drawableId);
                rightIcon.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setRightText(String text) {
        if (getView() != null) {
            TextView rightTitle = getView().findViewById(R.id.right_title);
            if(rightTitle == null)
                mBaseActivity.setRightText(text);
            else {
                rightTitle.setText(text);
                rightTitle.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setRightMenuEnable(boolean enable) {
        if (getView() != null) {
            View menu = getView().findViewById(R.id.right_menu);
            if(menu == null) {
                mBaseActivity.setRightMenuEnable(enable);
            }else {
                menu.setEnabled(enable);
            }
        }
    }

    public void setLeftMenuEnable(boolean enable) {
        if (getView() != null) {
            View menu = getView().findViewById(R.id.left_menu);
            if(menu == null) {
                mBaseActivity.setLeftMenuEnable(enable);
            }else {
                menu.setEnabled(enable);
            }
        }
    }

    public void setTitle(String titleText) {
        mBaseActivity.setTitle(titleText);
    }

    public abstract T getPresenter();

    @Override
    public void showProgressBar() {
        mBaseActivity.showProgressBar();
    }

    @Override
    public void showProgressBar(String message) {
        mBaseActivity.showProgressBar(message);
    }

    @Override
    public void hideProgressBar() {
        mBaseActivity.hideProgressBar();

    }

    @Override
    public void showSnackBar(String message) {
        mBaseActivity.showSnackBar(message);
    }

    @Override
    public void showToast(String message) {
        mBaseActivity.showToast(message);
    }

    @Override
    public void setResult(int result) {
        mBaseActivity.setResult(result);
    }

    @Override
    public void setResult(int result, Intent data) {
        mBaseActivity.setResult(result, data);
    }

    public void finish() {
        mBaseActivity.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getPresenter().onActivityResult(requestCode, resultCode, data);
    }


    public void replaceFragment(int id, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        if (getChildFragmentManager().findFragmentById(id) == null) {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.replace(id, fragment);
            transaction.commit();
            getChildFragmentManager().executePendingTransactions();
        }
    }

    public boolean popFragment() {
        boolean isPop = false;
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            isPop = true;
            getChildFragmentManager().popBackStack();
        }
        return isPop;
    }

    public void addEmptyLayout() {
        ViewGroup view = (ViewGroup) this.view;
        view.removeAllViews();
        view.addView(getEmptyLayout());
    }

    public EmptyLayout getEmptyLayout() {
        return null;
    }


    public abstract String getTitle();
}
