package com.softmine.drpedia.utils;

import com.softmine.drpedia.R;
import com.softmine.drpedia.expendablerecylerview.Genre;
import com.softmine.drpedia.home.model.CategoryMainItem;
import com.softmine.drpedia.home.model.SubCategoryItem;

import java.util.Arrays;
import java.util.List;

public class GenreDataFactory {

    public static List<CategoryMainItem> makeGenres() {
        return Arrays.asList(makeRockGenre(),
                makeRockGenre(),
                makeRockGenre(),
                makeRockGenre(),
                makeRockGenre());
    }

    public static CategoryMainItem makeRockGenre() {
        return new CategoryMainItem("Rock", makeRockArtists(), R.drawable.ic_electric_guitar);
    }


    public static List<SubCategoryItem> makeRockArtists() {
        SubCategoryItem queen = new SubCategoryItem(1 ,"Queen");
        SubCategoryItem queen2 = new SubCategoryItem(2 ,"king");
        SubCategoryItem queen3 = new SubCategoryItem(3 ,"child");
        SubCategoryItem queen4 = new SubCategoryItem(4 ,"hello");

        return Arrays.asList(queen, queen2, queen3, queen4);
    }
}
