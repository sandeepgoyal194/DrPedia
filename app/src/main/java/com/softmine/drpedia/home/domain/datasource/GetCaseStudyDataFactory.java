package com.softmine.drpedia.home.domain.datasource;

import android.util.Log;

import javax.inject.Inject;

public class GetCaseStudyDataFactory {

    private GetCaseStudyDataSource getCaseStudyDataSource;

    @Inject
    public GetCaseStudyDataFactory(GetCaseStudyDataSource getCaseStudyDataSource)
    {
        this.getCaseStudyDataSource = getCaseStudyDataSource;
    }

    public ICaseStudyDataSource createCaseStudyDataSource()
    {
        Log.d("imagelogs","image liked in createCaseStudyDataSource  factory view");
        return this.getCaseStudyDataSource;
    }
}
