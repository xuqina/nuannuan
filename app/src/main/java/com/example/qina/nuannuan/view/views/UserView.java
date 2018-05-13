package com.example.qina.nuannuan.view.views;

import android.content.Context;

import com.example.qina.nuannuan.model.entity.User;

/**
 * Created by weijia on 18-5-12.
 */
public interface UserView {
    public int getUserCount();
    public void insertUser(User user);
}
