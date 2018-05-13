package com.example.qina.nuannuan.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.qina.nuannuan.R;
import com.example.qina.nuannuan.model.entity.User;
import com.example.qina.nuannuan.presenter.impl.UserPresenterImpl;
import com.example.qina.nuannuan.presenter.presenters.UserPresenter;
import com.example.qina.nuannuan.view.service.UpdateWeatherService;
import com.example.qina.nuannuan.view.views.UserView;

public class MainActivity extends AppCompatActivity implements UserView {
    UserPresenter userPresenter = new UserPresenterImpl(this,this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent serviceIntent = new Intent(MainActivity.this,UpdateWeatherService.class);
        startService(serviceIntent);

        int userCount = getUserCount();
        Log.i("qina","userCount:"+userCount);
        if (userCount == 0) {
            Intent reg = new Intent();
            reg.setClass(MainActivity.this,RegisterActivity.class);
            startActivity(reg);
            finish();
        } else {
            Intent wea = new Intent();
            wea.setClass(MainActivity.this,WeatherActivity.class);
            startActivity(wea);
            finish();
        }
    }

    @Override
    public int getUserCount() {
        return userPresenter.getUserCount();
    }

    @Override
    public void insertUser(User user) {

    }
}
