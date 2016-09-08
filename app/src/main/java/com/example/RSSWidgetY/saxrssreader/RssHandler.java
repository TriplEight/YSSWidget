package com.example.RSSWidgetY.saxrssreader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

class RssHandler extends DefaultHandler {

    private RssFeed rssFeed;
    private RssItem rssItem;
    private StringBuilder stringBuilder;

    @Override
    public void startDocument() {
        rssFeed = new RssFeed();
        //new
        rssItem = new RssItem();
    }

    /**
     * Return the parsed RssFeed with it's RssItems
     */
    public RssFeed getResult() {
        return rssFeed;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        stringBuilder = new StringBuilder();

        if (qName.equalsIgnoreCase("item") && rssFeed != null) {
            rssItem = new RssItem();
            rssItem.setFeed(rssFeed);
            rssFeed.addRssItem(rssItem);
        }

    }

    @Override
    public void characters(char[] ch, int start, int length) {
        stringBuilder.append(new String(ch, start, length));
    }

    @Override
    public void endElement(String uri, String localName, String qName) {

        if (rssFeed != null && rssItem == null) {
            // Parse feed properties

            try {
                if (qName != null && qName.length() > 0) {
                    String methodName = "set" + qName.substring(0, 1).toUpperCase() + qName.substring(1);
                    Method method = rssFeed.getClass().getMethod(methodName, String.class);
                    method.invoke(rssFeed, stringBuilder.toString());
                }
            } catch (SecurityException | IllegalAccessException | NoSuchMethodException
                    | IllegalArgumentException | InvocationTargetException ignored) {
            }

        } else {
            if (rssItem != null) {
                // Parse item properties

                try {
                    if (qName.equals("content:encoded"))
                        qName = "content";
                    String methodName = "set" + qName.substring(0, 1).toUpperCase() + qName.substring(1);
                    Method method = rssItem.getClass().getMethod(methodName, String.class);
                    method.invoke(rssItem, stringBuilder.toString());
                } catch (SecurityException | IllegalAccessException | NoSuchMethodException
                        | IllegalArgumentException | InvocationTargetException ignored) {
                }
            }
        }

    }

}
