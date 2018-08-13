package com.softmine.drpedia.home.presentor;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;


import com.softmine.drpedia.home.CaseListView;
import com.softmine.drpedia.home.di.PerActivity;
import com.softmine.drpedia.home.domain.usecases.GetCaseStudyListUseCase;
import com.softmine.drpedia.home.model.CaseItem;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import rx.Subscriber;

@PerActivity
public class CaseListPresentor implements ICaseListPresenter {

    private CaseListView viewListView;

    private GetCaseStudyListUseCase getCaseStudyListUseCase;

    @Inject
    public CaseListPresentor(GetCaseStudyListUseCase getCaseStudyListUseCase)
    {
        this.getCaseStudyListUseCase = getCaseStudyListUseCase;
    }

    public void setView(@NonNull CaseListView view) {
        this.viewListView = view;
    }

    public void onUserClicked(CaseItem userModel) {
        this.viewListView.viewUser(userModel);
    }

    public void initialize() {
        Log.d("loginresponse","loadCaseStudyList presentor");
        this.hideViewRetry();
        this.showViewLoading();
        this.loadCaseStudyList();
    }


    @Override
    public void loadCaseStudyList() {

        this.getCaseStudyListUseCase.execute(RequestParams.EMPTY,new Subscriber<List<CaseItem>>() {
            @Override
            public void onCompleted() {
                Log.d("loginresponse","onCompleted");
                CaseListPresentor.this.hideViewLoading();

            }

            @Override
            public void onError(Throwable e) {
                Log.d("loginresponse","onError");
                CaseListPresentor.this.hideViewLoading();
                CaseListPresentor.this.showViewRetry();
                e.printStackTrace();
            }

            @Override
            public void onNext(List<CaseItem> caseList) {
                Log.d("loginresponse","onNext");
                CaseListPresentor.this.showUsersCollectionInView(caseList);
            }
        });
    }

    @Override
    public void destroy() {
        //this.getCaseStudyListUseCase.disp
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    private  void showViewLoading()
    {
        this.viewListView.showProgressBar();
    }


    private void hideViewLoading() {
        Log.d("loginresponse","hide loading====================");

        this.viewListView.hideProgressBar();
    }

    private void showViewRetry() {
        this.viewListView.showRetry();
    }

    private void hideViewRetry() {
        this.viewListView.hideRetry();
    }

    private void showUsersCollectionInView(Collection<CaseItem> usersCollection) {
      /*  final Collection<CaseItem> userModelsCollection =
                this.userModelDataMapper.transform(usersCollection);*/

        this.viewListView.renderCaseList(usersCollection);
    }

  /*  private final class UserListObserver extends DefaultObserver<List<CaseItem>> {

        @Override
        public void onComplete()
        {
            CaseListPresentor.this.hideViewLoading();
        }

        @Override public void onError(Throwable e) {
            CaseListPresentor.this.hideViewLoading();
            CaseListPresentor.this.showViewRetry();
        }

        @Override public void onNext(List<CaseItem> users) {
            CaseListPresentor.this.showUsersCollectionInView(users);
        }
    }*/


}
