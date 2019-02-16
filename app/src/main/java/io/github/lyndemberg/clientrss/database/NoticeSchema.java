package io.github.lyndemberg.clientrss.database;

import android.provider.BaseColumns;

public class NoticeSchema implements BaseColumns{

    public static final String TABLE_NAME = "notice";
    public static final String COLUMN_NAME_LINK = "link";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_AUTHOR = "author";
    public static final String COLUMN_NAME_PUBLISHED = "published";
    public static final String COLUMN_NAME_FEED_ID = "feed_id";

    private NoticeSchema(){

    }


}
