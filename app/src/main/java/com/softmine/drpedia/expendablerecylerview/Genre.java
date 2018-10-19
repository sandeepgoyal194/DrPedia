package com.softmine.drpedia.expendablerecylerview;

import com.softmine.drpedia.home.model.SubCategoryItem;

import java.util.List;

public class Genre{

  private int iconResId;

  public Genre(String title, List<SubCategoryItem> items, int iconResId) {
   // super(title, items);
    this.iconResId = iconResId;
  }

  public int getIconResId() {
    return iconResId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Genre)) return false;

    Genre genre = (Genre) o;

    return getIconResId() == genre.getIconResId();

  }

  @Override
  public int hashCode() {
    return getIconResId();
  }
}

