package com.softmine.drpedia.home.customview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.softmine.drpedia.R;
import com.softmine.drpedia.home.adapter.MediaItemListAdapter;


public class CaseMediaItemHorizontalRecyclerView extends LinearLayout {

    private RecyclerView mRecyclerView;
    private ImageView mProgressBar;

    public CaseMediaItemHorizontalRecyclerView(Context context) {
        super(context);
    }

    public CaseMediaItemHorizontalRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }
    public CaseMediaItemHorizontalRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CaseMediaItemHorizontalRecyclerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.horizontal_recycler_view_mediaitem, this);
        mRecyclerView = (RecyclerView) itemView.findViewById(R.id.horizontal_recycler_view_item);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mProgressBar = (ImageView) itemView.findViewById(R.id.progress_bar);
    }

    public void setAdapter(MediaItemListAdapter adapter) {
        Log.d("hrview","setting adapter");
        mRecyclerView.setAdapter(adapter);
    }

    public void onListLoaded() {
        Log.d("hrview","progress bar view gone");
        mProgressBar.setVisibility(View.GONE);
    }
}
