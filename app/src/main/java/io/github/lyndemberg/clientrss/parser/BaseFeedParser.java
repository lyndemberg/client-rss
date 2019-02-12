package io.github.lyndemberg.clientrss.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class BaseFeedParser implements FeedParser{
    static final String NAME = "name";
    static final String TITLE = "title";
    static final String ID = "id";
    static final String ENTRY = "entry";
    static final String PUBLISHED = "published";
    static final String FEED = "feed";
    static final String UPDATED = "updated";

    final URL feedUrl;

    protected BaseFeedParser(String feedUrl) {
        try {
            this.feedUrl = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    protected InputStream getInputStream(){
        try {
            return feedUrl.openConnection().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
