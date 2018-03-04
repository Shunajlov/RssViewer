package com.ihavenodomain.rssviewer.model.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.ihavenodomain.rssviewer.AppExecutors;
import com.ihavenodomain.rssviewer.model.rss.RSS;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Database(entities = {RSS.class}, version = 1)
public abstract class MyDb extends RoomDatabase {
    private static final String DB_NAME = "my_db";
    private static MyDb ourInstance;

    private final MutableLiveData<Boolean> isDbCreated = new MutableLiveData<>();

    public abstract MyDao getRSSDao();

    public static MyDb getInstance(final Context context, final AppExecutors executors) {
        if (ourInstance == null) {
            synchronized (MyDb.class) {
                if (ourInstance == null) {
                    ourInstance = buildDatabase(context, executors);
                    ourInstance.updateDBCreated(context.getApplicationContext());
                }
            }
        }
        return ourInstance;
    }

    private static MyDb buildDatabase(Context context, final AppExecutors executors) {
        if (ourInstance == null)
            ourInstance = Room.databaseBuilder(
                    context.getApplicationContext(), MyDb.class, DB_NAME)
                    .allowMainThreadQueries()
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            executors.diskIO().execute(() -> {
                                if (getAssetsJsonString(context) != null
                                        && ourInstance.getRSSDao().getAllRssSync() == null
                                        || ourInstance.getRSSDao().getAllRssSync().isEmpty()) {
                                    insertData(ourInstance, new Gson().fromJson(getAssetsJsonString(context), RSS.class));
                                }
                                ourInstance.setDbCreated();
                            });
                        }
                    })
                    .build();
        return ourInstance;
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDbCreated()}
     */
    private void updateDBCreated(final Context context) {
        if (context.getDatabasePath(DB_NAME).exists()) {
            setDbCreated();
        }
    }

    private void setDbCreated() {
        isDbCreated.postValue(true);
    }

    public LiveData<Boolean> getDbCreated() {
        return isDbCreated;
    }

    private static void insertData(MyDb db, RSS rss) {
        db.runInTransaction(() -> {
            db.getRSSDao().clearAllData();
            db.getRSSDao().insert(rss);
        });
    }

    private static String getAssetsJsonString(Context context) {
        String str;

        try {
            StringBuilder buf = new StringBuilder();
            InputStream json = context.getAssets().open("initial_news.json");
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(json, "UTF-8"));


            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            str = buf.toString();

            in.close();
            return str;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
