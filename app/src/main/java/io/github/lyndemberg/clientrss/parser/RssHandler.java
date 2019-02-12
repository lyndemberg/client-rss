package io.github.lyndemberg.clientrss.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.net.MalformedURLException;
import java.net.URL;

import io.github.lyndemberg.clientrss.valueobject.Feed;
import io.github.lyndemberg.clientrss.valueobject.Notice;

import static io.github.lyndemberg.clientrss.parser.BaseFeedParser.ENTRY;
import static io.github.lyndemberg.clientrss.parser.BaseFeedParser.ID;
import static io.github.lyndemberg.clientrss.parser.BaseFeedParser.NAME;
import static io.github.lyndemberg.clientrss.parser.BaseFeedParser.PUBLISHED;
import static io.github.lyndemberg.clientrss.parser.BaseFeedParser.TITLE;
import static io.github.lyndemberg.clientrss.parser.BaseFeedParser.UPDATED;

public class RssHandler extends DefaultHandler {
    private Feed feed;
    private Notice currentNotice;
    private StringBuilder builder;

    public Feed getFeed() {
        return feed;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        builder.append(ch,start,length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (this.currentNotice != null){
            if(localName.equalsIgnoreCase(NAME)){
                currentNotice.setAuthor(builder.toString().trim());
            }if (localName.equalsIgnoreCase(TITLE)){
                currentNotice.setTitle(builder.toString().trim());
            } else if (localName.equalsIgnoreCase(ID.trim())){
                try {
                    currentNotice.setLink(new URL(builder.toString().trim()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else if(localName.equalsIgnoreCase(PUBLISHED)){
                currentNotice.setPublished(builder.toString().trim());
            }else if (localName.equalsIgnoreCase(ENTRY)){
                feed.addNotice(currentNotice);
            }

        }
        else if(localName.equalsIgnoreCase(UPDATED)){
            feed.setLastUpdated(builder.toString().trim());
        }
        builder.setLength(0);
    }


    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        this.feed = new Feed();
        builder = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if(localName.equalsIgnoreCase(ENTRY)){
            this.currentNotice = new Notice();
        }
    }
}
