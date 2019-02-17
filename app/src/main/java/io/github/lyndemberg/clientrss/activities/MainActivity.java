package io.github.lyndemberg.clientrss.activities;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import io.github.lyndemberg.clientrss.database.FeedDbHelper;
import io.github.lyndemberg.clientrss.database.NoticeSchema;
import io.github.lyndemberg.clientrss.valueobject.Notice;
import io.github.lyndemberg.clientrss.R;
import io.github.lyndemberg.clientrss.adapters.NoticesAdapter;
import io.github.lyndemberg.clientrss.receivers.MainReceiver;
import io.github.lyndemberg.clientrss.services.FeedService;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_MAIN_ACTIVITY = "clientrss.MainActivity";
    private MainReceiver receiver;
    private ListView listNews;
    private NoticesAdapter adapter;
    private ArrayList<Notice> noticesList;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private static final int INTERVAL_ALARM = 30*1000;

    private FeedDbHelper feedDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listNews = findViewById(R.id.listNews);

        receiver = new MainReceiver() {
            @Override
            public void handleView(Intent intent) {
                noticesList  = (ArrayList<Notice>) intent.getSerializableExtra("newsList");
                adapter.clear();
                adapter.addAll(noticesList);
                adapter.notifyDataSetChanged();
            }
        };

        listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Notice selected = noticesList.get(i);
                Log.d(TAG_MAIN_ACTIVITY, "selected: " +  selected.getLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(selected.getLink().toString()));
                startActivity(intent);
            }
        });

        //create channel if necessary for android API 26+
        createNotificationChannel();
        //scheduling FeedService
        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, FeedService.class);
        pendingIntent = PendingIntent.getService(this,0,intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),INTERVAL_ALARM, pendingIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //load notices cache in local database
        feedDb = new FeedDbHelper(this);
        noticesList = feedDb.getAllNotices();
        adapter = new NoticesAdapter(this, noticesList);
        listNews.setAdapter(adapter);

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,new IntentFilter(MainReceiver.ACTION_FEED));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        feedDb.close();
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = getString(R.string.channel_id);
            CharSequence CHANNEL_NAME = getString(R.string.channel_name);
            int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPORTANCE);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
