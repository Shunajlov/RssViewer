package com.ihavenodomain.rssviewer.model.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ihavenodomain.rssviewer.model.rss.RSS;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface MyDao {
    @Insert(onConflict = REPLACE)
    void insert(RSS rss);

    @Insert
    void insertAll(List<RSS> rssList);

    @Query("SELECT * FROM rss")
    LiveData<List<RSS>> getAllRss();

    @Query("SELECT * FROM rss")
    List<RSS> getAllRssSync();

    @Query("DELETE FROM rss")
    void clearAllData();
}
