package com.softmine.drpedia.home.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.softmine.drpedia.DoctorGuideBaseActivity;
import com.softmine.drpedia.R;
import com.softmine.drpedia.home.IViewCategoryListView;
import com.softmine.drpedia.home.adapter.GenreAdapter;
import com.softmine.drpedia.home.di.ActivityModule;
import com.softmine.drpedia.home.di.CaseStudyComponent;
import com.softmine.drpedia.home.di.DaggerCaseStudyComponent;
import com.softmine.drpedia.home.di.GetCaseStudyListModule;
import com.softmine.drpedia.home.model.CategoryMainItem;
import com.softmine.drpedia.home.model.CategoryMainItemResponse;
import com.softmine.drpedia.home.model.SubCategoryItem;
import com.softmine.drpedia.home.presentor.ViewCategoryListPresentor;
import com.softmine.drpedia.profile.activity.EditProfile;
import com.softmine.drpedia.profile.activity.Profile;
import com.softmine.drpedia.utils.GenreDataFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindBitmap;
import butterknife.BindView;
import butterknife.ButterKnife;
import frameworks.AppBaseApplication;

public class ViewCategoryListActivity extends DoctorGuideBaseActivity implements IViewCategoryListView{

    CaseStudyComponent caseStudyComponent;
    public GenreAdapter adapter;
    ArrayList<CategoryMainItemResponse> arraylist;
    LinearLayoutManager layoutManager;
    @Inject
    ViewCategoryListPresentor viewCategoryListPresentor;
    ArrayList<String> TAGS1;
    ArrayList<Integer> user_interest_list;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.no_interest_data_txt)
    TextView no_interest_category;

    List<CategoryMainItem> categoryMainItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category_list);
        ButterKnife.bind(this);
//        setmToolbar();
        this.initializeInjector();
        this.caseStudyComponent.inject(this);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  //      getSupportActionBar().setTitle(getClass().getSimpleName());

        layoutManager = new LinearLayoutManager(this);
        categoryMainItemList = new ArrayList<>();
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }


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
    protected void onStart() {
        super.onStart();
        TAGS1 = new ArrayList<>();
        user_interest_list = new ArrayList<>();
        viewCategoryListPresentor.setView(this);

        viewCategoryListPresentor.getUserInterest();
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

    @Override
    public void setUserInterestSize(List<CategoryMainItemResponse> categoryMainItemResponses) {
        categoryMainItemList.clear();
        TAGS1.clear();
        user_interest_list.clear();
        if(categoryMainItemResponses.size()>0)
        {
            no_interest_category.setVisibility(View.GONE);
            for(CategoryMainItemResponse res1 : categoryMainItemResponses) {

                CategoryMainItem mainItem = new CategoryMainItem(res1.getCategoryName(),res1.getSubCategory(),res1.getCategoryID());
                categoryMainItemList.add(mainItem);

                Log.d("userprofile1", "Main Category name===" + res1.getCategoryName());
                Log.d("userprofile1", "Main Category ID===" + res1.getCategoryID());

                for (SubCategoryItem item1 : res1.getSubCategory()) {
                    TAGS1.add(item1.getSubtype());
                    user_interest_list.add(item1.getSubtype_id());
                    Log.d("userprofile1", "sub Category name===" + item1.getSubtype());
                    Log.d("userprofile1", "sub Category ID===" + item1.getSubtype_id());
                    Log.d("userprofile1", "sub Category interest ID===" + item1.getIntrest_id());
                }
            }
           // adapter = new GenreAdapter(GenreDataFactory.makeGenres());
            adapter = new GenreAdapter(categoryMainItemList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else
        {
            no_interest_category.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(this, CategoryListActivity.class);
        i.putExtra("interest_request_type","2");
        i.putStringArrayListExtra("user_interest_list",TAGS1);
        i.putIntegerArrayListExtra("user_interest_id_list",user_interest_list);
        startActivityForResult(i , CategoryListActivity.update_interest_code);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CategoryListActivity.update_interest_code)
        {
            if(resultCode == CategoryListActivity.update_interest_response_code)
            {
                viewCategoryListPresentor.getUserInterest();
            }
        }
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
}
