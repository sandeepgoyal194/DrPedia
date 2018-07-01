package com.softmine.drpedia.home;

import com.softmine.drpedia.home.model.CaseItem;
import com.softmine.drpedia.home.model.CommentData;

import java.util.Collection;

import frameworks.basemvp.IView;

public interface CaseDetailView extends IView {

    void updateLikeOrUnlikePost(boolean status);
    void updateBookmarkUserPost(boolean status);
    void UpdateCommentOnPost(boolean status);
    void setAllCommentsOnPost(Collection<CommentData> caseStudyCollection);
    void updateCaseItemDetail(CaseItem caseItem);
}
