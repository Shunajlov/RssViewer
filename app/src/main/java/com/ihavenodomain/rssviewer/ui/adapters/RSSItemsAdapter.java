package com.ihavenodomain.rssviewer.ui.adapters;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ihavenodomain.rssviewer.App;
import com.ihavenodomain.rssviewer.R;
import com.ihavenodomain.rssviewer.model.rss.Item;
import com.ihavenodomain.rssviewer.ui.MasterActivity;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

public class RSSItemsAdapter extends RecyclerView.Adapter<RSSItemsAdapter.RSSItemViewHolder> {
    private ItemClick callback;
    private ArrayList<Item> rssList = new ArrayList<>();

    public RSSItemsAdapter(ItemClick callback) {
        this.callback = callback;
    }

    @Override
    public RSSItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RSSItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RSSItemViewHolder holder, int position) {
        Item item = rssList.get(position);
        if (item.getTitle() != null)
            holder.header.setText(item.getTitle());
        if (item.getDescription() != null) {
            holder.description.setText(Jsoup.parse(item.getDescription()).text());
        }

        if (item.getThumbnail() != null)
            MasterActivity.loadImageIntoView(App.CONTEXT, holder.ivPic, item.getThumbnail());

        holder.itemView.setOnClickListener(v -> callback.onItemClick(holder.ivPic, holder.header, item));
    }

    public void setItemsList(List<Item> items) {
        rssList.clear();
        rssList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return rssList.size();
    }

    public static class RSSItemViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView ivPic;
        TextView header, description;

        RSSItemViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rss, parent, false));
            ivPic = itemView.findViewById(R.id.ivPic);
            header = itemView.findViewById(R.id.tvHeader);
            description = itemView.findViewById(R.id.tvDescription);
        }
    }

    public interface ItemClick {
        void onItemClick(AppCompatImageView ivPic, TextView header, Item item);
    }
}
