package com.ihavenodomain.rssviewer.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ihavenodomain.rssviewer.App;
import com.ihavenodomain.rssviewer.R;
import com.ihavenodomain.rssviewer.model.api.ApiQueries;
import com.ihavenodomain.rssviewer.model.rss.Item;
import com.ihavenodomain.rssviewer.model.rss.RSS;
import com.ihavenodomain.rssviewer.ui.adapters.RSSItemsAdapter;
import com.ihavenodomain.rssviewer.viewModel.RSSListViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MainActivity extends MasterActivity implements RSSItemsAdapter.ItemClick {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RSSItemsAdapter adapter;
    private HashMap<String, List<Item>> feedMap = new HashMap<>();
    private ArrayList<Item> items = new ArrayList<>();
    private String currentFeed;
    private static String rssAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rssAll = currentFeed = getString(R.string.rss_all);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(this::loadNews);

        RecyclerView recyclerView = findViewById(R.id.rvRss);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RSSItemsAdapter(this);
        recyclerView.setAdapter(adapter);

        final RSSListViewModel model = ViewModelProviders.of(this).get(RSSListViewModel.class);
        subscribeUi(model);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                boolean enabled = manager.findFirstCompletelyVisibleItemPosition() == 0 || manager.findFirstCompletelyVisibleItemPosition() == -1;
                swipeRefreshLayout.setEnabled(enabled);
            }
        });
    }

    private void subscribeUi(RSSListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getRss().observe(this, rsses -> {
            if (rsses != null) {
                for (RSS rss : rsses) {
                    items.addAll(rss.getItems());
                    feedMap.put(rss.getFeed().getTitle(), rss.getItems());
                }
                updateBasedOnFeedName(currentFeed);
                hideProgress();
            }
        });
    }

    private void updateBasedOnFeedName(String feedName) {
        if (adapter == null) return;

        if (!feedName.equals(rssAll))
            adapter.setItemsList(feedMap.get(feedName));
        else
            adapter.setItemsList(items);
    }

    @Override
    protected void onResume() {
        super.onResume();
        swipeRefreshLayout.post(this::loadNews);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_feeds:
                feedsDialog();
                return true;
            default:
                return true;
        }
    }

    private void loadNews() {
        showProgress();

        Set<String> urls = App.getSubscriptionUrls();

        ApiQueries.requestRSSList(this, (App) getApplication(), urls);
    }

    private void showProgress() {
        swipeRefreshLayout.setRefreshing(true);
    }

    private void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(AppCompatImageView ivPic, TextView header, Item item) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_ITEM, item);
        Pair<View, String> p1 = Pair.create(ivPic, "pic");
        Pair<View, String> p2 = Pair.create(header, "header");

        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, p1, p2);
        startActivity(intent, options.toBundle());
    }

    private void feedsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Needed to put "All feeds" item at the end of the list
        ArrayList<String> feedNames = new ArrayList<>(feedMap.keySet());
        feedNames.add(rssAll);

        final CharSequence[] items = feedNames.toArray(new CharSequence[feedMap.size()]);
        int pos = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(currentFeed)) {
                pos = i;
                break;
            }
        }

        builder.setSingleChoiceItems(items, pos, (dialog, which) -> {
            currentFeed = items[which].toString();
            updateBasedOnFeedName(currentFeed);
            dialog.dismiss();
        });

        builder.create().show();
    }
}
