package com.softmine.drpedia.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.softmine.drpedia.home.model.CategoryMainItemResponse;
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
    List<CategoryMainItemResponse> categoryMainItemListResponse;
    List<CategoryMainItem> categoryMainItemList;
    HashMap<Integer,List<Integer>> map ;

    @Inject
    CategoryListPresentor categoryListPresentor;
    @BindView(R.id.rl_progress)
    RelativeLayout rl_progress;
    CaseStudyComponent caseStudyComponent;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    @BindView(R.id.categoryListContainer)
    ConstraintLayout categoryListContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        ButterKnife.bind(this);
        this.initializeInjector();
        this.getComponent().inject(this);
        getSupportActionBar().setTitle(getClass().getSimpleName());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        categoryMainItemListResponse = new ArrayList<>();
        categoryMainItemList = new ArrayList<>();
        map = new HashMap<>(categoryMainItemListResponse.size());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_upload_case,menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.menu_itm_signup:
                printPos();
                break;
        }
        return super.onOptionsItemSelected(item);
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
    public void updateCategoryList(List<CategoryMainItemResponse> categoryMainItemListResponse) {

        this.categoryMainItemListResponse = categoryMainItemListResponse;
        categoryMainItemList.clear();
        if(this.categoryMainItemListResponse.size()>0) {
            Log.d("categoryListItems" , "Size > 0");

            for(CategoryMainItemResponse item : categoryMainItemListResponse)
            {
                CategoryMainItem mainItem = new CategoryMainItem(item.getCategoryName(),item.getSubCategory(),item.getCategoryID());
                categoryMainItemList.add(mainItem);
            }

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
    public void startActivity() {
        Log.d("subTypePos","startActivity DashBoardActivity");
        Intent dashBoardIntent = new Intent(this, DashBoardActivity.class);
        startActivity(dashBoardIntent);
        finish();
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
        Snackbar snackbar = Snackbar
                .make(categoryListContainer, message, Snackbar.LENGTH_LONG);
        snackbar.show();
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
//        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
       // adapter.onRestoreInstanceState(savedInstanceState);
    }

    public void printPos()
    {
        ArrayList<Integer> category_SubType_list = new ArrayList<>();
        List<Integer> initialCheckedPositions = adapter.getChildCheckController().getCheckedPositions();
        map = adapter.getChildCheckController().getMap();
        Log.d("subTypePos","traversing Map");
        for(Map.Entry entry  : map.entrySet())
        {
            Log.d("subTypePos","group index  "+entry.getKey());
            List<Integer> list = (List<Integer>) entry.getValue();
            for(Integer val :list)
            {
                category_SubType_list.add(val);
                Log.d("subTypePos","child index  "+val);
            }
        }

        categoryListPresentor.createUserInterest(category_SubType_list);

    }
}
