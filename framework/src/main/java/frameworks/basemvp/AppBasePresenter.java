package frameworks.basemvp;

import android.content.Intent;

/**
 * Created by sandeep.g9 on 7/27/2017.
 */

public abstract class AppBasePresenter<T extends IView> implements IPresenter<T> {

    T view;
    @Override
    public void attachView(T view) {
        this.view = view;
    }

    public void onViewStarted() {

    }

    @Override
    public void onViewCreated() {

    }

    public void detachView() {

    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data)
    {
        return false;
    }

    @Override
    public T getView() {
        return view;
    }
}
