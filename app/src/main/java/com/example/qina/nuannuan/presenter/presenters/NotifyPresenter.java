package com.example.qina.nuannuan.presenter.presenters;

import com.example.qina.nuannuan.model.entity.Notify;

import java.util.List;

/**
 * Created by weijia on 18-5-12.
 */
public interface NotifyPresenter {
    public List<Notify> findAllNotify();
    public void insertNotify(Notify notify);
    public void deleteNotify(String notifyid);
}
