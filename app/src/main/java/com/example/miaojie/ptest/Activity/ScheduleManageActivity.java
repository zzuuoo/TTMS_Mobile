package com.example.miaojie.ptest.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.miaojie.ptest.Adapter.ScheduleAdapter;
import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.MyApplication;
import com.example.miaojie.ptest.Utils.MyDatabaseHelper;
import com.example.miaojie.ptest.pojo.Schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleManageActivity extends AppCompatActivity {
    private List<Schedule> scheduleList = new ArrayList<>();
    private List<Schedule> searchlist = new ArrayList<Schedule>();
    private Toolbar schedule_toolbar;
    private SearchView schdule_searchview;
    private Button schedule_add;
    private ScheduleAdapter scheduleAdapter;
    ListView listView;
    private boolean isSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_manage);
        initScheduleData();
        init();

    }

    public void init()
    { scheduleAdapter = new ScheduleAdapter(this,R.layout.schedule_item,scheduleList);
        listView = (ListView)findViewById(R.id.schedule_list);
        listView.setAdapter(scheduleAdapter);
        schedule_toolbar = (Toolbar)findViewById(R.id.schedule_toolbar);
        schedule_toolbar.setTitle("");
        setSupportActionBar(schedule_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        schedule_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Schedule s;
                if(isSearch){
                    s = searchlist.get(i);
                }else{
                    s = scheduleList.get(i);
                }
                Intent intent = new Intent(ScheduleManageActivity.this,EditScheduleActivity.class);
                intent.putExtra("schedule", s);
                startActivityForResult(intent,2);
//                Toast.makeText(getApplication(), s.getSched_id()+"", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Schedule sc;
                if(isSearch){
                    sc = searchlist.get(i);
                }else{
                    sc = scheduleList.get(i);
                }
                AlertDialog.Builder dialog = new AlertDialog.Builder(ScheduleManageActivity.this);
                dialog.setTitle("警告");
                dialog.setMessage("确认删除吗？");
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteDatabase sqLiteDatabase = MyDatabaseHelper.getInstance().getWritableDatabase();
                        sqLiteDatabase.delete("schedule","sched_id  =  ?",new String[]{String.valueOf(sc.getSched_id())});
                        sqLiteDatabase.execSQL("update play set play_status = play_status-1 " +
                                " where play_id = " +sc.getPlay_id());
                        sqLiteDatabase.execSQL("update studio set studio_flag = studio_flag-1 " +
                                " where studio_id = " +sc.getStudio_id());
                        sqLiteDatabase.delete("ticket","sched_id  =  ?",new String[]{String.valueOf(sc.getSched_id())});
                        Toast.makeText(getApplicationContext(), "删除了啦", Toast.LENGTH_SHORT).show();
                        initScheduleData();
                        scheduleAdapter = new ScheduleAdapter(getApplication(),R.layout.schedule_item,scheduleList);
                        listView.setAdapter(scheduleAdapter);
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "取消", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                return true;
            }
        });

        schdule_searchview = (SearchView)findViewById(R.id.scheduleSearchView);

        schdule_searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Toast.makeText(getApplicationContext(),newText, Toast.LENGTH_SHORT).show();
                searchlist.clear();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
                SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getReadableDatabase();

                for(int i=0;i<scheduleList.size();i++){
                    Cursor cursor = sqLiteDatabase.query("play",null,"play_id = ? "
                            ,new String[]{String.valueOf(scheduleList.get(i).getPlay_id())},null,null,null,null);
                    String play_names = "";
                    if(cursor.moveToFirst()){
                        play_names = cursor.getString(cursor.getColumnIndex("play_name"));
                    }
                    cursor = sqLiteDatabase.query("studio",null,"studio_id = ? "
                            ,new String[]{String.valueOf(scheduleList.get(i).getStudio_id())},null,null,null,null);
                    String studio_names = "";
                    if(cursor.moveToFirst()){
                        studio_names = cursor.getString(cursor.getColumnIndex("studio_name"));
                    }
                    if((play_names+studio_names+format.format(scheduleList.get(i).getSched_time())
                            +scheduleList.get(i).getSched_ticket_price()).contains(newText)){
                        searchlist.add(scheduleList.get(i));
                    }
                }
                if(searchlist.size()>0&&newText.length()>0){
                    scheduleAdapter = new ScheduleAdapter(MyApplication.getContext(),R.layout.schedule_item,searchlist);
                    listView.setAdapter(scheduleAdapter);
                    isSearch=true;

                }
                if(newText.length()==0)
                {
                    scheduleAdapter = new ScheduleAdapter(MyApplication.getContext(),R.layout.schedule_item,scheduleList);
                    listView.setAdapter(scheduleAdapter);
                    isSearch=false;
                }
                return false;
            }
        });

        schedule_add = (Button)findViewById(R.id.add_schedule);
        schedule_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScheduleManageActivity.this,AddScheduleActivity.class);
                startActivityForResult(intent,1);
            }
        });}

    public void initScheduleData()
    {
        scheduleList.clear();
        SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SQLiteDatabase sqLiteDatabase = MyDatabaseHelper.getInstance().getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("schedule", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {

            do {
                Schedule schedule = new Schedule();
                schedule.setSched_id(cursor.getInt(cursor.getColumnIndex("sched_id")));
                schedule.setPlay_id(cursor.getInt(cursor.getColumnIndex("play_id")));
                schedule.setStudio_id(cursor.getInt(cursor.getColumnIndex("studio_id")));
                schedule.setSched_ticket_price(cursor.getDouble(cursor.getColumnIndex("sched_ticket_price")));
                try {
                    schedule.setSched_time((Date)format.parse(cursor.getString(cursor.getColumnIndex("sched_time"))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                scheduleList.add(schedule);
            } while (cursor.moveToNext());

        }
        cursor.close();

    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        switch (requestCode)
        {
            case 1:
                //添加，成功则更新list
                if(resultCode==1)
                {
                    initScheduleData();
                    scheduleAdapter = new ScheduleAdapter(this,R.layout.schedule_item,scheduleList);
                    listView.setAdapter(scheduleAdapter);
                    Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
                }else if(resultCode==0)
                {
                    Toast.makeText(this,"添加失败",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"取消添加",Toast.LENGTH_SHORT).show();
                }

                break ;
            case 2://edit
//                成功则更新list
                if(resultCode==1)
                {
                    initScheduleData();
                    scheduleAdapter = new ScheduleAdapter(this,R.layout.schedule_item,scheduleList);
                    listView.setAdapter(scheduleAdapter);
                    Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
                }else if(resultCode==0)
                {
                    Toast.makeText(this,"修改失败",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"取消修改",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

}
