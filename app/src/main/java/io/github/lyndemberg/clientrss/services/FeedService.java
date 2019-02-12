package io.github.lyndemberg.clientrss.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.io.Serializable;
import java.util.Date;

import io.github.lyndemberg.clientrss.parser.AndroidSaxFeedParser;
import io.github.lyndemberg.clientrss.parser.BaseFeedParser;
import io.github.lyndemberg.clientrss.valueobject.Feed;

import static io.github.lyndemberg.clientrss.receivers.MainReceiver.ACTION_FEED;

public class FeedService extends IntentService {

    private Date lastUpdated;
    private final long INTERVAL = 300000;
    private BaseFeedParser feedParser;

    public FeedService() {
        super("Feed-Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.feedParser = new AndroidSaxFeedParser("https://www.diariodosertao.com.br/feed/atom");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Feed parse = feedParser.parse();
        Date lastUpdated = parse.getLastUpdated();

        if(lastUpdated != this.lastUpdated){
            //send intent to activity
            Intent intentExecuted = new Intent(ACTION_FEED);
            intentExecuted.putExtra("newsList", (Serializable) parse.getNotices());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intentExecuted);

            //TODO send notification here
        }
        try {
            Thread.sleep(INTERVAL);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onHandleIntent(intent);
    }

}
