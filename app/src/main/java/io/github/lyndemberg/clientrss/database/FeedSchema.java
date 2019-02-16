package io.github.lyndemberg.clientrss.database;

import android.provider.BaseColumns;

public class FeedSchema implements BaseColumns{

    public static final String TABLE_NAME = "feed";
    public static final String COLUMN_NAME_LAST_UPDATED = "last_updated";

    private FeedSchema(){
    }
}
