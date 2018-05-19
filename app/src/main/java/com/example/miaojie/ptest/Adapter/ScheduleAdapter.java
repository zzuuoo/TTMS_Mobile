package com.example.miaojie.ptest.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.pojo.Schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ScheduleAdapter extends ArrayAdapter<Schedule> {
    private int resourceId;
    public ScheduleAdapter(@NonNull Context context, int resource, @NonNull List<Schedule> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @Override
    public View getView(int position, View converView, ViewGroup viewGroup)
    {
        Schedule schedule = getItem(position);
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
        Date date = schedule.getSched_time();
        View view = LayoutInflater.from(getContext()).inflate(resourceId,viewGroup,false);
        TextView play_name = (TextView)view.findViewById(R.id.schedule_play_name);
        TextView schedule_date = (TextView)view.findViewById(R.id.schedule_date);
        TextView schedule_time = (TextView)view.findViewById(R.id.schedule_time);
        TextView schedule_price = (TextView)view.findViewById(R.id.schedule_price);
        TextView schedule_studio = (TextView)view.findViewById(R.id.schedule_studio);
        play_name.setText("影片"+schedule.getPlay_id());
        schedule_date.setText(format0.format(date.getTime()));
        schedule_time.setText(format1.format(date.getTime())+"上映");
        schedule_price.setText(String.valueOf(schedule.getSched_ticket_price())+"元");
        schedule_studio.setText("影厅"+schedule.getStudio_id());
        return view;
    }

}
