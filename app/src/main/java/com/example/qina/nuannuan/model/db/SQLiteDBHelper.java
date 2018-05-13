package com.example.qina.nuannuan.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by weijia on 18-4-3.
 */
public class SQLiteDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 7;
    private static final String DB_NAME = "motic.db";

    private static SQLiteDBHelper helper;

    private SQLiteDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.i("qina", "SQLiteDBHelper");
    }


    public static SQLiteDBHelper getInstance(Context context) {
        if (helper == null) {
            helper = new SQLiteDBHelper(context, DB_NAME, null, DB_VERSION);
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("qina","onCreate");
        String sql1 = "create table if not exists User (userid varchar(10) primary key, username varchar(50));";
        String sql2 = "create table if not exists Notify (notifyid varchar(20) primary key, isNotifyFri char(1), " +
                "friendName varchar(20), friendPhone varchar(20), notifyType char(1), keyword varchar(30), date varchar(10)," +
                "city varchar(20));";
        String []statements = new String[] {sql1,sql2};
        for (String sql : statements) {
            Log.i("qina",sql);
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("qina","onUpgrade");
        String sql1 = "drop table if exists User;";
        String sql2 =  "drop table if exists Notify;";
        String sql3 = "create table if not exists User (userid varchar(10) primary key, username varchar(50));";
        String sql4 = "create table if not exists Notify (notifyid varchar(20) primary key, isNotifyFri char(1), " +
                "friendName varchar(20), friendPhone varchar(20), notifyType char(1), keyword varchar(30), date varchar(10)," +
                "city varchar(20));";
        String []statements = new String[] {sql1,sql2,sql3,sql4};
        for (String sql : statements) {
            Log.i("qina",sql);
            db.execSQL(sql);
        }
    }
}
