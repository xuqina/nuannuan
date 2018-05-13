package com.example.qina.nuannuan.model.models;

import android.content.Context;

import com.example.qina.nuannuan.model.entity.User;

/**
 * Created by weijia on 18-5-12.
 */
public interface UserModel {
    public int getUserCount();
    public void insertUser(User user);
}
