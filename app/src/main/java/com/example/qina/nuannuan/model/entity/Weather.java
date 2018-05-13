package com.example.qina.nuannuan.model.entity;

/**
 * Created by qina on 18-1-30.
 */
public class Weather {
    private String date;
    private String weather_description;
    private String temperature;
    private String detail;
    public String getDate() {
        return this.date;
    }
    public String getWeather_description() {
        return this.weather_description;
    }
    public String getTemperature() {
        return temperature;
    }
    public String getDetail() {
        return detail;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setWeather_description(String weather_description) {
        this.weather_description = weather_description;
    }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
}