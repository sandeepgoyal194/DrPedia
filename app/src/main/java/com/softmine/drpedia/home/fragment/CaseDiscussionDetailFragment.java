package com.softmine.drpedia.home.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.sachin.doctorsguide.R;
import com.softmine.drpedia.home.CaseDetailView;
import com.softmine.drpedia.home.adapter.CommentsOnPostListAdapter;
import com.softmine.drpedia.home.di.CaseStudyComponent;
import com.softmine.drpedia.home.glide.CircleTransform;
import com.softmine.drpedia.home.model.CaseItem;
import com.softmine.drpedia.home.model.CommentData;
import com.softmine.drpedia.home.presentor.CaseDetailPresentor;
import com.softmine.drpedia.utils.UserManager;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import frameworks.appsession.AppSessionManager;
import frameworks.di.component.HasComponent;
import frameworks.imageloader.ImageLoader;


public class CaseDiscussionDetailFragment extends Fragment implements CaseDetailView, TextWatcher {

    private static final String CASE_DETAIL = "CASE_DETAIL";
    CaseItem caseItemArg;
    @BindView(R.id.caseCoverPic)
    ImageView postPic;
    @BindView(R.id.comments_count)
    TextView comments_count;
    @BindView(R.id.like_count)
    TextView like_count;
    @BindView(R.id.bookmark_count)
    TextView bookmark_count;
    @BindView(R.id.img_like)
    ImageView like;
    @BindView(R.id.img_bookmart)
    ImageView bookmarkImg;
    @BindView(R.id.img_comment)
    ImageView openCommentList;

    @BindView(R.id.edt_txt_post_comment)
    EditText postComment;

    @BindView(R.id.user_profile_photo)
    ImageView loggedInUserProfilePic;

    @BindView(R.id.list_comments)
    RecyclerView listView;

    @Inject
    CaseDetailPresentor caseDetailPresentor;

    @BindView(R.id.parentLayout)
    LinearLayout parentLayout;

    @BindView(R.id.rl_progress)
    FrameLayout rl_progress;

    @Inject
    ImageLoader mImageLoader;

    @BindView(R.id.post_comment)
    ImageView post_comment_btn;

    @Inject
    AppSessionManager appSessionManager;

    boolean likeStatus , bookmarkStatus;

    @BindView(R.id.expand_text_view)
    ExpandableTextView expTv;

    @Inject
    CommentsOnPostListAdapter commentsOnPostListAdapter;

    public static CaseDiscussionDetailFragment forCaseItemDetail(CaseItem caseItem) {
        CaseDiscussionDetailFragment fragment = new CaseDiscussionDetailFragment();
        final Bundle arg = new Bundle();
        arg.putSerializable(CASE_DETAIL,caseItem);
        fragment.setArguments(arg);
        return fragment;
    }

    public CaseDiscussionDetailFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            caseItemArg = (CaseItem) getArguments().getSerializable(CASE_DETAIL);
        }
        this.getComponent(CaseStudyComponent.class).inject(this);
    }

    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View fragmentView = inflater.inflate(R.layout.fragment_case_discussion_detail, container, false);
        ButterKnife.bind(this,fragmentView);
        setupRecyclerView();
        postComment.addTextChangedListener(this);

        return fragmentView;

    }

    private void setupRecyclerView() {
        
        this.listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.listView.setAdapter(commentsOnPostListAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.caseDetailPresentor.setView(this);
        this.loadCaseDetail(caseItemArg);
    }

    private void loadCaseDetail(CaseItem caseItem)
    {
        expTv.setOnExpandStateChangeListener(new ExpandableTextView.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(TextView textView, boolean isExpanded) {
                Toast.makeText(getActivity(), isExpanded ? "Expanded" : "Collapsed", Toast.LENGTH_SHORT).show();
            }
        });

        expTv.setText(getString(R.string.dummy_text1));
     //   postDesc.setText(caseItem.getLong_desc());
        comments_count.setText(caseItem.getTotal_comment());
        like_count.setText(caseItem.getTotal_like());
        bookmark_count.setText(caseItem.getTotal_bookmark());

        mImageLoader.loadImage(caseItem.getPostPicUrl() , postPic);
        Glide.with(getActivity())
                .load(caseItem.getPostPicUrl()) // applying the image transformer
                .into(postPic);

        Glide.with(getActivity())
                .load(new UserManager(getActivity()).getUser().getPhotoUrl()) // add your image url
                .transform(new CircleTransform(getActivity())) // applying the image transformer
                .into(loggedInUserProfilePic);

        if(caseItem.getPostLikeStatus().equals("true")) {
            likeStatus=true;
            like.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.like_checked));
        }
        else{
            likeStatus=false;
            like.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.like));
        }

        if(caseItem.getPostBookmarkStatus().equals("true")){
            bookmarkStatus=true;
            bookmarkImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.checked_bookmark));}
        else{
            bookmarkStatus =false;
            bookmarkImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.bookmark));}

        setOpenCommentList();

    }

    @OnClick(R.id.img_like)
    void doLikeOrUnlikePost()
    {
        Log.d("imagelogs","image liked in fragment view");
     //   Toast.makeText(getActivity().getBaseContext(),"image liked in fragment view",Toast.LENGTH_LONG).show();
      //  this.caseDetailPresentor.doLikeorUnlikePost("true",this.caseItem.getPost_id());
        if(likeStatus)
            this.caseDetailPresentor.doLikeorUnlikePost("false",caseItemArg.getPost_id());
        else
            this.caseDetailPresentor.doLikeorUnlikePost("true",caseItemArg.getPost_id());
    }


    @OnClick(R.id.img_bookmart)
    void doBookmarkPost()
    {
        Log.d("imagelogs","image bookmarked in fragment view");
        //   Toast.makeText(getActivity().getBaseContext(),"image liked in fragment view",Toast.LENGTH_LONG).show();
        //  this.caseDetailPresentor.doLikeorUnlikePost("true",this.caseItem.getPost_id());
        if(bookmarkStatus)
            this.caseDetailPresentor.doBookmarkPost("false",caseItemArg.getPost_id());
        else
            this.caseDetailPresentor.doBookmarkPost("true",caseItemArg.getPost_id());
        //getCaseItemDetail();
    }

    @OnClick(R.id.post_comment)
    void uploadCommentOnPost()
    {
        Log.d("imagelogs","image commented in fragment view");
        String comment = postComment.getText().toString();
        Log.d("imagelogs","comment on post is====== "+comment);
        this.caseDetailPresentor.doUploadCommentOnPost(comment,caseItemArg.getPost_id());

       // getCaseItemDetail();
    }


    @OnClick(R.id.img_comment)
    void setOpenCommentList()
    {
        Log.d("imagelogs","setOpenCommentList in fragment view");
        this.caseDetailPresentor.loadAllComments(caseItemArg.getPost_id());
       // listView.setVisibility(View.VISIBLE);
    }

    void getCaseItemDetail()
    {
        this.caseDetailPresentor.getCaseItemDetail(caseItemArg.getPost_id());
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
        this.rl_progress.setVisibility(View.GONE);
    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void showSnackBar(String message) {

        Snackbar snackbar = Snackbar
                .make(parentLayout, message, Snackbar.LENGTH_LONG);
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

    @Override
    public void updateLikeOrUnlikePost(boolean status) {

        Log.d("imagelogs","image has liked successfully");
     //   showSnackBar("image has liked successfully");
        if(likeStatus){
          //  likeStatus = false;
           // like.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.like));
            showSnackBar("image has Disliked successfully");
        }
        else{
           // likeStatus=true;
           // like.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.like_checked));
            showSnackBar("image has liked successfully");
        }

        getCaseItemDetail();
    }

    @Override
    public void updateBookmarkUserPost(boolean status) {
        Log.d("imagelogs","image has bookmarked successfully");
      //  showSnackBar("image has bookmarked successfully");
        if(bookmarkStatus){
          //  bookmarkStatus=false;
           // bookmarkImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.bookmark));
            showSnackBar("bookmarked removed from image successfully");

        }
        else{
           // bookmarkStatus=true;
          //  bookmarkImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.checked_bookmark));
            showSnackBar("image has bookmarked successfully");
        }

        getCaseItemDetail();
}

    @Override
    public void UpdateCommentOnPost(boolean status) {
        Log.d("imagelogs","image liked in UpdateCommentOnPost view");
        if(status)
        {
         postComment.setText("");
         showSnackBar("Comment has posted successfully on Post");
         setOpenCommentList();
        }

        getCaseItemDetail();
    }

    @Override
    public void setAllCommentsOnPost(Collection<CommentData> commentsOnPostCollection) {

        Log.d("imagelogs","collection size==="+commentsOnPostCollection.size());

        if (commentsOnPostCollection != null) {
            listView.setVisibility(View.VISIBLE);
            this.commentsOnPostListAdapter.setAllCommentOnPostCollection(commentsOnPostCollection);
        }
    }

    @Override
    public void updateCaseItemDetail(CaseItem caseItem) {
        loadCaseDetail(caseItem);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
            post_comment_btn.setVisibility(View.VISIBLE);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
