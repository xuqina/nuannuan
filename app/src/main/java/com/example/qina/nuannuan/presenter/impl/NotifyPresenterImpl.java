package com.example.qina.nuannuan.presenter.impl;

import android.content.Context;

import com.example.qina.nuannuan.model.impl.NotifyModelImpl;
import com.example.qina.nuannuan.model.entity.Notify;
import com.example.qina.nuannuan.model.models.NotifyModel;
import com.example.qina.nuannuan.presenter.presenters.NotifyPresenter;
import com.example.qina.nuannuan.view.views.NotifyView;

import java.util.List;

/**
 * Created by weijia on 18-5-12.
 */
public class NotifyPresenterImpl implements NotifyPresenter{

    NotifyModel notifyModel = null;
    public NotifyPresenterImpl(NotifyView notifyView, Context context) {
        notifyModel = new NotifyModelImpl(context);
    }

    @Override
    public void deleteNotify(String notifyid) {
        notifyModel.deleteNotify(notifyid);
    }

    @Override
    public List<Notify> findAllNotify() {
        return notifyModel.findAllNotify();
    }

    @Override
    public void insertNotify(Notify notify) {
        notifyModel.insertNotify(notify);
    }
}
