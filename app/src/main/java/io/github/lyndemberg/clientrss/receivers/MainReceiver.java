package io.github.lyndemberg.clientrss.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class MainReceiver extends BroadcastReceiver {
    //actions
    public static String ACTION_FEED = "main-action-feed";

    public abstract void handleView(Intent intent);

    @Override
    public void onReceive(Context context, Intent intent) {
        handleView(intent);
    }
}
