package com.softmine.drpedia.home.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softmine.drpedia.R;
import com.softmine.drpedia.home.CaseListView;
import com.softmine.drpedia.home.adapter.CaseListAdapter;
import com.softmine.drpedia.home.di.CaseStudyComponent;
import com.softmine.drpedia.home.model.CaseItem;
import com.softmine.drpedia.home.presentor.BookMarkListPresentor;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import frameworks.di.component.HasComponent;

public class UserBookmarksListFragment extends Fragment implements CaseListView {

    public interface BookmarkCaseListListener {
        void onBookmarkCaseClicked(final CaseItem userModel);
    }

    private BookmarkCaseListListener bookmarkCaseListListener;

    @Inject
    CaseListAdapter caseListAdapter;
    @Inject
    BookMarkListPresentor bookMarkListPresentor;

    @BindView(R.id.list_case)
    RecyclerView rv_users;
    @BindView(R.id.rl_progress)
    RelativeLayout rl_progress;
    @BindView(R.id.rl_retry)
    RelativeLayout rl_retry;
    @BindView(R.id.bt_retry)
    Button bt_retry;
    @BindView(R.id.bookmarkListContainer)
    FrameLayout parentLayout;

    @BindView(R.id.nodata_tv)
    TextView emptyTv;

    public UserBookmarksListFragment() {
        // Required empty public constructor
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(CaseStudyComponent.class).inject(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BookmarkCaseListListener) {
            this.bookmarkCaseListListener = (BookmarkCaseListListener) activity;
        }
    }

    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Log.d("bookmarkresponse", "onCreateView called");

        final View fragmentView = inflater.inflate(R.layout.fragment_bookmark_case_list, container, false);
        ButterKnife.bind(this, fragmentView);
        setupRecyclerView();
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("bookmarkresponse", "onViewCreated called");
        this.bookMarkListPresentor.setView(this);
       /* if (savedInstanceState == null) {
            this.loadCaseStudyList();
        }*/
    }


    private void loadCaseStudyList() {

        Log.d("bookmarkresponse", "loadCaseStudyList fragment");

        this.bookMarkListPresentor.initialize();
    }

    @Override
    public void onStart() {

        super.onStart();
        Log.d("bookmarkresponse", "onStart fragment");
    }

    @Override
    public void onResume() {

        super.onResume();
        Log.d("bookmarkresponse", "onResume fragment");
        this.loadCaseStudyList();
    }

    @Override
    public void onStop() {

        super.onStop();
        Log.d("bookmarkresponse", "onStop fragment");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("bookmarkresponse", "onViewStateRestored fragment");
    }

    @OnClick(R.id.bt_retry)
    void onButtonRetryClick() {
        UserBookmarksListFragment.this.loadCaseStudyList();
    }

    private void setupRecyclerView() {
        this.caseListAdapter.setOnItemClickListener(onItemClickListener);
        this.rv_users.setLayoutManager(new LinearLayoutManager(context()));
        this.rv_users.setAdapter(caseListAdapter);
    }

    public Context context() {
        return this.getActivity().getApplicationContext();
    }

    private CaseListAdapter.OnItemClickListener onItemClickListener =
            new CaseListAdapter.OnItemClickListener() {
                @Override
                public void onUserItemClicked(CaseItem userModel) {
                    if (UserBookmarksListFragment.this.bookMarkListPresentor != null && userModel != null) {
                        UserBookmarksListFragment.this.bookMarkListPresentor.onUserClicked(userModel);
                    }
                }
            };

    @Override
    public void showRetry() {
        this.rl_retry.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetry() {
        this.rl_retry.setVisibility(View.GONE);
    }

    @Override
    public void renderCaseList(Collection<? extends CaseItem> caseStudyCollection) {
        Log.d("bookmarkresponse", "caseStudyCollection size" + caseStudyCollection.size());
        if (caseStudyCollection != null) {
            Log.d("bookmarkresponse", "size=="+caseStudyCollection.size());
            Log.d("bookmarkresponse", "inside if ");

            if(caseStudyCollection.size()>0){
                emptyTv.setVisibility(View.GONE);
                this.caseListAdapter.setUsersCollection(caseStudyCollection);
            }
            else
            {
                Log.d("bookmarkresponse", "inside else ");
                emptyTv.setVisibility(View.VISIBLE);
                this.caseListAdapter.setUsersCollection(caseStudyCollection);

            }
        }
    }

    @Override
    public void viewUser(CaseItem CaseItem) {
        if (this.bookmarkCaseListListener != null) {
            this.bookmarkCaseListListener.onBookmarkCaseClicked(CaseItem);
        }
    }

    @Override
    public void showProgressBar() {
        this.rl_progress.setVisibility(View.VISIBLE);
        this.getActivity().setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void showProgressBar(String message) {
    }

    @Override
    public void hideProgressBar() {
        Log.d("bookmarkresponse", "hide loading====================");
        this.rl_progress.setVisibility(View.GONE);
        this.getActivity().setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void showSnackBar(String message) {

        Snackbar snackbar = Snackbar
                .make(parentLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void setResult(int result) {

    }

    @Override
    public void setResult(int result, Intent data) {

    }

    @Override
    public void finish() {

    }

    @Override
    public void addEmptyLayout() {

    }
}