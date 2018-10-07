package com.softmine.drpedia.home;



import com.softmine.drpedia.home.model.CaseItem;

import java.util.Collection;

import frameworks.basemvp.IView;

public interface CaseListView extends IView {

    public void showRetry();
    public void hideRetry();
    public void renderCaseList(Collection<? extends CaseItem> caseStudyCollection);
    void viewUser(CaseItem CaseItem);

}
