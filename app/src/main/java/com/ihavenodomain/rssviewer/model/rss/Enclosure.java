
package com.ihavenodomain.rssviewer.model.rss;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Enclosure implements Serializable {

    @SerializedName("length")
    private Long length;
    @SerializedName("link")
    private String link;
    @SerializedName("type")
    private String type;

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isImage() {
        return type.contains("image");
    }

}
