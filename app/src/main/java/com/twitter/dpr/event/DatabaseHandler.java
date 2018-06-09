package com.twitter.dpr.event;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by root on 7/2/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private int DATABASE_VERSION;
    private String DATABASE_NAME;
    private String TABLE_NAME = "dummy";



    public DatabaseHandler(Context context, String name, int version) {
        super(context, name, null, version);

        this.DATABASE_NAME = name;
        this.DATABASE_VERSION = version;


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( id INTEGER PRIMARY KEY, location TEXT , date TEXT , title TEXT )";
        sqLiteDatabase.execSQL(create);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);

    }

    void addEntry(Tweet tweet, String tableName){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("location",tweet.getLocation() );
        values.put("date", tweet.getDate());
        values.put("title",tweet.getName());

        sqLiteDatabase.insert(tableName, null, values);
        sqLiteDatabase.close();

    }

//    Tweet getTweet(String location,String tableName){
//
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//        Cursor cursor = sqLiteDatabase.query(tableName, new String[]{"id","location","date","title"}, "location?" , new String[]{location}, null,null,null,null);
//
//        if(cursor != null)
//            cursor.moveToFirst();
//
//        Tweet tweet = new Tweet(cursor.getString(1),cursor.getString(2),cursor.getString(3));
//
//        return tweet;
//
//
//    }


    public List<String> getAllTweets(String tableName, String location,List<String> tweetList){

//        List<Tweet> tweetList = new ArrayList<Tweet>();
        String selectAll = "SELECT * FROM "+ tableName + " WHERE location = '"+ location + "'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectAll, null);

        if(cursor.moveToFirst()){

            do{
                Tweet tweet = new Tweet(cursor.getString(1),cursor.getString(2),cursor.getString(3));
                tweetList.add(tweet.getName()+" ("+tweet.getDate()+")");
            }while(cursor.moveToNext());


        }

        return tweetList;
    }


    public void createTable(String tableName){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String create = "CREATE TABLE IF NOT EXISTS " + tableName + "( id INTEGER PRIMARY KEY, location TEXT , date TEXT , title TEXT , UNIQUE(location, date, title) ON CONFLICT REPLACE)";
        try{
        sqLiteDatabase.execSQL(create);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void dropTable(String tableName){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ tableName);
    }


}