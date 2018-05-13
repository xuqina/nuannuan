package com.example.qina.nuannuan.model.impl;

import android.os.AsyncTask;
import android.util.Log;

import com.example.qina.nuannuan.model.models.WeatherModel;
import com.example.qina.nuannuan.model.listener.WeatherQueryListener;
import com.example.qina.nuannuan.utils.NetworkUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by weijia on 18-4-21.
 */
public class WeatherModelImpl implements WeatherModel{
    private static final String url = "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather";
    private WeatherQueryListener listener;

    public WeatherModelImpl(WeatherQueryListener listener) {
        this.listener = listener;
    }

    public void startQueryWeather(String[] params) {
        (new Weather_Search()).execute(params);
    }

    private class Weather_Search extends AsyncTask<String, Integer, ArrayList<String>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            ArrayList<String> xmlData = null;
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) ((new URL(url.toString()).openConnection()));
                connection.setRequestMethod("POST");
                connection.setReadTimeout(8000);
                connection.setConnectTimeout(8000);

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                String request = params[0];
                request = URLEncoder.encode(request, "utf-8");

                out.writeBytes("theCityCode=" + request + "&theUserID=");

                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader((new InputStreamReader(in)));
                StringBuilder response = new StringBuilder();

                String line;

                while((line = reader.readLine()) != null) {
                    response.append(line).append("\n");
                }
                Log.i("qinWeather_Searcha", "rsp:" + response.toString());
                xmlData = NetworkUtil.PauseWithXMLPull(response.toString());

            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }return xmlData;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);
            listener.onWeatherQuerySuccess(s);
        }
    }


}
