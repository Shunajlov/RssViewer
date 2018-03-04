package com.ihavenodomain.rssviewer.model.rss;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.ihavenodomain.rssviewer.model.db.FeedTypeConverter;

import java.util.List;


/**
 * I've planned to use one table for each entity,
 * but for easier lists serialization i used {@link FeedTypeConverter}.
 * So in this case, list of the news (items) is stored like one text field in the database
 */
@Entity(tableName = "rss")
@TypeConverters(FeedTypeConverter.class)
public class RSS {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("feed")
    private com.ihavenodomain.rssviewer.model.rss.Feed Feed;

    @SerializedName("items")
    private List<Item> Items;

    @SerializedName("status")
    private String Status;

    private boolean isActive = true;

    public com.ihavenodomain.rssviewer.model.rss.Feed getFeed() {
        return Feed;
    }

    public void setFeed(com.ihavenodomain.rssviewer.model.rss.Feed feed) {
        Feed = feed;
    }

    public List<Item> getItems() {
        return Items;
    }

    public void setItems(List<Item> items) {
        Items = items;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
