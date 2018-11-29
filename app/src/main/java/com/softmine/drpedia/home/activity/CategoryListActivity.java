package com.softmine.drpedia.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import com.softmine.drpedia.home.model.SubCategoryItem;
import com.softmine.drpedia.home.presentor.CategoryListPresentor;
import com.softmine.drpedia.profile.activity.Profile;

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
    Menu mMenu;
    @Inject
    CategoryListPresentor categoryListPresentor;
    @BindView(R.id.rl_progress)
    RelativeLayout rl_progress;
    CaseStudyComponent caseStudyComponent;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    @BindView(R.id.categoryListContainer)
    ConstraintLayout categoryListContainer;

    int mScreenType = OnScreenChangeListener.SCREEN_TYPE_CREATE;
    String interest_request_type;
    ArrayList<String> user_interest_list;
    ArrayList<Integer> user_interest_id_list;

    public static final int update_interest_code=101;
    public static final int update_interest_response_code=102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        ButterKnife.bind(this);
        this.initializeInjector();
        this.getComponent().inject(this);
        getSupportActionBar().setTitle(getClass().getSimpleName());

        interest_request_type =  getIntent().getStringExtra("interest_request_type");
        user_interest_list = getIntent().getStringArrayListExtra("user_interest_list");
        user_interest_id_list = getIntent().getIntegerArrayListExtra("user_interest_id_list");
        if(interest_request_type!=null)
        {
            Log.d("categoryListItems" , "Interest request type = "+interest_request_type);
            mScreenType = Integer.parseInt(interest_request_type);
            Log.d("categoryListItems" , "Interest request type screen type = "+mScreenType);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        categoryMainItemListResponse = new ArrayList<>();
        categoryMainItemList = new ArrayList<>();
        map = new HashMap<>(categoryMainItemListResponse.size());

    }

    @Override
    protected void onStart() {
        super.onStart();
        categoryListPresentor.setView(this);
        categoryListPresentor.loadCategoryList();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mMenu = menu;
        setMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    private void setMenu() {
        if (mScreenType == OnScreenChangeListener.SCREEN_TYPE_CREATE) {
            getMenuInflater().inflate(R.menu.menu_upload_case, mMenu);
        } else if (mScreenType == OnScreenChangeListener.SCREEN_TYPE_UPDATE) {
            getMenuInflater().inflate(R.menu.menu_update_user_interest, mMenu);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.menu_itm_signup:
                onCreateUserInterest();
              //  printPos();
                break;
            case R.id.menu_item_update_interest:
                onUpdateUserInterest();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        invalidateOptionsMenu();
        return true;
    }

    private void onCreateUserInterest()
    {
        mMenu.clear();
      //  mScreenType = OnScreenChangeListener.SCREEN_TYPE_CREATE;
        Log.d("categoryListItems" , "create interest called");
        Log.d("categoryListItems","printing latest user interst");
        ArrayList<Integer> update_category_SubType_list = new ArrayList<>();
        List<Integer> initialCheckedPositions = adapter.getChildCheckController().getCheckedPositions();
        map = adapter.getChildCheckController().getMap();
        Log.d("categoryListItems","User updated interst List");
        for(Map.Entry entry  : map.entrySet())
        {
            Log.d("categoryListItems","group index  "+entry.getKey());
            List<Integer> list = (List<Integer>) entry.getValue();
            for(Integer val :list)
            {
                update_category_SubType_list.add(val);
                Log.d("categoryListItems","child index  "+val);
            }
        }

        categoryListPresentor.createUserInterest(update_category_SubType_list);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onUpdateUserInterest()
    {
        mMenu.clear();
     //   mScreenType = OnScreenChangeListener.SCREEN_TYPE_UPDATE;
        Log.d("categoryListItems" , "update interest called");
        printPos();
    }

    public interface OnScreenChangeListener {
        public static final int SCREEN_TYPE_CREATE = 1;
        public static final int SCREEN_TYPE_UPDATE = 2;
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

    Map<Integer , ArrayList<Integer>> compare = new HashMap<>();


    @RequiresApi(api = Build.VERSION_CODES.N)
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

            int outer=0;
            for(CategoryMainItemResponse item : categoryMainItemListResponse)
            {
                int inner=0;
                ArrayList<Integer> childIndex = new ArrayList<>();
               for(final SubCategoryItem subItem : item.getSubCategory())
               {
                  // if(user_interest_list.stream().anyMatch(str -> str.trim().equals(subItem.getSubtype())))
                   if(user_interest_id_list.stream().anyMatch(str -> str.equals(subItem.getSubtype_id())))
                   {
                       Log.d("categoryListItems" , "group id is   "+item.getCategoryName());
                       Log.d("categoryListItems" , "child id is   "+subItem.getSubtype());
                       //compare.put(outer , inner);
                       childIndex.add(inner);
                   }
                   inner++;
               }
                compare.put(outer , childIndex);
               outer++;
            }

            for(Map.Entry entry  : compare.entrySet())
            {
                Log.d("categoryListItems","group index  "+entry.getKey());
                List<Integer> list = (List<Integer>) entry.getValue();
                for(Integer val :list) {
                    Log.d("categoryListItems", "child index  " + val);
                    if(mScreenType==OnScreenChangeListener.SCREEN_TYPE_UPDATE)
                    {
                        adapter.checkChild(true , Integer.parseInt(entry.getKey().toString()) , val);
                    }
                }
            }

            adapter.notifyDataSetChanged();
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
    public void startProfileActivity() {
        Log.d("subTypePos","startActivity DashBoardActivity");
        setResult(update_interest_response_code);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void printPos()
    {
        Log.d("categoryListItems","printing latest user interst");
        ArrayList<Integer> update_category_SubType_list = new ArrayList<>();
        List<Integer> initialCheckedPositions = adapter.getChildCheckController().getCheckedPositions();
        map = adapter.getChildCheckController().getMap();
        Log.d("categoryListItems","User updated interst List");
        for(Map.Entry entry  : map.entrySet())
        {
            Log.d("categoryListItems","group index  "+entry.getKey());
            List<Integer> list = (List<Integer>) entry.getValue();
            for(Integer val :list)
            {
                update_category_SubType_list.add(val);
                Log.d("categoryListItems","child index  "+val);
            }
        }

        Log.d("categoryListItems","User previous interst list");

        for(Integer interest : user_interest_id_list)
        {
            Log.d("categoryListItems","child index  "+interest);
        }

        Log.d("categoryListItems","Calculating added and removed interest");
        ArrayList<Integer> added_interest = new ArrayList<>();
        for(Integer latest_interest : update_category_SubType_list)
        {
            if(user_interest_id_list.stream().anyMatch(str -> str.equals(latest_interest)))
            {
                user_interest_id_list.remove(latest_interest);
            }
            else
            {
                added_interest.add(latest_interest);
            }
        }

        Log.d("categoryListItems","Added interest");

        for(Integer latest_interest : added_interest)
        {
            Log.d("categoryListItems","child index  "+latest_interest);
        }

        Log.d("categoryListItems","Removed interest");

        for(Integer latest_interest : user_interest_id_list)
        {
            Log.d("categoryListItems","child index  "+latest_interest);
        }




        categoryListPresentor.updateUserInterest(added_interest , user_interest_id_list);

    }
}
