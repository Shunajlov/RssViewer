package com.ihavenodomain.rssviewer.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ihavenodomain.rssviewer.App;
import com.ihavenodomain.rssviewer.R;
import com.ihavenodomain.rssviewer.Utils;
import com.ihavenodomain.rssviewer.ui.adapters.UrlListAdapter;

public class SettingsActivity extends MasterActivity implements View.OnClickListener {
    private RecyclerView rvSubscriptions;
    private UrlListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvSubscriptions = findViewById(R.id.rvSubscriptions);
        rvSubscriptions.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UrlListAdapter();
        rvSubscriptions.setAdapter(adapter);

        adapter.addAllLinks(App.getSubscriptionUrls());

        findViewById(R.id.btnAdd).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                openNewRssDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openNewRssDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_add_title);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(R.string.dialog_create, (dialog, which) -> {
            String text = input.getText().toString();
            if (text.matches(Utils.WEB_URL.pattern())) {
                dialog.dismiss();
                App.addSubscriptionUrl(text);
                adapter.addLink(text);
                rvSubscriptions.scrollToPosition(adapter.getItemCount() - 1);
            } else {
                toast(getString(R.string.not_an_url));
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
