package com.example.miaojie.ptest.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.LoadingDialog;
import com.example.miaojie.ptest.Utils.MyDatabaseHelper;
import com.example.miaojie.ptest.Utils.SeatTableManage;
import com.example.miaojie.ptest.pojo.Seat;
import com.example.miaojie.ptest.pojo.Studio;

import java.util.ArrayList;

public class SeatManageActivity extends AppCompatActivity {

    SeatTableManage seatTable;
    ArrayList<Seat> seats =new ArrayList<>();
    private Handler handler;
    private Toolbar toolbar;
    private Button update;
    private Studio studio = null;
    LoadingDialog dialog;
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
        SQLiteDatabase sqLiteDatabase = MyDatabaseHelper.getInstance().getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("seat",null,"studio_id = ?",new String[]{String.valueOf(studio.getStudio_id())},null,null,null);
        if(cursor.moveToFirst())
        {

            do{
                Seat s = new Seat();
                s.setSeat_id(cursor.getInt(cursor.getColumnIndex("seat_id")));
                s.setStudio_id(cursor.getInt(cursor.getColumnIndex("studio_id")));
                s.setSeat_row(cursor.getInt(cursor.getColumnIndex("seat_row")));
                s.setSeat_column(cursor.getInt(cursor.getColumnIndex("seat_column")));
                s.setSeat_status(cursor.getInt(cursor.getColumnIndex("seat_status")));
                Log.e("seatManageActivity","col:"+s.getSeat_column()+"row:"+s.getSeat_row());

                seats.add(s);
            }while (cursor.moveToNext());
        }
        cursor.close();

    }
    public void init()
    {
        dialog = new LoadingDialog(SeatManageActivity.this,"座位更新中...");
        Intent intent = this.getIntent();
        studio = (Studio) intent.getSerializableExtra("studio");
        seatTable = (SeatTableManage) findViewById(R.id.seatmanagetableView);

        initSeat();
        update = (Button)findViewById(R.id.update_seat);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeatManageActivity.this.setResult(0);
                finish();
            }
        });

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
        toolbar= (Toolbar) findViewById(R.id.mansge_seat_toolbar);
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

                dialog.show();
                seats = seatTable.getSeats();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
                        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        for(int i=0;i<seats.size();i++)
                        {
                            contentValues.clear();
                            contentValues.put("studio_id", seats.get(i).getStudio_id());
                            contentValues.put("seat_row",seats.get(i).getSeat_row());
                            contentValues.put("seat_column",seats.get(i).getSeat_column());
                            contentValues.put("seat_status",seats.get(i).getSeat_status());
                            sqLiteDatabase.update("seat",contentValues,"seat_id = ?",
                                    new String[]{String.valueOf(seats.get(i).getSeat_id())});

                        }
                        initSeat();

                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }).start();


            }
        });

    }


}