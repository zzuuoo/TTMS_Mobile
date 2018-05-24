package com.example.miaojie.ptest.Activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.MyDatabaseHelper;
import com.example.miaojie.ptest.pojo.Play;
import com.example.miaojie.ptest.pojo.Studio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddScheduleActivity extends AppCompatActivity implements View.OnClickListener {
    private Play p = null;
    private Studio s = null;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
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
        sure = (Button)findViewById(R.id.sure_addschedule);
        cancle=(Button)findViewById(R.id.cancel_addschedule);
        spinnerPlay = (Spinner)findViewById(R.id.add_play_spinner);
        spinnerPlay.setPrompt("请选择影片:");
        spinnerStudio=(Spinner)findViewById(R.id.add_studio_spinner);
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
        ticket_price =(EditText)findViewById(R.id.schedule_price_add);
        sched_date = (EditText)findViewById(R.id.schedule_data_add);
        sched_time=(EditText)findViewById(R.id.schedule_time_add);
        sure.setOnClickListener(this);
        cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.cancel_addschedule:
                finish();
                break;
            case R.id.sure_addschedule:
                if(p==null||s==null)
                {
                    Toast.makeText(getApplication(),"没厅或影片不能添加计划",Toast.LENGTH_SHORT).show();
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

                    try {
                        sqLiteDatabase.insert("schedule",null,contentValues);
//                        contentValues.clear();
//                        contentValues.put("studio_flag",0);
//                        sqLiteDatabase.update("studio",contentValues,"studio_id = ?",
//                                new String[]{String.valueOf(s.getStudio_id())});
                    }catch (Exception e)
                    {
                        this.setResult(0);
                        finish();
                    }
                    this.setResult(1);
                    finish();
                }
                break;
        }
    }
}
