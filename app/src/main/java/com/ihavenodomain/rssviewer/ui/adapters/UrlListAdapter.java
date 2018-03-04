package com.ihavenodomain.rssviewer.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ihavenodomain.rssviewer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UrlListAdapter extends RecyclerView.Adapter<UrlListAdapter.UrlViewHolder> {
    private ArrayList<String> urls = new ArrayList<>();

    @Override
    public UrlViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UrlViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(UrlViewHolder holder, int position) {
        TextView urlTextView = (TextView) holder.itemView;
        String url = urls.get(position);
        urlTextView.setText(url);
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public void addLink(String url) {
        urls.add(url);
        notifyDataSetChanged();
    }

    public void addAllLinks(Set<String> urls) {
        this.urls.clear();
        this.urls.addAll(urls);
        notifyDataSetChanged();
    }

    static class UrlViewHolder extends RecyclerView.ViewHolder {
        UrlViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_url, parent, false));
        }
    }
}
