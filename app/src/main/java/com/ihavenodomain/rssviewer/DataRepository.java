package com.ihavenodomain.rssviewer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.ihavenodomain.rssviewer.model.db.MyDb;
import com.ihavenodomain.rssviewer.model.rss.RSS;

import java.util.List;

public class DataRepository {
    private static DataRepository ourInstance;

    private final MyDb database;
    private MediatorLiveData<List<RSS>> observableRss;

    private DataRepository(final MyDb database) {
        this.database = database;
        observableRss = new MediatorLiveData<>();

        observableRss.addSource(database.getRSSDao().getAllRss(), rsses -> {
            if (database.getDbCreated().getValue() != null) {
                observableRss.postValue(rsses);
            }
        });
    }

    public static DataRepository getInstance(final MyDb database) {
        if (ourInstance == null) {
            synchronized (DataRepository.class) {
                if (ourInstance == null) {
                    ourInstance = new DataRepository(database);
                }
            }
        }
        return ourInstance;
    }

    /**
     * Get the list of rss from the database and get notified when the data changes.
     */
    public LiveData<List<RSS>> getRss() {
        return observableRss;
    }
}
