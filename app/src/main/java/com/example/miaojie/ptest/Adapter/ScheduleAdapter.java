package com.example.miaojie.ptest.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.pojo.Grob_var;
import com.example.miaojie.ptest.pojo.PlayWeb;
import com.example.miaojie.ptest.pojo.Schedule;
import com.example.miaojie.ptest.pojo.StudioWeb;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ScheduleAdapter extends ArrayAdapter<Schedule> {
    private Context context;
    RequestQueue requestQueue;
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
        this.context=context;
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
            holder.play_name = view.findViewById(R.id.schedule_play_name);
            holder.schedule_date = view.findViewById(R.id.schedule_date);
            holder.schedule_time = view.findViewById(R.id.schedule_time);
            holder.schedule_price = view.findViewById(R.id.schedule_price);
            holder.schedule_studio = view.findViewById(R.id.schedule_studio);
            converView.setTag(holder);
        }else{
            view=converView;
            holder=(ViewHolder) converView.getTag();

        }

//        MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
//        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getReadableDatabase();
//        Cursor cursor = sqLiteDatabase.query("play",null,"play_id = ? "
//            ,new String[]{String.valueOf(schedule.getPlay_id())},null,null,null,null);
//        String play_names = "";
//        if(cursor.moveToFirst()){
//            play_names = cursor.getString(cursor.getColumnIndex("play_name"));
//        }
//        cursor = sqLiteDatabase.query("studio",null,"studio_id = ? "
//                ,new String[]{String.valueOf(schedule.getStudio_id())},null,null,null,null);
//        String studio_names = "";
//        if(cursor.moveToFirst()){
//            studio_names = cursor.getString(cursor.getColumnIndex("studio_name"));
//        }
//        holder.play_name.setText("影片:"+play_names);
        holder.schedule_date.setText(format0.format(date.getTime()));
        holder.schedule_time.setText(format1.format(date.getTime())+"上映");
        holder.schedule_price.setText(String.valueOf(schedule.getSched_ticket_price())+"元");
//        holder.schedule_studio.setText("影厅:"+studio_names);

        get( holder,schedule);
        return view;
    }

    public void get( ViewHolder holder,Schedule schedule){
        //创建一个请求队列
        requestQueue = Volley.newRequestQueue(context);
        //创建一个请求
//        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
//        String url1 = "http://192.168.0.32:8080/mobilePlay/getPlay";

        StringRequest stringRequest =new StringRequest(Grob_var.host+"mobilePlay/getPlayById?play_id="+schedule.getPlay_id(), new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {
//                List<User> userList = gson.fromJson(string, new TypeToken<List<User>>() {}.getType());
//                List<User> userList = gson.fromJson(jsonArray, new TypeToken<List<User>>() {}.getType());
                try {
                    Gson gson = new Gson();
                    PlayWeb playWeb= gson.fromJson(s, PlayWeb.class);
                    holder.play_name.setText("影片:"+playWeb.getPlayName());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                volley_result.setText("加载错误"+volleyError);
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);


        //创建一个请求队列
        requestQueue = Volley.newRequestQueue(context);
        //创建一个请求
//        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
//        String url1 = "http://192.168.0.32:8080/mobilePlay/getPlay";

        StringRequest stringRequest1 =new StringRequest(Grob_var.host+"mobileStudio/getStudioById?studio_id="+schedule.getStudio_id(), new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {
//                List<User> userList = gson.fromJson(string, new TypeToken<List<User>>() {}.getType());
//                List<User> userList = gson.fromJson(jsonArray, new TypeToken<List<User>>() {}.getType());
                try {
                    Gson gson = new Gson();
                    StudioWeb studioWeb= gson.fromJson(s, StudioWeb.class);
                    holder.schedule_studio.setText("影厅:"+studioWeb.getStudioName());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                volley_result.setText("加载错误"+volleyError);
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest1);
    }

}
