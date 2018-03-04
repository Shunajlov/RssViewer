package com.ihavenodomain.rssviewer.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.widget.Toast;

import com.ihavenodomain.rssviewer.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public abstract class MasterActivity extends AppCompatActivity {
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void toast(int msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public static void loadImageIntoView(Context context, AppCompatImageView imageView, String url) {
        if (url == null || url.length() == 0) return;

        Picasso.with(context)
                .load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.mipmap.ic_launcher)
                .noFade()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {}

                    @Override
                    public void onError() {
                        Picasso.with(context).load(url).into(imageView);
                    }
                });
    }
}
