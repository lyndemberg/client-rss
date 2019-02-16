package io.github.lyndemberg.clientrss.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FeedDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Feed.db";

    private static final String SQL_CREATE_FEED =
            "CREATE TABLE " + FeedSchema.TABLE_NAME + " ("+
                    FeedSchema.COLUMN_NAME_LAST_UPDATED + " DATE PRIMARY KEY)";

    private static final String SQL_DROP_FEED =
            "DROP TABLE IF EXISTS " + FeedSchema.TABLE_NAME;

    private static final String SQL_CREATE_NOTICE =
            "CREATE TABLE " + NoticeSchema.TABLE_NAME + " ("+
                    NoticeSchema.COLUMN_NAME_LINK + " TEXT PRIMARY KEY," +
                    NoticeSchema.COLUMN_NAME_AUTHOR + " TEXT," +
                    NoticeSchema.COLUMN_NAME_TITLE + " TEXT," +
                    NoticeSchema.COLUMN_NAME_FEED_ID +" DATE,"+
                    NoticeSchema.COLUMN_NAME_PUBLISHED + " DATE," +
                    "FOREIGN KEY ("+ NoticeSchema.COLUMN_NAME_FEED_ID + ")" +
                        " REFERENCES " + FeedSchema.TABLE_NAME + "(" + FeedSchema.COLUMN_NAME_LAST_UPDATED + ")" +
                            " ON DELETE CASCADE ON UPDATE CASCADE)";

    private static final String SQL_DROP_NOTICE =
            "DROP TABLE IF EXISTS " + NoticeSchema.TABLE_NAME;

    public FeedDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_FEED);
        sqLiteDatabase.execSQL(SQL_CREATE_NOTICE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DROP_NOTICE);
        sqLiteDatabase.execSQL(SQL_DROP_FEED);

        onCreate(sqLiteDatabase);
    }
}
