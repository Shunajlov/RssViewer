package com.ihavenodomain.rssviewer.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import com.ihavenodomain.rssviewer.App;
import com.ihavenodomain.rssviewer.model.rss.RSS;

import java.util.List;


public class RSSListViewModel extends AndroidViewModel {
    /**
     * MediatorLiveData can observe other LiveData objects and react on their emissions.
     */
    private final MediatorLiveData<List<RSS>> observableRss;

    public RSSListViewModel(@NonNull Application application) {
        super(application);

        observableRss = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableRss.setValue(null);

        LiveData<List<RSS>> rssList = ((App) application).getRepository().getRss();

        // observe the changes of the products from the database and forward them
        observableRss.addSource(rssList, observableRss::setValue);
    }

    /**
     * Expose the LiveData RSS query so the UI can observe it.
     */
    public MediatorLiveData<List<RSS>> getRss() {
        return observableRss;
    }
}
