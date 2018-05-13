package com.example.qina.nuannuan.model.entity;

/**
 * Created by weijia on 18-4-3.
 */
public class User {
    private String userid;
    private String username;

    public User(String username) {
        this.userid = String.valueOf(System.currentTimeMillis());
        this.username = username;
    }

    public User(String userid, String username) {
        this.userid = userid;
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
