package com.example.qina.nuannuan.view.adapter;

/**
 * Created by qina on 18-1-30.
 */
import java.util.ArrayList;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qina.nuannuan.R;
import com.example.qina.nuannuan.model.entity.Weather;

public class WeatherListviewAdapter extends RecyclerView.Adapter<WeatherListviewAdapter.ViewHolder>{
    private ArrayList<Weather> weather_list;
    private LayoutInflater mInflater;

    public WeatherListviewAdapter(Context context, ArrayList<Weather> items) {
        super();
        weather_list = items;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.weather_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        holder.Date = (TextView)view.findViewById(R.id.date);
        holder.Weather_description =(TextView)view.findViewById(R.id.weather_description);
        holder.Temperature = (TextView)view.findViewById(R.id.temperature);
        holder.Detail = (TextView)view.findViewById(R.id.detail);
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.Date.setText(weather_list.get(i).getDate());
        viewHolder.Weather_description.setText(weather_list.get(i).getWeather_description());
        viewHolder.Temperature.setText(weather_list.get(i).getTemperature());
        viewHolder.Detail.setText(weather_list.get(i).getDetail());
    }
    @Override
    public int getItemCount() {
        return weather_list.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
        TextView Date;
        TextView Weather_description;
        TextView Temperature;
        TextView Detail;
    }
}
