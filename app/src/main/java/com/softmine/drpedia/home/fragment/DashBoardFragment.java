package com.softmine.drpedia.home.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softmine.drpedia.R;
import com.softmine.drpedia.home.CaseListView;
import com.softmine.drpedia.home.adapter.CaseListAdapter;
import com.softmine.drpedia.home.di.CaseStudyComponent;
import com.softmine.drpedia.home.model.CaseItem;
import com.softmine.drpedia.home.model.CaseMediaItem;
import com.softmine.drpedia.home.presentor.CaseListPresentor;
import com.softmine.drpedia.home.service.UploadService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import frameworks.di.component.HasComponent;


public class DashBoardFragment extends Fragment implements CaseListView, SwipeRefreshLayout.OnRefreshListener {

    public interface CaseListListener {
        void onCaseClicked(final CaseItem userModel);
    }

    private CaseListListener caseListListener;
    @Inject
    CaseListAdapter caseListAdapter;
    @Inject
    CaseListPresentor caseListPresentor;

    @BindView(R.id.list_case) RecyclerView rv_users;
    @BindView(R.id.rl_progress) RelativeLayout rl_progress;
    @BindView(R.id.rl_retry) RelativeLayout rl_retry;
    @BindView(R.id.bt_retry) Button bt_retry;

    @BindView(R.id.nodata_tv)
    TextView emptyTv;

    @BindView(R.id.list_container)
    RelativeLayout container;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    BroadcastReceiver receiver;

    public DashBoardFragment() {
        // Required empty public constructor
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d("loginresponse","onCreate called");
        super.onCreate(savedInstanceState);
        this.getComponent(CaseStudyComponent.class).inject(this);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra(UploadService.UPLOAD_MSG);
                loadCaseStudyList();
            }
        };
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

        Log.d("loginresponse","onCreateView called");

        final View fragmentView =  inflater.inflate(R.layout.fragment_home_case_list, container, false);
        ButterKnife.bind(this, fragmentView);
        setupRecyclerView();
        swipeContainer.setOnRefreshListener(this);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("loginresponse","onViewCreated called");
        this.caseListPresentor.setView(this);
       /* if (savedInstanceState == null) {
            this.loadCaseStudyList();
        }*/
    }

    @Override
    public void onRefresh() {
        loadCaseStudyList();
    }

    private void loadCaseStudyList() {

        Log.d("loginresponse","loadCaseStudyList fragment");

        this.caseListPresentor.initialize();
    }

    @Override
    public void onStart() {

        super.onStart();
        Log.d("loginresponse","onStart fragment");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((receiver), new IntentFilter(UploadService.UPLOAD_RESULT));
    }

    @Override
    public void onResume() {

        super.onResume();
        Log.d("loginresponse","onResume fragment");
        this.loadCaseStudyList();
    }

    @Override
    public void onStop() {

        super.onStop();
        Log.d("loginresponse","onStop fragment");
        // caseListAdapter=null;
        // caseListPresentor=null;
        //this.caseListAdapter.setUsersCollection(null);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    @Override
    public void onDestroy() {
        Log.d("loginresponse","onDestroy called");
        super.onDestroy();
        caseListAdapter=null;
        caseListPresentor=null;
        this.rv_users.setAdapter(null);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("loginresponse","onViewStateRestored fragment");
    }

    @OnClick(R.id.bt_retry)
    void onButtonRetryClick() {
        DashBoardFragment.this.loadCaseStudyList();
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
                    if (DashBoardFragment.this.caseListPresentor != null && userModel != null) {
                        DashBoardFragment.this.caseListPresentor.onUserClicked(userModel);
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


    static final Comparator<CaseItem> byDate = new Comparator<CaseItem>() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public int compare(CaseItem org1, CaseItem org2) {
            java.util.Date d1 = null;
            java.util.Date d2 = null;
            try {
                d1 = sdf.parse(org1.getCreated_on());
                d2 = sdf.parse(org2.getCreated_on());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //return (d1.getTime() > d2.getTime() ? -1 : 1);     //descending
            return (d1.getTime() > d2.getTime() ? 1 : -1);     //ascending
        }
    };

    @Override
    public void renderCaseList(Collection<? extends CaseItem> caseStudyCollection) {
        Log.d("loginresponse","caseStudyCollection size"+caseStudyCollection.size());

        Collections.sort((List<CaseItem>) caseStudyCollection,Collections.reverseOrder(byDate));

        for(CaseItem caseItem : caseStudyCollection)
        {
            //Log.d("casedetailresponse", "date=="+caseItem.getCreated_on());
            Log.d("loginresponse", "==================");

            for (CaseMediaItem item: caseItem.getPostPicUrl())
            {
                Log.d("loginresponse","image url===="+item.getImage());
                Log.d("loginresponse","video url===="+item.getVideo());
                Log.d("loginresponse","thumbnail url===="+item.getThumbnail());
            }
        }

        if (caseStudyCollection != null) {
            Log.d("loginresponse", "size=="+caseStudyCollection.size());
            Log.d("loginresponse", "inside if ");

            if(caseStudyCollection.size()>0){
                emptyTv.setVisibility(View.GONE);
                this.caseListAdapter.setUsersCollection(caseStudyCollection);
            }
            else
            {
                Log.d("loginresponse", "inside else ");
                emptyTv.setVisibility(View.VISIBLE);
                this.caseListAdapter.setUsersCollection(caseStudyCollection);
            }
            swipeContainer.setRefreshing(false);
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
        Snackbar snackbar = Snackbar
                .make(container, message, Snackbar.LENGTH_LONG);
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
