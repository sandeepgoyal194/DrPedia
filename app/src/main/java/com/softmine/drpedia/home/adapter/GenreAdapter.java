package com.softmine.drpedia.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softmine.drpedia.R;
import com.softmine.drpedia.expendablerecylerview.Genre;
import com.softmine.drpedia.expendablerecylerview.GenreViewHolder;
import com.softmine.drpedia.home.model.SubCategoryItem;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class GenreAdapter extends ExpandableRecyclerViewAdapter<CategoryMainItemViewHolder, SubCategoryItemViewHolder> {

    public GenreAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public CategoryMainItemViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_genre, parent, false);
        return new CategoryMainItemViewHolder(view);
    }

    @Override
    public SubCategoryItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_subcategory, parent, false);
        return new SubCategoryItemViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(SubCategoryItemViewHolder holder, int flatPosition,
                                      ExpandableGroup group, int childIndex) {

        final SubCategoryItem artist = (SubCategoryItem) group.getItems().get(childIndex);
        holder.setSubCategoryName(artist.getSubtype());
    }

    @Override
    public void onBindGroupViewHolder(CategoryMainItemViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setGenreTitle(group);
    }

}
