package com.example.miaojie.ptest.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.LoadingDialog;
import com.example.miaojie.ptest.Utils.SeatTableManage;
import com.example.miaojie.ptest.pojo.Grob_var;
import com.example.miaojie.ptest.pojo.Seat;
import com.example.miaojie.ptest.pojo.SeatWeb;
import com.example.miaojie.ptest.pojo.Studio;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class SeatManageActivity extends AppCompatActivity {

    SeatTableManage seatTable;
    ArrayList<Seat> seats =new ArrayList<>();
    private Handler handler;
    private Toolbar toolbar;
    private Button update;
    private Studio studio = null;
    LoadingDialog dialog;
    RequestQueue requestQueue;
//    ProgressDialog  progressDialog = new ProgressDialog(SeatManageActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_manage);
        init();

    }
    public void initSeat()
    {
        seats.clear();
//        SQLiteDatabase sqLiteDatabase = MyDatabaseHelper.getInstance().getWritableDatabase();
//        Cursor cursor = sqLiteDatabase.query("seat",null,"studio_id = ?",new String[]{String.valueOf(studio.getStudio_id())},null,null,null);
//        if(cursor.moveToFirst())
//        {
//
//            do{
//                Seat s = new Seat();
//                s.setSeat_id(cursor.getInt(cursor.getColumnIndex("seat_id")));
//                s.setStudio_id(cursor.getInt(cursor.getColumnIndex("studio_id")));
//                s.setSeat_row(cursor.getInt(cursor.getColumnIndex("seat_row")));
//                s.setSeat_column(cursor.getInt(cursor.getColumnIndex("seat_column")));
//                s.setSeat_status(cursor.getInt(cursor.getColumnIndex("seat_status")));
//                Log.e("seatManageActivity","col:"+s.getSeat_column()+"row:"+s.getSeat_row());
//
//                seats.add(s);
//            }while (cursor.moveToNext());
//        }
//        cursor.close();
        get();

    }

    public void get(){
        //创建一个请求队列
        seats.clear();
        dialog = new LoadingDialog(SeatManageActivity.this,"加载...");
        dialog.show();
        requestQueue = Volley.newRequestQueue(this);
        String url = Grob_var.host+"/mobileSeat/getSeatByStudioId?studioId="+studio.getStudio_id();

        StringRequest stringRequest =new StringRequest(url, new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {
                try {
                    Gson gson = new Gson();
                    List<SeatWeb> seatWebs = new ArrayList<>();
                    seatWebs = gson.fromJson(s, new TypeToken<List<SeatWeb>>() {}.getType());
                    for(int i=0;i<seatWebs.size();i++)
                    {
                       Seat seat = new Seat();
                       seat.setSeat_id(seatWebs.get(i).getSeatId());
                       seat.setStudio_id(seatWebs.get(i).getStudioId());
                       seat.setSeat_row(seatWebs.get(i).getSeatRow());
                       seat.setSeat_column(seatWebs.get(i).getSeatColumn());
                       seat.setSeat_status(seatWebs.get(i).getSeatStatus());
                        Log.e("SeatManage","状态"+seat.getSeat_status());
                       seats.add(seat);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    dialog.close();
                    seatTable.setSeats(seats);
                    seatTable.invalidate();

                }



            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                volley_result.setText("加载错误"+volleyError);
                dialog.close();
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
    }
    public void init()
    {
        dialog = new LoadingDialog(SeatManageActivity.this,"座位更新中...");
        Intent intent = this.getIntent();
        studio = (Studio) intent.getSerializableExtra("studio");
        seatTable = findViewById(R.id.seatmanagetableView);

        initSeat();
        update = findViewById(R.id.update_seat);

        seatTable.setScreenName(studio.getStudio_name());//设置屏幕名称
        seatTable.setSeats(seats);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        dialog.close();
                        seatTable.setSeats(seats);
                        seatTable.invalidate();
                        Toast.makeText(SeatManageActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        dialog.close();
                        Toast.makeText(SeatManageActivity.this,"更新失败",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        seatTable.setSeatChecker(new SeatTableManage.SeatChecker() {

            @Override
            public boolean isValidSeat(int row, int column) {

                Seat s =null;
                for(int i=0;i<seats.size();i++)
                {
                    s=seats.get(i);
                    if(s.getSeat_row()-1==row&&s.getSeat_column()-1==column)
                    {
                       if( s.getSeat_status()==0)
                       {
                           return false;
                       }
                    }
                }
                return true;
            }

            @Override
            public boolean isSold(int row, int column) {

                return false;
            }

            @Override
            public void checked(int row, int column) {



            }

            @Override
            public void unCheck(int row, int column) {

            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }

        });
        seatTable.setData(studio.getStudio_row_count(),studio.getStudio_col_count());
        toolbar= findViewById(R.id.mansge_seat_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeatManageActivity.this.setResult(-1);
                finish();
            }
        });

        findViewById(R.id.update_seat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MainActivity.isLogin)
                {
                    Toast.makeText(SeatManageActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                    return;
                }

                //在此更新数据库

                dialog = new LoadingDialog(SeatManageActivity.this,"更新中..");
                dialog.show();
                seats = seatTable.getSeats();
                updateSeat();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
//                        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
//                        ContentValues contentValues = new ContentValues();
//                        for(int i=0;i<seats.size();i++)
//                        {
//                            contentValues.clear();
//                            contentValues.put("studio_id", seats.get(i).getStudio_id());
//                            contentValues.put("seat_row",seats.get(i).getSeat_row());
//                            contentValues.put("seat_column",seats.get(i).getSeat_column());
//                            contentValues.put("seat_status",seats.get(i).getSeat_status());
//                            sqLiteDatabase.update("seat",contentValues,"seat_id = ?",
//                                    new String[]{String.valueOf(seats.get(i).getSeat_id())});
//
//                        }


//                        initSeat();
//
//                        Message message = new Message();
//                        message.what = 1;
//                        handler.sendMessage(message);
//                    }
//                }).start();


            }
        });

    }

    public JSONArray getArray(List<Seat> seatss)
    {
        JSONArray jsonArray = new JSONArray();
        for(int i=0;i<seatss.size();i++) {
            Map<String, String> map = new HashMap<>();
            map.put("studioId", studio.getStudio_id() + "");
            map.put("seatRow", seatss.get(i).getSeat_row() + "");
            map.put("seatColumn", seatss.get(i).getSeat_column() + "");//传入参数
            map.put("seatStatus", seatss.get(i).getSeat_status() + "");//传入参数
            map.put("seatId", seatss.get(i).getSeat_id() + "");//传入参数
            JSONObject jsonObject = new JSONObject(map);
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }


    public void updateSeat()
    {

        JSONArray jsonArray = getArray(seats);
            OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
            RequestBody body = RequestBody.create(JSON, jsonArray.toString());
            Request request = new Request.Builder()
                    .url(Grob_var.host+"mobileSeat/updateByPrimaryKey")
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                    SeatManageActivity.this.setResult(0);
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    if(response.body().string().equals("failed"))
                    {
                        Message message = new Message();
                        message.what = 2;
                        handler.sendMessage(message);
                        SeatManageActivity.this.setResult(0);
//                        finish();
                    }else{
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        SeatManageActivity.this.setResult(1);
                    }
                }

            });

    }

}