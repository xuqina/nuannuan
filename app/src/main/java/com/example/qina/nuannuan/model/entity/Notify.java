package com.example.qina.nuannuan.model.entity;

/**
 * Created by weijia on 18-4-19.
 */
public class Notify {
    private String notifyid;
    private String isNotifyFri; //0:提醒自己,1:提醒好友
    private String friendName;
    private String friendPhone;
    private String notifyType; //0:日期提醒,1:天气提醒
    private String keyword;
    private String date; //格式: yyyy-mm-dd
    private String city;

    public Notify() {
    }

    public Notify(String notifyid, String isNotifyFri, String friendName, String friendPhone, String notifyType, String keyword, String date, String city) {
        this.date = date;
        this.friendName = friendName;
        this.isNotifyFri = isNotifyFri;
        this.friendPhone = friendPhone;
        this.keyword = keyword;
        this.notifyid = notifyid;
        this.notifyType = notifyType;
        this.city = city;
    }

    public String getNotifyid() {
        return notifyid;
    }

    public void setNotifyid(String notifyid) {
        this.notifyid = notifyid;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getIsNotifyFri() {
        return isNotifyFri;
    }

    public void setIsNotifyFri(String isNotifyFri) {
        this.isNotifyFri = isNotifyFri;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getFriendPhone() {
        return friendPhone;
    }

    public void setFriendPhone(String friendPhone) {
        this.friendPhone = friendPhone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
