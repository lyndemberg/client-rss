package io.github.lyndemberg.clientrss.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import io.github.lyndemberg.clientrss.valueobject.Notice;
import io.github.lyndemberg.clientrss.R;
import io.github.lyndemberg.clientrss.adapters.NoticesAdapter;
import io.github.lyndemberg.clientrss.receivers.MainReceiver;
import io.github.lyndemberg.clientrss.services.FeedService;

public class MainActivity extends AppCompatActivity {

    private MainReceiver receiver;
    private ListView listNews;
    private NoticesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.listNews = findViewById(R.id.listNews);

        this.receiver = new MainReceiver() {
            @Override
            public void handleView(Intent intent) {
                ArrayList<Notice> news  = (ArrayList<Notice>) intent.getSerializableExtra("newsList");
                adapter = new NoticesAdapter(MainActivity.this, news);
                listNews.setAdapter(adapter);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this,FeedService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,new IntentFilter(MainReceiver.ACTION_FEED));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
}
