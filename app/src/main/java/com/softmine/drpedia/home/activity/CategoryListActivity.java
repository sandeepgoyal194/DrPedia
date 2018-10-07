package com.softmine.drpedia.home.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.softmine.drpedia.R;
import com.softmine.drpedia.expendablerecylerview.MultiCheckGenreAdapter;
import com.softmine.drpedia.home.CategoryListView;
import com.softmine.drpedia.home.di.ActivityModule;
import com.softmine.drpedia.home.di.CaseStudyComponent;
import com.softmine.drpedia.home.di.DaggerCaseStudyComponent;
import com.softmine.drpedia.home.di.GetCaseStudyListModule;
import com.softmine.drpedia.home.model.CategoryMainItem;
import com.softmine.drpedia.home.presentor.CategoryListPresentor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import frameworks.AppBaseApplication;
import frameworks.di.component.HasComponent;

public class CategoryListActivity extends AppCompatActivity implements CategoryListView , HasComponent<CaseStudyComponent>{

    private MultiCheckGenreAdapter adapter;
    List<CategoryMainItem> categoryMainItemList;
    HashMap<String,List<String>> map ;

    @Inject
    CategoryListPresentor categoryListPresentor;
    @BindView(R.id.rl_progress)
    RelativeLayout rl_progress;
    CaseStudyComponent caseStudyComponent;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        ButterKnife.bind(this);
        this.initializeInjector();
        this.getComponent().inject(this);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getClass().getSimpleName());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        categoryMainItemList = new ArrayList<>();
        map = new HashMap<>(categoryMainItemList.size());
        /*adapter = new MultiCheckGenreAdapter(categoryMainItemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        categoryListPresentor.setView(this);
        categoryListPresentor.loadCategoryList();

    }
    private void initializeInjector() {
        this.caseStudyComponent = DaggerCaseStudyComponent.builder()
                .baseAppComponent(((AppBaseApplication)getApplication())
                        .getBaseAppComponent())
                .getCaseStudyListModule(new GetCaseStudyListModule(this))
                .activityModule(getActivityModule())
                .build();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }


    @Override
    public void updateCategoryList(List<CategoryMainItem> categoryMainItemList) {

        this.categoryMainItemList = categoryMainItemList;
        if(this.categoryMainItemList.size()>0) {
            Log.d("categoryListItems" , "Size > 0");
            adapter = new MultiCheckGenreAdapter(categoryMainItemList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
        else
        {
            Log.d("categoryListItems" , "Size < 0");
        }
    }

    @Override
    public void showProgressBar() {
        this.rl_progress.setVisibility(View.VISIBLE);
        this.setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void showProgressBar(String message) {

    }

    @Override
    public void hideProgressBar() {
        this.rl_progress.setVisibility(View.GONE);
        this.setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void showSnackBar(String message) {

    }

    @Override
    public void addEmptyLayout() {

    }

    @Override
    public CaseStudyComponent getComponent() {
        return caseStudyComponent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }

    public void printPos()
    {
        List<Integer> initialCheckedPositions = adapter.getChildCheckController().getCheckedPositions();
        map = adapter.getChildCheckController().getMap();

        Log.d("checkedPos","traversing Map");

        for(Map.Entry entry  : map.entrySet())
        {
            Log.d("checkedPos","group index  "+entry.getKey());
            List<String> list = (List<String>) entry.getValue();
            for(String val :list)
            {
                Log.d("checkedPos","child index  "+val);
            }
        }

    }
}
