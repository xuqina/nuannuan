package com.example.qina.nuannuan.view.views;

import java.util.ArrayList;

/**
 * Created by weijia on 18-5-12.
 */
public interface WeatherView {
    public void startQueryWeather(String[] params);
    public void QueryWeatherSuccess(ArrayList<String> s);
}
