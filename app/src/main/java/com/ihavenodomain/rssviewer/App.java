package com.ihavenodomain.rssviewer;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.ihavenodomain.rssviewer.model.db.MyDb;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.ihavenodomain.rssviewer.JobSchedulerService.JOB_LOAD_RSS;


public class App extends Application {
    private static final String URLS = "urls";
    public static Context CONTEXT;
    public static Handler HANDLER;
    private static SharedPreferences SP_SETTINGS;
    private static SharedPreferences.Editor SP_EDITOR;

    private AppExecutors appExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        CONTEXT = getApplicationContext();
        HANDLER = new Handler();
        SP_SETTINGS = getSharedPreferences("settings", Context.MODE_PRIVATE);
        SP_EDITOR = SP_SETTINGS.edit();

        appExecutors = new AppExecutors();

        scheduleUpdating();
    }

    public MyDb getDatabase() {
        return MyDb.getInstance(this, appExecutors);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }

    private JobInfo prepareScheduler() {
        ComponentName serviceName = new ComponentName(this, JobSchedulerService.class);
        return new JobInfo.Builder(JOB_LOAD_RSS, serviceName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setPeriodic(TimeUnit.HOURS.toMillis(1))
                .build();
    }

    private void scheduleUpdating() {
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int result = scheduler.schedule(prepareScheduler());
        if (result == JobScheduler.RESULT_SUCCESS) {
            // stub
        }
    }

    public static Set<String> getSubscriptionUrls() {
        Set<String> urls = SP_SETTINGS.getStringSet(URLS, new HashSet<>());
        if (urls.size() == 0) {
            urls.add(App.CONTEXT.getString(R.string.rss_url_1));
            urls.add(App.CONTEXT.getString(R.string.rss_url_2));
            urls.add(App.CONTEXT.getString(R.string.rss_url_3));

            SP_EDITOR.putStringSet(URLS, urls).apply();
        }
        return urls;
    }

    public static void addSubscriptionUrl(String url) {
        HashSet<String> existing = new HashSet<>(getSubscriptionUrls());
        existing.add(url);
        SP_EDITOR.putStringSet(URLS, existing).apply();
    }
}
