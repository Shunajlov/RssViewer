package com.ihavenodomain.rssviewer.model.db;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ihavenodomain.rssviewer.model.rss.Feed;
import com.ihavenodomain.rssviewer.model.rss.Item;

import java.lang.reflect.Type;
import java.util.List;

public class FeedTypeConverter {
    @TypeConverter
    public static List<Item> stringToItems(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Item>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String itemsToString(List<Item> items) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Item>>() {}.getType();
        return gson.toJson(items, type);
    }

    @TypeConverter
    public static Feed stringToFeed(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Feed.class);
    }

    @TypeConverter
    public static String feedToString(Feed feed) {
        Gson gson = new Gson();
        return gson.toJson(feed, Feed.class);
    }
}
