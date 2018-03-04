package com.ihavenodomain.rssviewer.model.api;

import android.content.Context;

import com.ihavenodomain.rssviewer.App;
import com.ihavenodomain.rssviewer.R;
import com.ihavenodomain.rssviewer.model.api.retrofit.Api;
import com.ihavenodomain.rssviewer.model.api.retrofit.ApiConnection;
import com.ihavenodomain.rssviewer.model.db.MyDb;
import com.ihavenodomain.rssviewer.model.rss.RSS;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;


public class ApiQueries {

    public static void requestRSSList(Context context, App app, Set<String> urls) {
        Api apiRef = ApiConnection.createXmlAdapterFor(Api.class, context.getString(R.string.rss_json_baseurl));
        final MyDb db = app.getDatabase();

        if (urls != null && urls.size() > 0) {
            List<Observable<?>> requests = new ArrayList<>();
            for (String url : urls) {
                requests.add(apiRef.getRss(url, context.getString(R.string.rss_json_apikey), 25));
            }

            // zip combines multiple requests and waits until all are done
            Observable.zip(requests, objects -> objects).subscribe(o -> {
                    if (o != null) {
                        db.beginTransaction();
                        // we clear all data from database to provide storing only last rss news
                        db.getRSSDao().clearAllData();

                        List<RSS> list = new ArrayList<>();
                        for (Object rssObj : o) {
                            if (rssObj instanceof RSS) {
                                list.add((RSS) rssObj);
                            }
                        }
                        db.getRSSDao().insertAll(list);

                        db.setTransactionSuccessful();
                        db.endTransaction();
                    }
                },
                Throwable::printStackTrace
            );
        }
    }
}
