package com.softmine.drpedia.home.adapter;

import android.view.View;
import android.widget.TextView;

import com.softmine.drpedia.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class SubCategoryItemViewHolder extends ChildViewHolder {

    private TextView childTextView;

    public SubCategoryItemViewHolder(View itemView) {
        super(itemView);
        childTextView = (TextView) itemView.findViewById(R.id.list_item_artist_name);
    }

    public void setSubCategoryName(String name) {
        childTextView.setText(name);
    }
}
