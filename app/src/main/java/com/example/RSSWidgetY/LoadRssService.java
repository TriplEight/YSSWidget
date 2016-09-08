package com.example.RSSWidgetY;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.RSSWidgetY.saxrssreader.RssFeed;
import com.example.RSSWidgetY.saxrssreader.RssItem;
import com.example.RSSWidgetY.saxrssreader.RssReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class LoadRssService extends IntentService {

    public static final String LOAD_RSS_ACTION = "com.example.RSSWidgetY.LoadRssService.LOAD_RSS_ACTION";
    public static final String RSS_PREFS_NAME = "com.example.RSSWidgetY.LoadRssService.Rss.Pref";
    public static final String PREF_PREFIX_KEY = "rss_";

    public LoadRssService() {
        super("LoadRssService");
    }

    private static void saveRss(Context context, int widgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(RSS_PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + widgetId, text);
        prefs.apply();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("LoadRssService", "onHandleIntent");
        if (intent.getAction().equals(LOAD_RSS_ACTION)) {
            Log.d("LoadRssService", "LOAD_RSS_ACTION");
        }

        ComponentName widget = new ComponentName(this, RssWidgetProvider.class);
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] widgetIds = appWidgetManager.getAppWidgetIds(widget);

        for (final int id : widgetIds) {
            String url = ConfigureActivity.loadRssUrl(this, id);
            try {
                List<RssItem> items = downloadRss(url);
                for (final RssItem rssItem : items) {
                    Log.d("LoadRssService", rssItem.getTitle());
                }
                saveRss(id, items);
                appWidgetManager.notifyAppWidgetViewDataChanged(id, R.id.stack_view);
            } catch (IOException | SAXException e) {
                Log.e("LoadRssService", e.toString());
            }
        }
    }

    private void saveRss(int id, List<RssItem> items) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (final RssItem rssItem : items) {
                JSONObject object = new JSONObject();
                object.put("title", rssItem.getTitle());
                object.put("text", android.text.Html.fromHtml(rssItem.getDescription()).toString());
                jsonArray.put(object);
            }
            saveRss(this, id, jsonArray.toString());
        } catch (JSONException e) {
            Log.e("LoadRssService", e.toString());
        }
    }

    private List<RssItem> downloadRss(String urlStr) throws IOException, SAXException {
        Log.d("LoadRssService", "downloadRss " + urlStr);
        URL url = new URL(urlStr);
        RssFeed feed = RssReader.read(url);
        return feed.getRssItems();
    }
}
