package com.ihavenodomain.rssviewer.model.api.retrofit;

import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConnection {
    public ApiConnection() {
    }

    public static <T> T createXmlAdapterFor(final Class<T> api, String baseUrl) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client) // Use OkHttp3 client
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())) // RxJava2 adapter
                .addConverterFactory(GsonConverterFactory.create()) // Gson
                .build();
        return retrofit.create(api);
    }
}
