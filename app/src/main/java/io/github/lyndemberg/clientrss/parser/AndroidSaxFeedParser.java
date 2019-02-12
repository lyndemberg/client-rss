package io.github.lyndemberg.clientrss.parser;

import android.util.Xml;

import io.github.lyndemberg.clientrss.valueobject.Feed;

public class AndroidSaxFeedParser extends BaseFeedParser {

    public AndroidSaxFeedParser(String feedUrl) {
        super(feedUrl);
    }

    @Override
    public Feed parse() {
        RssHandler handler = new RssHandler();
        try {
            Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, handler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return handler.getFeed();
    }
}
