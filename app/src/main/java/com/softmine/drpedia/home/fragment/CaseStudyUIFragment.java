package com.softmine.drpedia.home.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.sachin.doctorsguide.R;
import com.softmine.drpedia.home.CaseListView;
import com.softmine.drpedia.home.adapter.CaseListAdapter;
import com.softmine.drpedia.home.di.CaseStudyComponent;
import com.softmine.drpedia.home.model.CaseItem;
import com.softmine.drpedia.home.presentor.CaseListPresentor;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import frameworks.di.component.HasComponent;


public class CaseStudyUIFragment extends Fragment implements CaseListView{

    public interface CaseListListener {
        void onCaseClicked(final CaseItem userModel);
    }

    private CaseListListener caseListListener;
    @Inject
    CaseListAdapter caseListAdapter;
    @Inject
    CaseListPresentor caseListPresentor;

    @BindView(R.id.list_case) RecyclerView rv_users;
    @BindView(R.id.rl_progress) FrameLayout rl_progress;
    @BindView(R.id.rl_retry) RelativeLayout rl_retry;
    @BindView(R.id.bt_retry) Button bt_retry;

    public CaseStudyUIFragment() {
        // Required empty public constructor
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getComponent(CaseStudyComponent.class).inject(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CaseListListener) {
            this.caseListListener = (CaseListListener) activity;
        }
    }

    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Log.d("loginresponse1","onCreateView called");

        final View fragmentView =  inflater.inflate(R.layout.fragment_case_list, container, false);
        ButterKnife.bind(this, fragmentView);
        setupRecyclerView();
        return fragmentView;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("loginresponse1","onViewCreated called");
        this.caseListPresentor.setView(this);
       /* if (savedInstanceState == null) {
            this.loadCaseStudyList();
        }*/
    }

    private void loadCaseStudyList() {

        Log.d("loginresponse","loadCaseStudyList fragment");

        this.caseListPresentor.initialize();
    }

    @Override
    public void onStart() {

        super.onStart();
        Log.d("loginresponse1","onStart fragment");
    }

    @Override
    public void onResume() {

        super.onResume();
        Log.d("loginresponse1","onResume fragment");
        this.loadCaseStudyList();
    }

    @Override
    public void onStop() {

        super.onStop();
        Log.d("loginresponse1","onStop fragment");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("loginresponse1","onViewStateRestored fragment");
    }

    @OnClick(R.id.bt_retry)
    void onButtonRetryClick() {
        CaseStudyUIFragment.this.loadCaseStudyList();
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
                @Override public void onUserItemClicked(CaseItem userModel) {
                    if (CaseStudyUIFragment.this.caseListPresentor != null && userModel != null) {
                        CaseStudyUIFragment.this.caseListPresentor.onUserClicked(userModel);
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
    public void renderCaseList(Collection<CaseItem> caseStudyCollection) {
        Log.d("loginresponse","caseStudyCollection size"+caseStudyCollection.size());
        if (caseStudyCollection != null) {
            this.caseListAdapter.setUsersCollection(caseStudyCollection);
        }
    }

    @Override
    public void viewUser(CaseItem CaseItem) {
        if (this.caseListListener != null) {
            this.caseListListener.onCaseClicked(CaseItem);
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
        Log.d("loginresponse","hide loading====================");
        this.rl_progress.setVisibility(View.GONE);
        this.getActivity().setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void showSnackBar(String message) {

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
