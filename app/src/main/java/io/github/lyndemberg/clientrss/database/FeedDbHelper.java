package io.github.lyndemberg.clientrss.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.lyndemberg.clientrss.valueobject.Feed;
import io.github.lyndemberg.clientrss.valueobject.Notice;

public class FeedDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Feed.db";

    private static final String SQL_CREATE_FEED =
            "CREATE TABLE " + FeedSchema.TABLE_NAME + " ("+
                    FeedSchema.COLUMN_NAME_LAST_UPDATED + " DATE PRIMARY KEY)";

    private static final String SQL_DROP_FEED =
            "DROP TABLE IF EXISTS " + FeedSchema.TABLE_NAME;

    private static final String SQL_CREATE_NOTICE =
            "CREATE TABLE " + NoticeSchema.TABLE_NAME + " ("+
                    NoticeSchema._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    NoticeSchema.COLUMN_NAME_LINK + " TEXT," +
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

    public void insertFeed(Feed feed){
        SQLiteDatabase writerDb = getWritableDatabase();
        ContentValues values = new ContentValues();
        writerDb.beginTransaction();
        try {
            values.put(FeedSchema.COLUMN_NAME_LAST_UPDATED,Feed.FORMATTER.format(feed.getLastUpdated()));
            writerDb.insert(FeedSchema.TABLE_NAME,null, values);
            values.clear();
            for(Notice n : feed.getNotices()){
                values.put(NoticeSchema.COLUMN_NAME_FEED_ID, Feed.FORMATTER.format(feed.getLastUpdated()));
                values.put(NoticeSchema.COLUMN_NAME_AUTHOR,n.getAuthor());
                values.put(NoticeSchema.COLUMN_NAME_TITLE,n.getTitle());
                values.put(NoticeSchema.COLUMN_NAME_LINK,n.getLink().toString());
                values.put(NoticeSchema.COLUMN_NAME_PUBLISHED,Notice.FORMATTER.format(n.getPublished()));
                writerDb.insert(NoticeSchema.TABLE_NAME,null,values);
                values.clear();
            }
            writerDb.setTransactionSuccessful();
        }finally {
            writerDb.endTransaction();
        }
    }

    public void clearFeed(){
        SQLiteDatabase writerDb = getWritableDatabase();
        writerDb.delete(FeedSchema.TABLE_NAME,null,null);
    }

    public Date getLastUpdatedFeed(){
        SQLiteDatabase readerDb = getReadableDatabase();
        Cursor cursor = readerDb.rawQuery("SELECT " + FeedSchema.COLUMN_NAME_LAST_UPDATED + " FROM " + FeedSchema.TABLE_NAME, null);
        Date lastUpdatedDatabase = null;
        if(cursor.moveToFirst()){
            try {
                lastUpdatedDatabase = Feed.FORMATTER.parse(cursor.getString(cursor.getColumnIndex(FeedSchema.COLUMN_NAME_LAST_UPDATED)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return lastUpdatedDatabase;
    }

    public ArrayList<Notice> getAllNotices(){
        ArrayList<Notice> notices = new ArrayList<>();
        SQLiteDatabase readerDb = getReadableDatabase();
        Cursor cursor = readerDb.rawQuery("SELECT " +
                NoticeSchema.COLUMN_NAME_LINK + "," +
                NoticeSchema.COLUMN_NAME_TITLE + "," +
                NoticeSchema.COLUMN_NAME_AUTHOR + "," +
                NoticeSchema.COLUMN_NAME_FEED_ID + "," +
                NoticeSchema.COLUMN_NAME_PUBLISHED +
                " FROM " + NoticeSchema.TABLE_NAME,
                null
        );

        while(cursor.moveToNext()){
            Notice notice = new Notice();
            try {
                notice.setLink(new URL(cursor.getString(cursor.getColumnIndex(NoticeSchema.COLUMN_NAME_LINK))));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            notice.setAuthor(cursor.getString(cursor.getColumnIndex(NoticeSchema.COLUMN_NAME_AUTHOR)));
            notice.setTitle(cursor.getString(cursor.getColumnIndex(NoticeSchema.COLUMN_NAME_TITLE)));
            notice.setPublished(cursor.getString(cursor.getColumnIndex(NoticeSchema.COLUMN_NAME_PUBLISHED)));
            notices.add(notice);
        }

        return notices;
    }
}
