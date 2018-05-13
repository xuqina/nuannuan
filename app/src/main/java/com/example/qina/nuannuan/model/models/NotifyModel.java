package com.example.qina.nuannuan.model.models;

import com.example.qina.nuannuan.model.entity.Notify;

import java.util.List;

/**
 * Created by weijia on 18-5-12.
 */
public interface NotifyModel {
    public List<Notify> findAllNotify();
    public void insertNotify(Notify notify);
    public void deleteNotify(String notifyid);
}
