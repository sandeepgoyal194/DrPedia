package com.softmine.drpedia.home.model;

import com.google.gson.annotations.SerializedName;
import com.softmine.drpedia.home.model.BookmarkItem;

import java.io.Serializable;
import java.util.List;

public class UserBookmarkListResponse implements Serializable{

    @SerializedName(value="Bookmarks")
    List<BookmarkItem> data;

    public List<BookmarkItem> getBookmarkData() {
        return data;
    }

    public void setBookmarkData(List<BookmarkItem> bookmarkData) {
        this.data = bookmarkData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName(value="message")
    String message;
}
