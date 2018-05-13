package com.example.qina.nuannuan.view.receiver;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.qina.nuannuan.R;
import com.example.qina.nuannuan.presenter.impl.NotifyPresenterImpl;
import com.example.qina.nuannuan.presenter.impl.WeatherPresenterImpl;
import com.example.qina.nuannuan.presenter.presenters.NotifyPresenter;
import com.example.qina.nuannuan.presenter.presenters.WeatherPresenter;
import com.example.qina.nuannuan.utils.MessageUtil;
import com.example.qina.nuannuan.utils.NetworkUtil;
import com.example.qina.nuannuan.view.activity.SetActivity;
import com.example.qina.nuannuan.view.activity.ShareActivity;
import com.example.qina.nuannuan.model.entity.Notify;
import com.example.qina.nuannuan.model.impl.UserModelImpl;
import com.example.qina.nuannuan.view.views.NotifyView;
import com.example.qina.nuannuan.view.views.WeatherView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by weijia on 18-4-21.
 */
public class AlarmBroadCastReceiver extends BroadcastReceiver implements NotifyView,WeatherView {
    private List<Notify> notifyList = null;
    Notify notify = null;
    private Context context = null;
    private int notificationId = 0;
    private String currentUsername;
    private int index = 0;
    private NotifyPresenter notifyPresenter = null;
    private WeatherPresenter weatherPresenter = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("qina","接收到广播");
        notifyPresenter = new NotifyPresenterImpl(this,context);
        weatherPresenter = new WeatherPresenterImpl(this);
        //查询数据库的提醒
        this.context = context;
        currentUsername = (new UserModelImpl(context)).getCurrentUserName();
        notifyList = findAllNotify();
        if (notifyList.size()==0) {
            return;
        }
        queryWeather();
    }

    @Override
    public void deleteNotify(String notifyid) {

    }

    @Override
    public List<Notify> findAllNotify() {
        return notifyPresenter.findAllNotify();
    }

    @Override
    public void insertNotify(Notify notify) {

    }

    @Override
    public void startQueryWeather(String[] params) {
        weatherPresenter.startQueryWeather(params);
    }

    public void queryWeather() {
        Log.i("qina","queryWeather");
        if (!NetworkUtil.isNetworkConnected(context)) {
            return;
        }
        callNotify(index++);
    }

    void callNotify(int i) {
        if (i < notifyList.size()) {
            notify = notifyList.get(i);
            if (notify.getNotifyType().equals("0")) {//日期提醒
                Log.i("qina",i+"：日期提醒");
                String dateString = notify.getDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = dateFormat.parse(dateString);
                    dateString = dateFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.i("qina","dateString:"+dateString);
                Calendar tomorrow = Calendar.getInstance();
                tomorrow.add(Calendar.DATE,1);
                String tomorrowString = dateFormat.format(tomorrow.getTime());
                Log.i("qina","tomorrowString:"+tomorrowString);
                if (dateString.equals(tomorrowString)) {
                    String city = notify.getCity();
                    String[] params = new String[]{city};
                    startQueryWeather(params);
                } else {
                    callNotify(index++);
                }
            } else {
                Log.i("qina",i+"天气提醒");
                String city = notify.getCity();
                String[] params = new String[]{city};
                startQueryWeather(params);
            }

        }
    }

    @Override
    public void QueryWeatherSuccess(ArrayList<String> s) {
        loadWeather(s);
        try {
            Thread.sleep(10000); //天气查询API免费用户不能频繁查询，间隔10s
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        callNotify(index++);
    }


    public void loadWeather(ArrayList<String> s) {
        Log.i("qina","s.size():"+s.size());
        if (s.size() < 15) {
            return;
        }
        int j = 7;
        String[] todayWeatherStrings = s.get(j).split(" ");
        if (todayWeatherStrings.length < 2) {
            return;
        }
        String todayDateString = todayWeatherStrings[0];
        String todayWeatherDescription = todayWeatherStrings[1];
        String todayTempture = s.get(++j);
        String todayWind = s.get(++j);
        String[] tempStr = todayTempture.split("/");
        if (tempStr.length<2) {
            return;
        }
        String highTempStr = tempStr[0].split("℃")[0];
        String lowTempStr = tempStr[1].split("℃")[0];
        int todayHighTemp = Integer.valueOf(highTempStr);
        int todayLowTemp = Integer.valueOf(lowTempStr);
        Log.i("qina","todayDateString:"+todayDateString+",todayWeatherDescription:"+todayWeatherDescription
        +",todayTempture:"+todayTempture+",todayWind:"+todayWind);
        Log.i("qina","todayHighTemp:"+todayHighTemp+",todayLowTemp:"+todayLowTemp);

        j = j + 3;

        String[] tomorrowWeatherStrings = s.get(j).split(" ");
        if (tomorrowWeatherStrings.length < 2) {
            return;
        }
        String tomorrowDateString = tomorrowWeatherStrings[0];
        String tomorrowWeatherDescription = tomorrowWeatherStrings[1];
        String tomorrowTempture = s.get(++j);
        String tomorrowWind = s.get(++j);
        tempStr = tomorrowTempture.split("/");
        if (tempStr.length<2) {
            return;
        }
        highTempStr = tempStr[0].split("℃")[0];
        lowTempStr = tempStr[1].split("℃")[0];
        int tomorrowHighTemp = Integer.valueOf(highTempStr);
        int tomorrowLowTemp = Integer.valueOf(lowTempStr);
        Log.i("qina","tomorrowDateString:"+tomorrowDateString+",tomorrowWeatherDescription:"+tomorrowWeatherDescription
        +",tomorrowTempture:"+tomorrowTempture+",tomorrowWind:"+tomorrowWind);
        Log.i("qina","tomorrowHighTemp:"+tomorrowHighTemp+",tomorrowLowTemp:"+tomorrowLowTemp);
        String notifyMsg = "";
        if (notify.getNotifyType().equals("0")) {
            Log.i("qina","日期提醒");
            notifyMsg = "明天天气:"+notify.getCity()+"，"+tomorrowDateString+"，"+tomorrowTempture+"，"+tomorrowWeatherDescription+"，"+tomorrowWind;

        } else {
            Log.i("qina","天气提醒");
            String keyword = notify.getKeyword();
            if (keyword.equals("高温")) {
                if (tomorrowHighTemp<=35) {
                    return;
                }
                notifyMsg = "明天是高温天气，出门记得带上一把遮阳伞，注意防晒，多喝水，小心中暑哦～";
            }
            if (keyword.equals("升温")) {
                if (tomorrowHighTemp-todayHighTemp<=5){
                    return;
                }
                notifyMsg = "明天温度上升，可以适当调整衣着，以防流汗哦～";
            }
            if (keyword.equals("降温")) {
                if (todayLowTemp-tomorrowLowTemp<=5){
                    return;
                }
                notifyMsg = "明天温度下降，请适当增添衣物，以防着凉哦～";
            }
            if (keyword.equals("下雨")) {
                if (tomorrowWeatherDescription.contains("雨")){
                    notifyMsg = "明天是下雨天，出门记得带上一把伞哦～";
                } else {
                    return;
                }
            }
            if (keyword.equals("下雪")) {
                if (tomorrowWeatherDescription.contains("雪")){
                    notifyMsg = "明天是下雨天，出门记得带上一把伞哦～";
                } else {
                    return;
                }
            }
        }
        if(notify.getIsNotifyFri().equals("1")) {
            notifyMsg = sendNotifyMsm(notifyMsg);
        }
        Notification.Builder builder = new Notification.Builder(context);
        Log.i("qina","notifyMsg:"+notifyMsg);
        builder.setContentText(notifyMsg);
        builder.setContentTitle("暖暖提醒");
        builder.setSmallIcon(R.drawable.alert);
        builder.setAutoCancel(true);

        Intent intent = new Intent(context, SetActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId++,builder.build());

    }

    private String sendNotifyMsm(String notifyMsg) {
        String sendResult = "";
        /**
         * 第 1 步: 检查是否有相应的权限
         */
        boolean isAllGranted = ShareActivity.checkPermissionAllGranted(context,
                new String[]{
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_PHONE_STATE}
        );
        // 如果这个权限已拥有, 则直接执行代码
        if (isAllGranted) {
            String msg = "您的好友"+currentUsername+"在[暖暖]上提醒您，"+notifyMsg;
            MessageUtil.sendSMS(context, notify.getFriendPhone(), msg);
            sendResult = "提醒触发，已发送短信给好友"+notify.getFriendName();
        } else {
            sendResult = "提醒触发，需要发短信给好友，请到系统设置中打开本应用的“发短信”权限。";
        }
        return sendResult;
    }


}
