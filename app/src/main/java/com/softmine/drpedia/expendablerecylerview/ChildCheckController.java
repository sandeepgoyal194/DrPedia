package com.softmine.drpedia.expendablerecylerview;

import android.util.Log;
import android.widget.Checkable;
import android.widget.ExpandableListView;

import com.softmine.drpedia.home.model.SubCategoryItem;
import com.thoughtbot.expandablerecyclerview.models.ExpandableList;
import com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChildCheckController {

  private ExpandableList expandableList;
  private OnChildrenCheckStateChangedListener childrenUpdateListener;
  private List<Integer> initialCheckedPositions;
  HashMap<String,List<String>> map ;
  public ChildCheckController(ExpandableList expandableList,
                              OnChildrenCheckStateChangedListener listener) {
    this.expandableList = expandableList;
    map = new HashMap<>(this.expandableList.groups.size());
    this.childrenUpdateListener = listener;
    initialCheckedPositions = getCheckedPositions();

  }

  public List<Integer> getInitialCheckedPositions()
  {
    return getCheckedPositions();
  }

  /**
   * Triggered by a click event on a {@link CheckableChildViewHolder} causing the {@link Checkable}
   * object to change checked states
   *
   * @param checked The current checked state of the view
   * @param listPosition The flat position (raw index) of the {@link CheckableChildViewHolder}
   */
  public void onChildCheckChanged(boolean checked, ExpandableListPosition listPosition) {
    CheckedExpandableGroup group =
        (CheckedExpandableGroup) expandableList.groups.get(listPosition.groupPos);
    group.onChildClicked(listPosition.childPos, checked);
    if (childrenUpdateListener != null) {
      childrenUpdateListener.updateChildrenCheckState(
          expandableList.getFlattenedFirstChildIndex(listPosition),
          expandableList.getExpandableGroupItemCount(listPosition));
    }
  }

  public void checkChild(boolean checked, int groupIndex, int childIndex) {
    CheckedExpandableGroup group = (CheckedExpandableGroup) expandableList.groups.get(groupIndex);
    group.onChildClicked(childIndex, checked);
    if (childrenUpdateListener != null) {
      //only update children check states if group is expanded
      boolean isGroupExpanded = expandableList.expandedGroupIndexes[groupIndex];
      if (isGroupExpanded) {
        childrenUpdateListener.updateChildrenCheckState(
            expandableList.getFlattenedFirstChildIndex(groupIndex), group.getItemCount());
      }
    }
  }

  /**
   * @param listPosition the ExpandableListPosition representation of a child list item
   * @return The current checked state of the view
   */
  public boolean isChildChecked(ExpandableListPosition listPosition) {
    CheckedExpandableGroup group =
        (CheckedExpandableGroup) expandableList.groups.get(listPosition.groupPos);
    return group.isChildChecked(listPosition.childPos);
  }

  /**
   * @return list of indexes of all checked child items
   */
  public List<Integer> getCheckedPositions() {
    List<Integer> selected = new ArrayList<>();
    for (int i = 0; i < expandableList.groups.size(); i++)
    {
      if (expandableList.groups.get(i) instanceof CheckedExpandableGroup)
      {
        CheckedExpandableGroup group = (CheckedExpandableGroup) expandableList.groups.get(i);
        Log.d("checkedPos","group number "+i+" and name "+group.getTitle());
        List<String> selected1 = new ArrayList<>();
        for (int j = 0; j < group.getItemCount(); j++)
        {
          if (group.isChildChecked(j))
          {
            SubCategoryItem artist = (SubCategoryItem) group.getItems().get(j);
            Log.d("checkedPos","child number "+j+" and name "+artist.getSubtype());
            long packedPosition = ExpandableListView.getPackedPositionForChild(i, j);
            selected.add(expandableList.getFlattenedChildIndex(packedPosition));
            selected1.add(artist.getSubtype());
          }
        }
        if(group.getItemCount()>0)
        map.put(group.getTitle(),selected1);
      }
    }
    return selected;
  }


  public HashMap<String, List<String>> getMap() {
    return map;
  }

  /**
   * @return true if the checked state of any of the child items has changed since this class was
   * initialized
   */
  public boolean checksChanged() {
    return !initialCheckedPositions.equals(getCheckedPositions());
  }

  /**
   * Clear any choices previously checked
   */
  public void clearCheckStates() {
    for (int i = 0; i < expandableList.groups.size(); i++) {
      CheckedExpandableGroup group = (CheckedExpandableGroup) expandableList.groups.get(i);
      group.clearSelections();
    }
  }
}
