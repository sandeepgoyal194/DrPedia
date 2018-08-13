package com.softmine.drpedia.home.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.softmine.drpedia.R;
import com.softmine.drpedia.home.di.ActivityModule;
import com.softmine.drpedia.home.di.CaseStudyComponent;
import com.softmine.drpedia.home.di.DaggerCaseStudyComponent;
import com.softmine.drpedia.home.di.GetCaseStudyListModule;
import com.softmine.drpedia.home.fragment.CaseDiscussionDetailFragment;
import com.softmine.drpedia.home.model.CaseItem;

import frameworks.AppBaseApplication;
import frameworks.di.component.HasComponent;

public class CaseDiscussionDetail extends AppCompatActivity implements HasComponent<CaseStudyComponent> {

    private CaseStudyComponent caseStudyComponent;
    CaseItem caseItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_discussion_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intentExtras = getIntent();
      //  Bundle bundle = intentExtras.getExtras();
        caseItem = (CaseItem) intentExtras.getSerializableExtra(DashBoardActivity.CASE_DETAIL);
        getSupportActionBar().setTitle(caseItem.getShort_desc());
        this.addFragment(savedInstanceState);
        this.initializeInjector();
    }

    private void addFragment(Bundle savedInstanceState)
    {
        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragmentDetailContainer, CaseDiscussionDetailFragment.forCaseItemDetail(caseItem));;
            fragmentTransaction.commit();
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

    @Override
    public CaseStudyComponent getComponent() {
        return caseStudyComponent;
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
