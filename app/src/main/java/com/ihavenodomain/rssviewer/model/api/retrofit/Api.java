package com.ihavenodomain.rssviewer.model.api.retrofit;

import com.ihavenodomain.rssviewer.model.rss.RSS;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    /**
     * We use rss2json service to get any rss data in same form
     * @param url rss url
     * @param apiKey rss2json apiKey
     * @return {@link io.reactivex.Observable}
     */
    @GET("api.json")
    Observable<RSS> getRss(@Query("rss_url") String url,
                           @Query("api_key") String apiKey,
                           @Query("count") int count);
}
