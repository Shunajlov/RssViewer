package com.ihavenodomain.rssviewer.model.rss;

import com.google.gson.annotations.SerializedName;

public class Feed {
    @SerializedName("author")
    private String author;

    @SerializedName("description")
    private String description;

    @SerializedName("image")
    private String image;

    @SerializedName("link")
    private String link;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
