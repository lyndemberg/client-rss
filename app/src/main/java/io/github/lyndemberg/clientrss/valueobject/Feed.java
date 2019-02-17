package io.github.lyndemberg.clientrss.valueobject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Feed implements Serializable {
    public static DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private List<Notice> notices;
    private Date lastUpdated;

    public Feed() {
        this.notices = new ArrayList<>();
    }

    public Feed(List<Notice> notices, Date lastUpdated) {
        this.notices = notices;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "lastUpdated=" + lastUpdated +
                ", notices=" + notices +
                '}';
    }

    public List<Notice> getNotices() {
        return notices;
    }

    public void addNotice(Notice n){
        this.notices.add(n);
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        try {
            this.lastUpdated = FORMATTER.parse(lastUpdated);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
