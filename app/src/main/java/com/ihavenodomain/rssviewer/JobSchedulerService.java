package com.ihavenodomain.rssviewer;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.ihavenodomain.rssviewer.model.api.ApiQueries;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class JobSchedulerService extends JobService {
    public static final int JOB_LOAD_RSS = 19; // just because it is my favorite number.

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Set<String> urls = App.getSubscriptionUrls();

        ApiQueries.requestRSSList(this, (App) getApplication(), urls);

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
