package com.example.qina.nuannuan.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qina.nuannuan.R;
import com.example.qina.nuannuan.presenter.impl.WeatherPresenterImpl;
import com.example.qina.nuannuan.presenter.presenters.WeatherPresenter;
import com.example.qina.nuannuan.utils.NetworkUtil;
import com.example.qina.nuannuan.view.adapter.WeatherListviewAdapter;
import com.example.qina.nuannuan.model.entity.Weather;
import com.example.qina.nuannuan.model.impl.UserModelImpl;
import com.example.qina.nuannuan.view.views.WeatherView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qina on 18-1-30.
 */
public class WeatherActivity extends AppCompatActivity implements WeatherView {

    private Button search_button;
    private static final int UPDATE_CONTENT = 0;
    private EditText editText;
    private TextView city;
    private TextView timeText;
    private TextView HightemText;
    private TextView temText;
    private TextView wetText;
    private TextView airText;
    private TextView windText;
    private Activity activity;
    private String shareContent = "";
    private WeatherPresenter weatherPresenter = new WeatherPresenterImpl(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);
        activity = this;
        Log.i("qina", (new UserModelImpl(activity)).getCurrentUserName());
        editText = (EditText) findViewById(R.id.input);
        city = (TextView) findViewById(R.id.city_name);
        timeText = (TextView) findViewById(R.id.time);
        HightemText = (TextView) findViewById(R.id.high_temp);
        temText = (TextView) findViewById(R.id.temp);
        wetText = (TextView) findViewById(R.id.wet);
        airText = (TextView) findViewById(R.id.air);
        windText = (TextView) findViewById(R.id.wind);
        search_button = (Button) findViewById(R.id.search);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtil.isNetworkConnected(activity)) {
                    Toast.makeText(activity, "当前没有可用网络！", Toast.LENGTH_SHORT).show();
                } else {
                    String[] params = new String[]{editText.getText().toString()};
                    startQueryWeather(params);
                }
            }
        });
    }

    @Override
    public void startQueryWeather(String[] params) {
        weatherPresenter.startQueryWeather(params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sharing:
                if (shareContent.length()==0) {
                    Toast.makeText(this,"请查询天气之后再转发",Toast.LENGTH_LONG).show();
                } else {
                    Intent share = new Intent();
                    share.setClass(this, ShareActivity.class);
                    share.putExtra("content", shareContent);
                    startActivity(share);
                }
                break;
            case R.id.setting:
                Intent set = new Intent();
                set.setClass(this, SetActivity.class);
                startActivity(set);
                break;
        }
        return true;
    }

    @Override
    public void QueryWeatherSuccess(ArrayList<String> s) {
        if (s.size() == 1) {
            if (s.get(0).equals("查询结果为空")) {
                Toast.makeText(activity, "当前城市不存在，请重新输入", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.message);
        relativeLayout.setVisibility(View.VISIBLE);

        city.setText(s.get(1));
        temText.setText(s.get(8));
        String[] array1 = s.get(3).split(" ");
        timeText.setText(array1[1] + "更新");

        String[] array = s.get(4).split("：|；");
        if (array[1].equals("暂无实况")) {
            HightemText.setText("暂无预报");
            wetText.setText("暂无预报");
            airText.setText("暂无预报");
            windText.setText("暂无预报");
        } else {
            HightemText.setText(array[2]);
            wetText.setText("湿度:" + array[6]);
            windText.setText(array[4]);
            airText.setText(s.get(5).split("。")[1]);
            Log.i("qina", (new UserModelImpl(activity)).getCurrentUserName());
            shareContent = "您的好友"+(new UserModelImpl(activity)).getCurrentUserName()+"通过[暖暖]给您带来了温馨天气提醒："+city.getText().toString()+"，"+temText.getText().toString()
                    +"，"+wetText.getText().toString()+"，"+airText.getText().toString()+"，"+windText.getText().toString();
        }

        List<Map<String, Object>> data = new ArrayList<>();
        for (int i=0;i<5;i++) {
            Map<String, Object> temp = new HashMap<>();
            if (array[1].equals("暂无实况")) {
                String[] title = {"紫外线指数","感冒指数","穿衣指数","洗车指数","运动指数","空气污染指数"};
                String detail = "暂无预报";
                temp.put("Title", title[i]);
                temp.put("Detail",detail);
            }else {
                String[] strings = s.get(6).split("。|：");
//                String[] title = {strings[0],strings[2],strings[4],strings[6],strings[8],strings[10]};
//                String[] detail = {strings[1],strings[3],strings[5],strings[7],strings[9],strings[11]};
                String[] title = {strings[0],strings[2],strings[4],strings[6],strings[8]};
                String[] detail = {strings[1],strings[3],strings[5],strings[7],strings[9]};
                temp.put("Title",title[i]);
                temp.put("Detail",detail[i]);
            }
            data.add(temp);
        }
        ListView listView = (ListView)findViewById(R.id.list);
        SimpleAdapter simpleAdapter = new SimpleAdapter(activity,data,R.layout.item,new String[]{"Title","Detail"},new int[]{R.id.t,R.id.d});
        listView.setAdapter(simpleAdapter);

        ArrayList<Weather> weather_list = new ArrayList<>();
        int j = 7;
        for (int i=0;i<5;i++) {
            Weather weather = new Weather();
            String[] strings = s.get(j).split(" ");
            weather.setDate(strings[0]);
            weather.setWeather_description(strings[1]);
            weather.setTemperature(s.get(++j));
            weather.setDetail(s.get(++j));
            weather_list.add(weather);
            j = j + 3;
        }
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        WeatherListviewAdapter adapter = new WeatherListviewAdapter(activity, weather_list);
        recyclerView.setAdapter(adapter);
    }


}
