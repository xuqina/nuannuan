package com.example.qina.nuannuan.presenter.impl;

import android.content.Context;

import com.example.qina.nuannuan.model.impl.UserModelImpl;
import com.example.qina.nuannuan.model.entity.User;
import com.example.qina.nuannuan.model.models.UserModel;
import com.example.qina.nuannuan.presenter.presenters.UserPresenter;
import com.example.qina.nuannuan.view.views.UserView;

/**
 * Created by weijia on 18-5-12.
 */
public class UserPresenterImpl implements UserPresenter {
    UserModel userModel = null;

    public UserPresenterImpl(UserView userView, Context context) {
        userModel = new UserModelImpl(context);
    }

    @Override
    public int getUserCount() {
        return userModel.getUserCount();
    }

    @Override
    public void insertUser(User user) {
        userModel.insertUser(user);
    }
}
