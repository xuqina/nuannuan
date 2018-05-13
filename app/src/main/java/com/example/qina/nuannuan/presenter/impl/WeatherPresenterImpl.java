package com.example.qina.nuannuan.presenter.impl;

import com.example.qina.nuannuan.model.models.WeatherModel;
import com.example.qina.nuannuan.model.impl.WeatherModelImpl;
import com.example.qina.nuannuan.model.listener.WeatherQueryListener;
import com.example.qina.nuannuan.presenter.presenters.WeatherPresenter;
import com.example.qina.nuannuan.view.views.WeatherView;

import java.util.ArrayList;

/**
 * Created by weijia on 18-5-12.
 */
public class WeatherPresenterImpl implements WeatherPresenter,WeatherQueryListener {
    WeatherModel weatherModel = null;
    WeatherView weatherView = null;

    public WeatherPresenterImpl(WeatherView weatherView) {
        this.weatherView = weatherView;
        weatherModel = new WeatherModelImpl(this);
    }
    @Override
    public void startQueryWeather(String[] params) {
        weatherModel.startQueryWeather(params);
    }

    @Override
    public void onWeatherQueryFail() {

    }

    @Override
    public void onWeatherQuerySuccess(ArrayList<String> s) {
        weatherView.QueryWeatherSuccess(s);
    }
}
