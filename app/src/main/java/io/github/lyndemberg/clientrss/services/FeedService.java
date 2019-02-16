package io.github.lyndemberg.clientrss.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.Serializable;
import java.util.Date;

import io.github.lyndemberg.clientrss.R;
import io.github.lyndemberg.clientrss.activities.MainActivity;
import io.github.lyndemberg.clientrss.parser.AndroidSaxFeedParser;
import io.github.lyndemberg.clientrss.parser.BaseFeedParser;
import io.github.lyndemberg.clientrss.valueobject.Feed;

import static io.github.lyndemberg.clientrss.receivers.MainReceiver.ACTION_FEED;

public class FeedService extends IntentService {

    private static final String TAG_FEED_SERVICE = "services.FeedService";

    private Date lastUpdated;
    private BaseFeedParser feedParser;

    private String CHANNEL_ID_NOTIFICATION;

    private int NOTIFICATION = 0;

    public FeedService() {
        super("Feed-Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.feedParser = new AndroidSaxFeedParser("https://www.diariodosertao.com.br/feed/atom");
        this.CHANNEL_ID_NOTIFICATION = getString(R.string.channel_id);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG_FEED_SERVICE,"Checking feed");
        Log.d(TAG_FEED_SERVICE,"lastUpdated: "+lastUpdated);
        //
        Feed parse = feedParser.parse();
        Date lastUpdated = parse.getLastUpdated();

        if(lastUpdated != this.lastUpdated){

            this.lastUpdated = lastUpdated;

            //send intent to activity
            Intent intentExecuted = new Intent(ACTION_FEED);
            intentExecuted.putExtra("newsList", (Serializable) parse.getNotices());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intentExecuted);

            //send notification here
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_NOTIFICATION)
                    .setSmallIcon(R.drawable.ic_fiber_new_black_24dp)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Existem novas notícias!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(NOTIFICATION++, notification);

        }
    }




}
