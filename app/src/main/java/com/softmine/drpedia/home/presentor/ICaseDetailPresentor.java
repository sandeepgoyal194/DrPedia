package com.softmine.drpedia.home.presentor;

public interface ICaseDetailPresentor {

    public void doLikeorUnlikePost(String likeStatus,int postID);
    public void doBookmarkPost(String bookmarkStatus, int postID);
    public void doUploadCommentOnPost(String comment , int postID);
    public void loadAllComments(int postID);
    public void getCaseItemDetail(int postID);

}
