package com.example.RSSWidgetY.saxrssreader;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;


public class RssFeed implements Parcelable {

    public static final Parcelable.Creator<RssFeed> CREATOR = new Parcelable.Creator<RssFeed>() {
        public RssFeed createFromParcel(Parcel data) {
            return new RssFeed(data);
        }

        public RssFeed[] newArray(int size) {
            return new RssFeed[size];
        }
    };
    private String title;
    private String link;
    private String description;
    private String language;
    private final ArrayList<RssItem> rssItems;

    public RssFeed() {
        rssItems = new ArrayList<RssItem>();
    }

    private RssFeed(Parcel source) {

        Bundle data = source.readBundle();
        title = data.getString("title");
        link = data.getString("link");
        description = data.getString("description");
        language = data.getString("language");
        rssItems = data.getParcelableArrayList("rssItems");

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        Bundle data = new Bundle();
        data.putString("title", title);
        data.putString("link", link);
        data.putString("description", description);
        data.putString("language", language);
        data.putParcelableArrayList("rssItems", rssItems);
        dest.writeBundle(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    void addRssItem(RssItem rssItem) {
        rssItems.add(rssItem);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public ArrayList<RssItem> getRssItems() {
        return rssItems;
    }

}
