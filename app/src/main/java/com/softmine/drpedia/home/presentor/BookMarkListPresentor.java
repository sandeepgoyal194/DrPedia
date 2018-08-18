package com.softmine.drpedia.home.presentor;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.softmine.drpedia.home.CaseListView;
import com.softmine.drpedia.home.domain.usecases.GetBookmarkListUseCase;
import com.softmine.drpedia.home.model.BookmarkItem;
import com.softmine.drpedia.home.model.CaseItem;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public class BookMarkListPresentor implements com.sachin.doctorsguide.home.presentor.IBookmarkListPresentor {

    private CaseListView viewListView;

    private GetBookmarkListUseCase getBookmarkListUseCase;

    @Inject
    public BookMarkListPresentor(GetBookmarkListUseCase getBookmarkListUseCase)
    {
        this.getBookmarkListUseCase = getBookmarkListUseCase;
    }

    public void setView(@NonNull CaseListView view) {
        this.viewListView = view;
    }

    public void onUserClicked(CaseItem userModel) {
        this.viewListView.viewUser(userModel);
    }

    public void initialize() {
        Log.d("bookmarkresponse","loadCaseStudyList presentor");
        this.hideViewRetry();
        this.showViewLoading();
        this.loadBookmarkList();
    }


    @Override
    public void loadBookmarkList() {

        this.getBookmarkListUseCase.execute(RequestParams.EMPTY,new Subscriber<List<BookmarkItem>>() {
            @Override
            public void onCompleted() {
                Log.d("bookmarkresponse","onCompleted");
                BookMarkListPresentor.this.hideViewLoading();

            }

            @Override
            public void onError(Throwable e) {
                Log.d("bookmarkresponse","onError");
                BookMarkListPresentor.this.hideViewLoading();
                BookMarkListPresentor.this.showViewRetry();
                e.printStackTrace();

                if(e instanceof IOException)
                {
                    Log.d("bookmarkresponse","Internet not working");
                    viewListView.showSnackBar("Internet not working");
                }
                else
                {
                    Log.d("bookmarkresponse","exception code  ");
                    if(e instanceof JsonSyntaxException)
                    {
                        Log.d("bookmarkresponse", "Json Parsing exception");
                        viewListView.showSnackBar("Json Parsing exception");
                    }
                    else if(e instanceof HttpException)
                    {
                        Log.d("bookmarkresponse","exception code  "+((HttpException)e).code());
                        viewListView.showSnackBar("server issues");
                    }
                    else {
                        Log.d("bookmarkresponse", "other issues");
                        viewListView.showSnackBar("issues occured while getting bookmarks list");
                    }
                }
            }

            @Override
            public void onNext(List<BookmarkItem> caseList) {
                Log.d("bookmarkresponse","onNext");
                BookMarkListPresentor.this.showUsersCollectionInView(caseList);
            }
        });

    }

    @Override
    public void destroy() {

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
        Log.d("bookmarkresponse","hide loading====================");

        this.viewListView.hideProgressBar();
    }

    private void showViewRetry() {
        this.viewListView.showRetry();
    }

    private void hideViewRetry() {
        this.viewListView.hideRetry();
    }

    private void showUsersCollectionInView(Collection<? extends CaseItem> usersCollection) {
      /*  final Collection<CaseItem> userModelsCollection =
                this.userModelDataMapper.transform(usersCollection);*/
        Log.d("bookmarkresponse","list size===="+usersCollection.size());
        this.viewListView.renderCaseList(usersCollection);
    }
}
