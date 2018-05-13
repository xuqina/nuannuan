package com.example.qina.nuannuan.view.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.qina.nuannuan.view.receiver.AlarmBroadCastReceiver;

import java.util.Calendar;

/**
 * Created by weijia on 18-4-20.
 */
public class UpdateWeatherService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("qina","UpdateWeatherService onStartCommand");
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent receiverIntent = new Intent(this, AlarmBroadCastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,receiverIntent,0);
        Calendar now = Calendar.getInstance();
        Calendar target = (Calendar)now.clone();
        target.set(Calendar.HOUR_OF_DAY, 20);
        target.set(Calendar.MINUTE, 00);

        if (target.before(now)) {
            target.add(Calendar.DATE,1);
        }
        Log.i("qina","now:"+now.getTime());
        Log.i("qina", "target:" + target.getTime());
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, target.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        //测试用，可以立即触发定时任务，有短暂延迟
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,0,AlarmManager.INTERVAL_DAY,pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }
}
