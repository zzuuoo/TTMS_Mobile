package com.example.miaojie.ptest.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.LoadingDialog;
import com.example.miaojie.ptest.pojo.Grob_var;
import com.example.miaojie.ptest.pojo.Play;
import com.example.miaojie.ptest.pojo.PlayWeb;
import com.example.miaojie.ptest.pojo.Schedule;
import com.example.miaojie.ptest.pojo.Studio;
import com.example.miaojie.ptest.pojo.StudioWeb;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class EditScheduleActivity extends AppCompatActivity implements View.OnClickListener{

    private Play p = null;
    private Studio s = null;
    private Schedule schedule =null;
    private Button sure;
    private Button cancle;
    private List<Play> plays = new ArrayList<>();
    private List<Studio> studios = new ArrayList<>();
    private Spinner spinnerStudio;
    private Spinner spinnerPlay;
    private ArrayAdapter<Play> arrayAdapterPlay;
    private ArrayAdapter<Studio> arrayAdapterStudio;
    private EditText ticket_price;
    private TextView sched_date;
    private TextView sched_time;
    private Handler handler;
    private LoadingDialog dialog;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);
        Intent intent = this.getIntent();
        schedule = (Schedule) intent.getSerializableExtra("schedule");
        getPlay();
        getStudio();
        dialog = new LoadingDialog(EditScheduleActivity.this,"更新中...");
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        dialog.close();
                        Toast.makeText(EditScheduleActivity.this,"生成票成功",Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case 2:
                        dialog.close();
                        finish();
                        break;
                }
            }
        };
        init();
    }

    public void getPlay(){
        //创建一个请求队列
        plays.clear();
        requestQueue = Volley.newRequestQueue(this);
        //创建一个请求
//        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
//        String url1 = "http://192.168.0.32:8080/mobilePlay/getPlay";

        StringRequest stringRequest =new StringRequest(Grob_var.host+"mobilePlay/"+"getPlay", new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {
//                List<User> userList = gson.fromJson(string, new TypeToken<List<User>>() {}.getType());
//                List<User> userList = gson.fromJson(jsonArray, new TypeToken<List<User>>() {}.getType());
                try {
                    Gson gson = new Gson();
                    List<PlayWeb> playWebs = new ArrayList<>();
                    playWebs = gson.fromJson(s, new TypeToken<List<PlayWeb>>() {}.getType());
                    Log.e("playssssss",playWebs.toString());
                    for(int i=0;i<playWebs.size();i++)
                    {
                        Play p = new Play();
                        p.setPlay_id(playWebs.get(i).getPlayId());
                        Log.e("PlayIDDDD",p.getPlay_id()+"");
                        p.setPlay_introdution(playWebs.get(i).getPlayIntroduction());
                        p.setPlay_type(playWebs.get(i).getPlayType());
                        p.setPlay_length(playWebs.get(i).getPlayLength());
                        p.setPlay_lang(playWebs.get(i).getPlayLang());
                        p.setPlay_image(playWebs.get(i).getPlayImage());
                        p.setPlay_ticket_price(playWebs.get(i).getPlayTicketPrice());
                        p.setPlay_name(playWebs.get(i).getPlayName());
                        p.setPlay_status(playWebs.get(i).getPlayStatus());
                        plays.add(p);

                    }
                    arrayAdapterPlay = new ArrayAdapter<Play>(EditScheduleActivity.this,android.R.layout.simple_spinner_item,plays);
                    spinnerPlay.setAdapter(arrayAdapterPlay);
                    SpinnerAdapter spinnerPAdapter = spinnerPlay.getAdapter();
                    int k = spinnerPAdapter.getCount();
                    for(int i=0;i<k;i++)
                    {
                        if(schedule.getPlay_id()==((Play)spinnerPAdapter.getItem(i)).getPlay_id()){
                            spinnerPlay.setSelection(i,true);
                            break;
                        }
                    }

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
    }

    public void getStudio(){
        //创建一个请求队列
        studios.clear();
        requestQueue = Volley.newRequestQueue(this);
        String url = Grob_var.host+"/mobileStudio/getStudio";

        StringRequest stringRequest =new StringRequest(url, new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {
//                List<User> userList = gson.fromJson(string, new TypeToken<List<User>>() {}.getType());
//                List<User> userList = gson.fromJson(jsonArray, new TypeToken<List<User>>() {}.getType());
                try {
                    Gson gson = new Gson();
                    List<StudioWeb> studioWebs = new ArrayList<>();
                    studioWebs = gson.fromJson(s, new TypeToken<List<StudioWeb>>() {}.getType());
                    Log.e("playssssss",studioWebs.toString());
                    for(int i=0;i<studioWebs.size();i++)
                    {
                        Studio studio = new Studio();
                        studio.setStudio_id(studioWebs.get(i).getStudioId());
                        studio.setStudio_flag(studioWebs.get(i).getStudioFlag());
                        studio.setStudio_introduction(studioWebs.get(i).getStudioIntroduction());
                        studio.setStudio_row_count(studioWebs.get(i).getStudioRowCount());
                        studio.setStudio_col_count(studioWebs.get(i).getStudioColCount());
                        studio.setStudio_name(studioWebs.get(i).getStudioName());
                        studios.add(studio);

                    }
                    arrayAdapterStudio = new ArrayAdapter<>(EditScheduleActivity.this,android.R.layout.simple_spinner_item,studios);
                    spinnerStudio.setAdapter(arrayAdapterStudio);
                    SpinnerAdapter spinnerSAdapter = spinnerStudio.getAdapter();
                    int ss = spinnerSAdapter.getCount();
                    for(int i=0;i<ss;i++)
                    {
                        if(schedule.getStudio_id()==((Studio)spinnerSAdapter.getItem(i)).getStudio_id()){
                            spinnerStudio.setSelection(i,true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        requestQueue.add(stringRequest);
    }
    public void init()
    {

//        MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
//        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getReadableDatabase();
//        Cursor cursor = sqLiteDatabase.query("play",null,null,null,null,null,null);
//        if(cursor.moveToFirst())
//        {
//
//            do{
//                Play p = new Play();
//                p.setPlay_name(cursor.getString(cursor.getColumnIndex("play_name")));
//                p.setPlay_status(cursor.getInt(cursor.getColumnIndex("play_status")));
//                p.setPlay_ticket_price(cursor.getDouble(cursor.getColumnIndex("play_ticket_price")));
//                p.setPlay_image(cursor.getString(cursor.getColumnIndex("play_image")));
//                p.setPlay_length(cursor.getInt(cursor.getColumnIndex("play_length")));
//                p.setPlay_introdution(cursor.getString(cursor.getColumnIndex("play_introduction")));
//                p.setPlay_id(cursor.getInt(cursor.getColumnIndex("play_id")));
//                Log.e("play_id",cursor.getInt(cursor.getColumnIndex("play_id"))+"");
//                p.setPlay_lang(cursor.getString(cursor.getColumnIndex("play_lang")));
//                p.setPlay_type(cursor.getString(cursor.getColumnIndex("play_type")));
//                plays.add(p);
//
//            }while (cursor.moveToNext());
//        }
//        cursor = sqLiteDatabase.query("studio", null, null, null, null, null, null);
//        if (cursor.moveToFirst()) {
//
//            do {
//                Studio studio = new Studio();
//                studio.setStudio_id(cursor.getInt(cursor.getColumnIndex("studio_id")));
//                studio.setStudio_name(cursor.getString(cursor.getColumnIndex("studio_name")));
//                studio.setStudio_col_count(cursor.getInt(cursor.getColumnIndex("studio_col_count")));
//                studio.setStudio_row_count(cursor.getInt(cursor.getColumnIndex("studio_row_count")));
//                studio.setStudio_introduction(cursor.getString(cursor.getColumnIndex("studio_introduction")));
//                studio.setStudio_flag(cursor.getInt(cursor.getColumnIndex("studio_flag")));
//                studios.add(studio);
//            } while (cursor.moveToNext());
//
//        }
//        cursor.close();
        sure = findViewById(R.id.sure_editschedule);
        cancle= findViewById(R.id.cancel_editschedule);
        spinnerPlay = findViewById(R.id.edit_play_spinner);
        spinnerPlay.setPrompt("请选择影片:");
        spinnerStudio= findViewById(R.id.edit_studio_spinner);
        spinnerStudio.setPrompt("请选择影厅:");
        arrayAdapterPlay = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,plays);
        arrayAdapterStudio = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,studios);
        arrayAdapterStudio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterPlay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStudio.setAdapter(arrayAdapterStudio);
        spinnerPlay.setAdapter(arrayAdapterPlay);
        spinnerStudio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                s = studios.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerPlay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                p = plays.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        SpinnerAdapter spinnerPAdapter = spinnerPlay.getAdapter();
        int k = spinnerPAdapter.getCount();
        for(int i=0;i<k;i++)
        {
            if(schedule.getPlay_id()==((Play)spinnerPAdapter.getItem(i)).getPlay_id()){
                spinnerPlay.setSelection(i,true);
                break;
            }
        }
        SpinnerAdapter spinnerSAdapter = spinnerStudio.getAdapter();
        int s = spinnerSAdapter.getCount();
        for(int i=0;i<s;i++)
        {
            if(schedule.getStudio_id()==((Studio)spinnerSAdapter.getItem(i)).getStudio_id()){
                spinnerStudio.setSelection(i,true);
                break;
            }
        }
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
        ticket_price = findViewById(R.id.schedule_price_edit);
        ticket_price.setText(String.valueOf(schedule.getSched_ticket_price()));
        sched_date = findViewById(R.id.schedule_data_edit);
        sched_date.setOnClickListener(this);
        sched_date.setText(format0.format(schedule.getSched_time()));
        sched_time= findViewById(R.id.schedule_time_edit);
        sched_time.setOnClickListener(this);
        sched_time.setText(format1.format(schedule.getSched_time()));
        sure.setOnClickListener(this);
        cancle.setOnClickListener(this);
    }
    private void postDataWithParame() {
        Map<String,String> map = new HashMap<>();
        SimpleDateFormat format0 = new SimpleDateFormat("yy/MM/dd HH:mm");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String sd=sched_date.getText().toString()+" "+sched_time.getText().toString();
        Date date = null;
        try {
            date = format0.parse(sd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date.getTime()<new Date().getTime())
        {
            Toast.makeText(getApplication(),"时间不合法",Toast.LENGTH_SHORT).show();
            dialog.close();
            return ;
        }
        String sd1 = format1.format(date);
        map.put("schedId",schedule.getSched_id()+"");
        map.put("studioId",String.valueOf(s.getStudio_id()));//传入参数
        map.put("playId",String.valueOf(p.getPlay_id()));//传入参数
        map.put("schedTicketPrice",ticket_price.getText().toString());//传入参数
        map.put("schedTime",sd1);//传入参数

        JSONObject jsonObject = new JSONObject(map);

        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Grob_var.host+"mobileSchedule/updateSchedule")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                EditScheduleActivity.this.setResult(0);
                finish();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Message message = new Message();
                message.what = 1;
                if(response.body().string().equals("succeed"))
                {
                    EditScheduleActivity.this.setResult(1);
                }else{
                    EditScheduleActivity.this.setResult(0);
                    message.what = 2;
                }
                handler.sendMessage(message);
            }


        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.cancel_editschedule:
                this.setResult(-1);
                finish();
                break;
            case R.id.sure_editschedule:
                if(p==null||s==null)
                {
                    Toast.makeText(getApplication(),"没厅或影片不能更新计划",Toast.LENGTH_SHORT).show();

                    break;
                }else{
                    dialog = new LoadingDialog(EditScheduleActivity.this,"加载中,,,");
                    dialog.show();
//                    SimpleDateFormat format0 = new SimpleDateFormat("yy/MM/dd HH:mm");
//                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                    String sd=sched_date.getText().toString()+" "+sched_time.getText().toString();
//                    Date date = null;
//                    try {
//                        date = (Date)format0.parse(sd);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    String sd1 = format1.format(date);
////                    Toast.makeText(getApplication(),sd+"---"+sd1,Toast.LENGTH_SHORT).show();
//                    MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
//                    SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("studio_id",String.valueOf(s.getStudio_id()));
//                    contentValues.put("play_id",String.valueOf(p.getPlay_id()));
//                    contentValues.put("sched_ticket_price",ticket_price.getText().toString());
//                    contentValues.put("sched_time",sd1);
//
//
//                    int up =     sqLiteDatabase.update("schedule",contentValues,"sched_id = ?",
//                                new String[]{String.valueOf(schedule.getSched_id())});
////                        contentValues.put("studio_flag",0);
////                        sqLiteDatabase.update("studio",contentValues,"studio_id = ?",
////                                new String[]{String.valueOf(s.getStudio_id())});
//                   if(up==1)
//                   {
//                       this.setResult(1);
//                       if(p.getPlay_id()!=schedule.getPlay_id())
//                       {
//                           sqLiteDatabase.execSQL("update play set play_status = play_status-1 " +
//                                   " where play_id = " +schedule.getPlay_id());
//                       }else{
//                           sqLiteDatabase.execSQL("update play set play_status = play_status+1 " +
//                                   " where play_id = " +p.getPlay_id());
//                       }
//                       if(s.getStudio_id()!=schedule.getStudio_id())
//                       {
//                           sqLiteDatabase.execSQL("update studio set studio_flag = studio_flag-1 " +
//                                   " where studio_id = " +schedule.getStudio_id());
//                       }else{
//                           sqLiteDatabase.execSQL("update studio set studio_flag = studio_flag+1 " +
//                                   " where studio_id = " +s.getStudio_id());
//                       }
//
//                   }else{
//                       this.setResult(0);
//                   }
//                   dialog.show();
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
//                            SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
//
//                            ContentValues contentValues = new ContentValues();
//
//                            sqLiteDatabase.delete("ticket","sched_id = ?",new String[]{String.valueOf(schedule.getSched_id())});
//
//                            Cursor cursor = sqLiteDatabase.query("seat",null,"studio_id = ?",new String[]{String.valueOf(s.getStudio_id())},null,null,null);
//                            if(cursor.moveToFirst())
//                            {
//
//                                do{
//
//                                    if(cursor.getInt(cursor.getColumnIndex("seat_status"))==1)
//                                    {
//                                        contentValues.clear();
//                                        contentValues.put("seat_id", cursor.getInt(cursor.getColumnIndex("seat_id")));
//                                        contentValues.put("sched_id", schedule.getSched_id());
//                                        contentValues.put("ticket_price",ticket_price.getText().toString());
//                                        contentValues.put("ticket_status",0);
//                                        sqLiteDatabase.insert("ticket",null,contentValues);
//                                    }
//
//                                }while (cursor.moveToNext());
//                            }
//                            cursor.close();
//                            Message message = new Message();
//                            message.what = 1;
//                            handler.sendMessage(message);
//                        }
//                    }).start();
//
                    postDataWithParame();
                }
                break;
            case R.id.schedule_data_edit:
                DatePickerDialog datePicker=new DatePickerDialog(EditScheduleActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        sched_date.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);
//                        Toast.makeText(AddScheduleActivity.this, year+"year "+(monthOfYear+1)+"month "+dayOfMonth+"day", Toast.LENGTH_SHORT).show();
                    }
                }, Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH+1),Calendar.getInstance().get((Calendar.DATE)));
                datePicker.show();
                break;
            case R.id.schedule_time_edit:
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        sched_time.setText(hourOfDay+":"+minute);
//                        Toast.makeText(AddScheduleActivity.this,hourOfDay+"hour "+minute+"minute", Toast.LENGTH_SHORT).show();
                    }
                },Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),true);
                timePickerDialog.show();
                break;
        }
    }

}
