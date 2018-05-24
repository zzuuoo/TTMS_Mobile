package com.example.miaojie.ptest.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.MyDatabaseHelper;
import com.example.miaojie.ptest.pojo.Schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ScheduleAdapter extends ArrayAdapter<Schedule> {

    static class ViewHolder{
        TextView play_name ;
        TextView schedule_date ;
        TextView schedule_time ;
        TextView schedule_price ;
        TextView schedule_studio ;
    }
    private int resourceId;
    public ScheduleAdapter(@NonNull Context context, int resource, @NonNull List<Schedule> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @Override
    public View getView(int position, View converView, ViewGroup viewGroup)
    {
        ViewHolder holder;
        Schedule schedule = getItem(position);
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
        Date date = schedule.getSched_time();
        View view;
        if(converView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,viewGroup,false);
            converView=view;
            holder = new ViewHolder();
            holder.play_name = (TextView)view.findViewById(R.id.schedule_play_name);
            holder.schedule_date = (TextView)view.findViewById(R.id.schedule_date);
            holder.schedule_time = (TextView)view.findViewById(R.id.schedule_time);
            holder.schedule_price = (TextView)view.findViewById(R.id.schedule_price);
            holder.schedule_studio = (TextView)view.findViewById(R.id.schedule_studio);
            converView.setTag(holder);
        }else{
            view=converView;
            holder=(ViewHolder) converView.getTag();

        }

//        TextView play_name = (TextView)view.findViewById(R.id.schedule_play_name);
//        TextView schedule_date = (TextView)view.findViewById(R.id.schedule_date);
//        TextView schedule_time = (TextView)view.findViewById(R.id.schedule_time);
//        TextView schedule_price = (TextView)view.findViewById(R.id.schedule_price);
//        TextView schedule_studio = (TextView)view.findViewById(R.id.schedule_studio);
        MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("play",null,"play_id = ? "
            ,new String[]{String.valueOf(schedule.getPlay_id())},null,null,null,null);
        String play_names = "";
        if(cursor.moveToFirst()){
            play_names = cursor.getString(cursor.getColumnIndex("play_name"));
        }
        cursor = sqLiteDatabase.query("studio",null,"studio_id = ? "
                ,new String[]{String.valueOf(schedule.getStudio_id())},null,null,null,null);
        String studio_names = "";
        if(cursor.moveToFirst()){
            studio_names = cursor.getString(cursor.getColumnIndex("studio_name"));
        }
        holder.play_name.setText("影片:"+play_names);
        holder.schedule_date.setText(format0.format(date.getTime()));
        holder.schedule_time.setText(format1.format(date.getTime())+"上映");
        holder.schedule_price.setText(String.valueOf(schedule.getSched_ticket_price())+"元");
        holder.schedule_studio.setText("影厅:"+studio_names);
        return view;
    }

}
