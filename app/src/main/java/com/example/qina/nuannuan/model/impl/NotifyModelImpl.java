package com.example.qina.nuannuan.model.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.qina.nuannuan.model.db.SQLiteDBHelper;
import com.example.qina.nuannuan.model.entity.Notify;
import com.example.qina.nuannuan.model.models.NotifyModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weijia on 18-4-19.
 */
public class NotifyModelImpl implements NotifyModel{
    private SQLiteDBHelper helper = null;
    private SQLiteDatabase db = null;

    public NotifyModelImpl(Context context) {
        helper = SQLiteDBHelper.getInstance(context);
        db = helper.getReadableDatabase();
    }

    public List<Notify>  findAllNotify() {
        List<Notify> notifyList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from Notify", null);
        while (cursor.moveToNext()) {
            String notifyid = cursor.getString(0);
            String isNotifyFri = cursor.getString(1);
            String friendName = cursor.getString(2);
            String friendPhone = cursor.getString(3);
            String notifyType = cursor.getString(4);
            String keyword = cursor.getString(5);
            String date = cursor.getString(6);
            String city = cursor.getString(7);
            Notify notify = new Notify(notifyid,isNotifyFri,friendName,friendPhone,notifyType,keyword,date,city);
            notifyList.add(notify);
        }
        return notifyList;
    }

    public void insertNotify(Notify notify) {
        db.execSQL("insert into Notify values(?,?,?,?,?,?,?,?)", new String[]{notify.getNotifyid(),notify.getIsNotifyFri(),
                notify.getFriendName(),notify.getFriendPhone(),notify.getNotifyType(),notify.getKeyword(),notify.getDate()
                ,notify.getCity()});
    }

    public void deleteNotify(String notifyid) {
        db.execSQL("delete from Notify where notifyid = ?", new String[]{notifyid});
    }
}
