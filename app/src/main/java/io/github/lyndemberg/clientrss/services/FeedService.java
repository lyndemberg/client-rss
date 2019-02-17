package io.github.lyndemberg.clientrss.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

import io.github.lyndemberg.clientrss.R;
import io.github.lyndemberg.clientrss.activities.MainActivity;
import io.github.lyndemberg.clientrss.database.FeedDbHelper;
import io.github.lyndemberg.clientrss.database.FeedSchema;
import io.github.lyndemberg.clientrss.database.NoticeSchema;
import io.github.lyndemberg.clientrss.parser.AndroidSaxFeedParser;
import io.github.lyndemberg.clientrss.parser.BaseFeedParser;
import io.github.lyndemberg.clientrss.valueobject.Feed;
import io.github.lyndemberg.clientrss.valueobject.Notice;

import static io.github.lyndemberg.clientrss.receivers.MainReceiver.ACTION_FEED;

public class FeedService extends IntentService {

    private static final String TAG_FEED_SERVICE = "clientrss.FeedService";

    private BaseFeedParser feedParser;

    private String CHANNEL_ID_NOTIFICATION;

    private FeedDbHelper feedDb;

    public FeedService() {
        super("Feed-Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.feedParser = new AndroidSaxFeedParser(getString(R.string.link_resource_rss));
        this.CHANNEL_ID_NOTIFICATION = getString(R.string.channel_id);
        this.feedDb = new FeedDbHelper(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG_FEED_SERVICE,"Checking feed");

        //feed retrieved
        Feed parse = feedParser.parse();

        Date lastUpdatedRss = parse.getLastUpdated();
        Date lastUpdatedDatabase = feedDb.getLastUpdatedFeed();

        Log.d(TAG_FEED_SERVICE,"lastUpdatedDatabase: "+lastUpdatedDatabase);
        Log.d(TAG_FEED_SERVICE,"lastUpdatedRss: "+lastUpdatedRss);
        if(lastUpdatedDatabase==null || !lastUpdatedDatabase.equals(lastUpdatedRss)){
            Log.d(TAG_FEED_SERVICE,"news notices in RSS");
            Log.d(TAG_FEED_SERVICE,"sending news to receiver!");
            //send intent to activity
            Intent intentExecuted = new Intent(ACTION_FEED);
            intentExecuted.putExtra("newsList", (Serializable) parse.getNotices());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intentExecuted);

            Log.d(TAG_FEED_SERVICE,"showing notification");
            //send notification here
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_NOTIFICATION)
                    .setSmallIcon(R.drawable.ic_fiber_new_black_24dp)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Existem novas notícias!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(lastUpdatedRss.hashCode(), notification);

            Log.d(TAG_FEED_SERVICE,"inserting new data in cache");
            //update local database
            feedDb.clearFeed();
            feedDb.insertFeed(parse);
        }
    }

    @Override
    public void onDestroy() {
        feedDb.close();
        super.onDestroy();
    }
}
