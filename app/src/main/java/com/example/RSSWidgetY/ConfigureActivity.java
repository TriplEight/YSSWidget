package com.example.RSSWidgetY;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;


public class ConfigureActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "com.example.RSSWidgetY.widget";
    private static final String PREF_PREFIX_KEY = "rss_url_";
    private final String[] urls = {"http://feeds.reuters.com/reuters/technologyNews",
            "http://feeds.reuters.com/reuters/entertainment",
            "http://feeds.reuters.com/reuters/scienceNews",
            "http://feeds.reuters.com/reuters/environment",
            "https://news.yandex.ru/science.rss",
            "https://news.yandex.ru/internet.rss",
            "https://news.yandex.ru/games.rss",
            "http://feeds.bbci.co.uk/news/england/rss.xml?edition=uk"};
    private int mWidgetId;
    private AutoCompleteTextView mRssUrlText;

    private static void saveRssUrl(Context context, int widgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + widgetId, text);
        prefs.apply();
    }

    static String loadRssUrl(Context context, int widgetId) {

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(PREF_PREFIX_KEY + widgetId, "");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configure_activity);

        mRssUrlText = (AutoCompleteTextView) findViewById(R.id.rss_url_text);

        mRssUrlText.setSelection(mRssUrlText.getText().length());

        mRssUrlText.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, urls));

        setResult(RESULT_CANCELED);
        setContentView(R.layout.configure_activity);
        getSupportActionBar().setTitle(R.string.title);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (mWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        mRssUrlText = (AutoCompleteTextView) findViewById(R.id.rss_url_text);
        String url = loadRssUrl(this, mWidgetId);
        if (!TextUtils.isEmpty(url)) {
            mRssUrlText.setText(url);
        }

        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClick();
            }
        });

    }

    private void onSaveButtonClick() {
        String rssUrl = mRssUrlText.getText().toString();
        if (URLUtil.isValidUrl(rssUrl)) {
            saveRssUrl(this, mWidgetId, mRssUrlText.getText().toString());

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        } else {
            Toast.makeText(ConfigureActivity.this, getString(R.string.error_in_url),
                    Toast.LENGTH_SHORT).show();
        }
    }

}
