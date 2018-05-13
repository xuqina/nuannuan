package com.example.qina.nuannuan.model.listener;

import java.util.ArrayList;

/**
 * Created by weijia on 18-4-21.
 */
public interface WeatherQueryListener {
    void onWeatherQuerySuccess(ArrayList<String> s);
    void onWeatherQueryFail();
}
