package com.example.miaojie.ptest.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.LoadingDialog;
import com.example.miaojie.ptest.Utils.MyDatabaseHelper;
import com.example.miaojie.ptest.pojo.Play;
import com.example.miaojie.ptest.pojo.Schedule;
import com.example.miaojie.ptest.pojo.Studio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private EditText sched_date;
    private EditText sched_time;
    private Handler handler;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);
        Intent intent = this.getIntent();
        schedule = (Schedule) intent.getSerializableExtra("schedule");
        dialog = new LoadingDialog(EditScheduleActivity.this,"更新中...");
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        dialog.close();
                        Toast.makeText(EditScheduleActivity.this,"更新票成功",Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
            }
        };
        init();
    }
    public void init()
    {

        MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("play",null,null,null,null,null,null);
        if(cursor.moveToFirst())
        {

            do{
                Play p = new Play();
                p.setPlay_name(cursor.getString(cursor.getColumnIndex("play_name")));
                p.setPlay_status(cursor.getInt(cursor.getColumnIndex("play_status")));
                p.setPlay_ticket_price(cursor.getDouble(cursor.getColumnIndex("play_ticket_price")));
                p.setPlay_image(cursor.getString(cursor.getColumnIndex("play_image")));
                p.setPlay_length(cursor.getInt(cursor.getColumnIndex("play_length")));
                p.setPlay_introdution(cursor.getString(cursor.getColumnIndex("play_introduction")));
                p.setPlay_id(cursor.getInt(cursor.getColumnIndex("play_id")));
                Log.e("play_id",cursor.getInt(cursor.getColumnIndex("play_id"))+"");
                p.setPlay_lang(cursor.getString(cursor.getColumnIndex("play_lang")));
                p.setPlay_type(cursor.getString(cursor.getColumnIndex("play_type")));
                plays.add(p);

            }while (cursor.moveToNext());
        }
        cursor = sqLiteDatabase.query("studio", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {

            do {
                Studio studio = new Studio();
                studio.setStudio_id(cursor.getInt(cursor.getColumnIndex("studio_id")));
                studio.setStudio_name(cursor.getString(cursor.getColumnIndex("studio_name")));
                studio.setStudio_col_count(cursor.getInt(cursor.getColumnIndex("studio_col_count")));
                studio.setStudio_row_count(cursor.getInt(cursor.getColumnIndex("studio_row_count")));
                studio.setStudio_introduction(cursor.getString(cursor.getColumnIndex("studio_introduction")));
                studio.setStudio_flag(cursor.getInt(cursor.getColumnIndex("studio_flag")));
                studios.add(studio);
            } while (cursor.moveToNext());

        }
        cursor.close();
        sure = (Button)findViewById(R.id.sure_editschedule);
        cancle=(Button)findViewById(R.id.cancel_editschedule);
        spinnerPlay = (Spinner)findViewById(R.id.edit_play_spinner);
        spinnerPlay.setPrompt("请选择影片:");
        spinnerStudio=(Spinner)findViewById(R.id.edit_studio_spinner);
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
        ticket_price =(EditText)findViewById(R.id.schedule_price_edit);
        ticket_price.setText(String.valueOf(schedule.getSched_ticket_price()));
        sched_date = (EditText)findViewById(R.id.schedule_data_edit);
        sched_date.setText(format0.format(schedule.getSched_time()));
        sched_time=(EditText)findViewById(R.id.schedule_time_edit);
        sched_time.setText(format1.format(schedule.getSched_time()));
        sure.setOnClickListener(this);
        cancle.setOnClickListener(this);
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
                    SimpleDateFormat format0 = new SimpleDateFormat("yy/MM/dd HH:mm");
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String sd=sched_date.getText().toString()+" "+sched_time.getText().toString();
                    Date date = null;
                    try {
                        date = (Date)format0.parse(sd);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String sd1 = format1.format(date);
//                    Toast.makeText(getApplication(),sd+"---"+sd1,Toast.LENGTH_SHORT).show();
                    MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
                    SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("studio_id",String.valueOf(s.getStudio_id()));
                    contentValues.put("play_id",String.valueOf(p.getPlay_id()));
                    contentValues.put("sched_ticket_price",ticket_price.getText().toString());
                    contentValues.put("sched_time",sd1);


                    int up =     sqLiteDatabase.update("schedule",contentValues,"sched_id = ?",
                                new String[]{String.valueOf(schedule.getSched_id())});
//                        contentValues.put("studio_flag",0);
//                        sqLiteDatabase.update("studio",contentValues,"studio_id = ?",
//                                new String[]{String.valueOf(s.getStudio_id())});
                   if(up==1)
                   {
                       this.setResult(1);
                       if(p.getPlay_id()!=schedule.getPlay_id())
                       {
                           sqLiteDatabase.execSQL("update play set play_status = play_status-1 " +
                                   " where play_id = " +schedule.getPlay_id());
                       }else{
                           sqLiteDatabase.execSQL("update play set play_status = play_status+1 " +
                                   " where play_id = " +p.getPlay_id());
                       }
                       if(s.getStudio_id()!=schedule.getStudio_id())
                       {
                           sqLiteDatabase.execSQL("update studio set studio_flag = studio_flag-1 " +
                                   " where studio_id = " +schedule.getStudio_id());
                       }else{
                           sqLiteDatabase.execSQL("update studio set studio_flag = studio_flag+1 " +
                                   " where studio_id = " +s.getStudio_id());
                       }

                   }else{
                       this.setResult(0);
                   }
                   dialog.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
                            SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();

                            ContentValues contentValues = new ContentValues();

                            sqLiteDatabase.delete("ticket","sched_id = ?",new String[]{String.valueOf(schedule.getSched_id())});

                            Cursor cursor = sqLiteDatabase.query("seat",null,"studio_id = ?",new String[]{String.valueOf(s.getStudio_id())},null,null,null);
                            if(cursor.moveToFirst())
                            {

                                do{

                                    if(cursor.getInt(cursor.getColumnIndex("seat_status"))==1)
                                    {
                                        contentValues.clear();
                                        contentValues.put("seat_id", cursor.getInt(cursor.getColumnIndex("seat_id")));
                                        contentValues.put("sched_id", schedule.getSched_id());
                                        contentValues.put("ticket_price",ticket_price.getText().toString());
                                        contentValues.put("ticket_status",0);
                                        sqLiteDatabase.insert("ticket",null,contentValues);
                                    }

                                }while (cursor.moveToNext());
                            }
                            cursor.close();
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }).start();

//                    finish();
                }
                break;
        }
    }

}
