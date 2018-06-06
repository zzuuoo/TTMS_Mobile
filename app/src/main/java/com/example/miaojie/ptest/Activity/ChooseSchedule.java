package com.example.miaojie.ptest.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.miaojie.ptest.Adapter.ScheduleAdapter;
import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.MyApplication;
import com.example.miaojie.ptest.Utils.MyDatabaseHelper;
import com.example.miaojie.ptest.pojo.Play;
import com.example.miaojie.ptest.pojo.Schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChooseSchedule extends AppCompatActivity {
    private List<Schedule> scheduleList = new ArrayList<>();
    private List<Schedule> searchlist = new ArrayList<Schedule>();
    private Toolbar schedule_toolbar;
    private SearchView schdule_searchview;
    private ScheduleAdapter scheduleAdapter;
    private ListView listView;
    private boolean isSearch = false;
    private Play play=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_schedule);
        Intent intent = getIntent();
        play= (Play) intent.getSerializableExtra("play");
        initScheduleData();
        init();
    }
    public void initScheduleData()
    {
        scheduleList = new ArrayList<>();
        SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //本地数据库
        SQLiteDatabase sqLiteDatabase = MyDatabaseHelper.getInstance().getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("schedule", null, "play_id = ? and sched_time > ?", new String[]{String.valueOf(play.getPlay_id()),format.format(new Date())}, null, null, null);
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
                Log.e("scheduleSale",schedule.getSched_id()+"");
                scheduleList.add(schedule);
            } while (cursor.moveToNext());

        }
        cursor.close();

        /**
         * 从服务器获取
         */


    }

    public void init()
    {
        listView = (ListView)findViewById(R.id.sale_schedule_list);
        scheduleAdapter = new ScheduleAdapter(this,R.layout.schedule_item,scheduleList);

        listView.setAdapter(scheduleAdapter);


        schedule_toolbar = (Toolbar)findViewById(R.id.sale_schedule_toolbar);
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
                Intent intent = new Intent(ChooseSchedule.this,SaleTicketChooseSeat.class);
                intent.putExtra("schedule", s);
                startActivityForResult(intent,2);
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
                Toast.makeText(getApplicationContext(),sc.getSched_id()+"",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        schdule_searchview = (SearchView)findViewById(R.id.sale_scheduleSearchView);

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
    }

}
