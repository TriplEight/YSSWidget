package com.example.RSSWidgetY.saxrssreader;


import java.util.Date;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class RssItem implements Comparable<RssItem>, Parcelable {

    public static final Parcelable.Creator<RssItem> CREATOR = new Parcelable.Creator<RssItem>() {
        public RssItem createFromParcel(Parcel data) {
            return new RssItem(data);
        }

        public RssItem[] newArray(int size) {
            return new RssItem[size];
        }
    };
    private RssFeed feed;
    private String title;
    private String link;
    private Date pubDate;
    private String description;
    private String content;

    public RssItem() {

    }

    private RssItem(Parcel source) {

        Bundle data = source.readBundle();
        title = data.getString("title");
        link = data.getString("link");
        pubDate = (Date) data.getSerializable("pubDate");
        description = data.getString("description");
        content = data.getString("content");
        feed = data.getParcelable("feed");

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        Bundle data = new Bundle();
        data.putString("title", title);
        data.putString("link", link);
        data.putSerializable("pubDate", pubDate);
        data.putString("description", description);
        data.putString("content", content);
        data.putParcelable("feed", feed);
        dest.writeBundle(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setFeed(RssFeed feed) {
        this.feed = feed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private Date getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int compareTo(@NonNull RssItem another) {
        if (getPubDate() != null && another.getPubDate() != null) {
            return getPubDate().compareTo(another.getPubDate());
        } else {
            return 0;
        }
    }

}
