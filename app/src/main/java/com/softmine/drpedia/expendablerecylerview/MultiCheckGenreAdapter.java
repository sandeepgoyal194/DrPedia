package com.softmine.drpedia.expendablerecylerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softmine.drpedia.R;
import com.softmine.drpedia.home.model.CategoryMainItem;
import com.softmine.drpedia.home.model.SubCategoryItem;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MultiCheckGenreAdapter extends
    CheckableChildRecyclerViewAdapter<GenreViewHolder, MultiCheckArtistViewHolder> {


  int size , index=0;
  Map<Integer,Integer> groupINdexMap = new TreeMap<>();

  public MultiCheckGenreAdapter(List<CategoryMainItem> groups) {
    super(groups);
    size = groups.size();
    for(CategoryMainItem num : groups)
    {
      groupINdexMap.put(index , num.getItems().size());
      index = index+num.getItems().size()+1;
    }
    Log.d("categoryListItems" , "MultiCheckGenreAdapter size : "+groups.size());
  }


  @Override
  public MultiCheckArtistViewHolder onCreateCheckChildViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_multicheck_artist, parent, false);
    return new MultiCheckArtistViewHolder(view);
  }

  @Override
  public void onBindCheckChildViewHolder(MultiCheckArtistViewHolder holder, int position,
      CheckedExpandableGroup group, int childIndex) {
    final SubCategoryItem artist = (SubCategoryItem) group.getItems().get(childIndex);
    Log.d("categoryListItems" , "sub title  "+artist.getSubtype());
    holder.setArtistName(artist.getSubtype());
  }

  @Override
  public GenreViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_genre, parent, false);
    return new GenreViewHolder(view);
  }

  @Override
  public void onBindGroupViewHolder(GenreViewHolder holder, int flatPosition,
      ExpandableGroup group) {
    Log.d("categoryListItems" , "main title  "+group.getTitle());
    holder.setGenreTitle(group);
  }

  public void toggleAllGroup()
  {
    for(Integer num : groupINdexMap.keySet())
    {
      if(!isGroupExpanded(num))
      {
        toggleGroup(num);
      }
    }
  }


}
