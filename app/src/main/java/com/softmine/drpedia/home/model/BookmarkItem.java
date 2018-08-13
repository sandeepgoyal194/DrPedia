package com.softmine.drpedia.home.model;

import java.io.Serializable;

public class BookmarkItem extends CaseItem implements Serializable {

    int bookmark_id;

    public int getBookmark_id() {
        return bookmark_id;
    }

    public void setBookmark_id(int bookmark_id) {
        this.bookmark_id = bookmark_id;
    }
}
