package com.ihavenodomain.rssviewer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ihavenodomain.rssviewer.App;
import com.ihavenodomain.rssviewer.R;
import com.ihavenodomain.rssviewer.model.rss.Item;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

public class DetailsActivity extends AppCompatActivity {
    public static String EXTRA_ITEM = "EXTRA_ITEM";

    private WebView webView;
    private ProgressBar progressBar;
    private AppCompatImageView ivPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        supportPostponeEnterTransition();

        Item item = (Item) getIntent().getExtras().getSerializable(EXTRA_ITEM);
        if (item == null) return;

        setContentView(R.layout.activity_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progress);

        ivPic = findViewById(R.id.ivPic);
        TextView header = findViewById(R.id.tvHeader);
        webView = findViewById(R.id.webView);
        TextView author = findViewById(R.id.tvAuthor);
        TextView dateTime = findViewById(R.id.tvDateTime);
        TextView link = findViewById(R.id.tvLink);

        String authorStr = getString(R.string.author) + item.getAuthor();
        author.setText(authorStr);

        String dateTimeStr = getString(R.string.pub_date) + item.getPubDate();
        dateTime.setText(dateTimeStr);

        link.setText(item.getLink());

        header.setText(item.getTitle());

        if (item.getThumbnail() != null && item.getThumbnail().length() > 0)
            Picasso.with(this)
                    .load(item.getThumbnail())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.mipmap.ic_launcher)
                    .noFade()
                    .into(ivPic, new Callback() {
                        @Override
                        public void onSuccess() {
                            supportStartPostponedEnterTransition();
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            supportStartPostponedEnterTransition();
                            hideProgressWithError.run();
                        }
                    });
        fetchDescription(item.getDescription());

        // Hide progressbar after 30 seconds if nothing was loaded
        App.HANDLER.postDelayed(hideProgressWithError, TimeUnit.SECONDS.toMillis(30));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // will finish activity with animation.
                // Commented because it looks a little bit ugly
//                supportFinishAfterTransition();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchDescription(String html) {
        String mime = "text/html";
        String encoding = "utf-8";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL(null, html, mime, encoding, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.HANDLER.removeCallbacks(hideProgressWithError);
    }

    Runnable hideProgressWithError = new Runnable() {
        @Override
        public void run() {
            progressBar.setVisibility(View.GONE);
            ivPic.setImageResource(R.mipmap.ic_launcher);
        }
    };
}
