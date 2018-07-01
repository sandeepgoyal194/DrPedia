package frameworks.customlayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


import frameworks.basemvp.AppBaseActivity;
import frameworks.basemvp.AppBaseFragment;
import frameworks.basemvp.IPresenter;
import in.healthhunt.framework.R;

public abstract class ActivitySingleFragment<T extends AppBaseFragment> extends AppBaseActivity {
    String TAG = "SingleFragment";
    @Override
    public int getViewToCreate() {
        return R.layout.activity_single_fragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppBaseFragment fragment = getFragment();
        addFrgment(fragment,TAG, R.id.container,true);
        setTitle(fragment.getTitle());
    }


    @Override
    public IPresenter getPresenter() {
        return null;
    }
    protected abstract T getFragment();
}
