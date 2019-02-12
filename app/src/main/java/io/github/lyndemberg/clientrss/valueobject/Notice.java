package io.github.lyndemberg.clientrss.valueobject;

import java.io.Serializable;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Notice implements Serializable {
    private static DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private String author;
    private String title;
    private URL link;
    private Date published;

    public Notice() {
    }

    @Override
    public String toString() {
        return "Notice{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", link=" + link +
                ", published=" + published +
                '}';
    }

    public Date getPublished() {
        return this.published;
    }

    public void setPublished(String published)  {
        try {
            this.published = FORMATTER.parse(published);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Notice(String author, String title, URL link, Date published) {
        this.author = author;
        this.title = title;
        this.link = link;
        this.published = published;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URL getLink() {
        return link;
    }

    public void setLink(URL link) {
        this.link = link;
    }
}
