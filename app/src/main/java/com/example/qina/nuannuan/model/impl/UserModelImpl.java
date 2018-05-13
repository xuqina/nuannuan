package com.example.qina.nuannuan.model.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.qina.nuannuan.model.db.SQLiteDBHelper;
import com.example.qina.nuannuan.model.entity.User;
import com.example.qina.nuannuan.model.models.UserModel;

/**
 * Created by weijia on 18-4-3.
 */
public class UserModelImpl implements UserModel {
    private SQLiteDBHelper helper = null;
    private SQLiteDatabase db = null;

    public UserModelImpl(Context context) {
        helper = SQLiteDBHelper.getInstance(context);

    }

    @Override
    public int getUserCount() {
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from User", null);
        return cursor.getCount();
    }

    public void insertUser(User user) {
        db = helper.getReadableDatabase();
        db.execSQL("insert into User values(?,?)", new String[]{ user.getUserid(), user.getUsername()});
    }

    public String getCurrentUserName() {
        db = helper.getReadableDatabase();
        String userName = "";
        Cursor cursor = db.rawQuery("select username from User", null);
        while (cursor.moveToNext()) {
            Log.i("qina", "cursor.getCount():" +cursor.getCount());
            userName = cursor.getString(0);
            Log.i("qina","username:"+userName);
        }
        return userName;
    }
}
