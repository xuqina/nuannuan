package com.example.qina.nuannuan.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by weijia on 18-5-12.
 */
public class MessageUtil {
    /**
     * 直接调用短信接口发短信
     * @param phoneNumber
     * @param message
     */
    public static void sendSMS(Context context, String phoneNumber,String message){
        //获取短信管理器
        Log.i("qina","sendSMS");
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        //拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, null, null);
        }
        Toast.makeText(context, "短信已发送", Toast.LENGTH_LONG).show();
    }
}
