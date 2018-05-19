package com.example.miaojie.ptest.Activity;

import android.content.DialogInterface;
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
import com.example.miaojie.ptest.pojo.Schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleManageActivity extends AppCompatActivity {
    private List<Schedule> scheduleList = new ArrayList<>();
    private List<Schedule> searchlist;
    private Toolbar schedule_toolbar;
    private SearchView schdule_searchview;
    private Button schedule_add;
    private ScheduleAdapter scheduleAdapter;
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
        ListView listView = (ListView)findViewById(R.id.schedule_list);
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

                Toast.makeText(getApplication(), s.getSched_id()+"", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Schedule s = scheduleList.get(i);
//                Toast.makeText(getApplicationContext(), "弹出删除按钮", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(ScheduleManageActivity.this);
                dialog.setTitle("警告");
                dialog.setMessage("确认删除吗？");
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "删除了啦", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(),newText, Toast.LENGTH_SHORT).show();
                searchlist = new ArrayList<Schedule>();
                for(int i=0;i<scheduleList.size();i++){
                    if(String.valueOf(scheduleList.get(i).getPlay_id()).contains(newText)){
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
                Toast.makeText(getApplicationContext(),"添加",Toast.LENGTH_SHORT).show();
            }
        });}

    public void initScheduleData()
    {
        for(int i = 1;i<20;i++)
        {
            Schedule schedule = new Schedule();
            schedule.setSched_id(i);
            schedule.setPlay_id(i+1);
            schedule.setSched_ticket_price(i*2);
            schedule.setSched_time(new Date());
            schedule.setStudio_id(i+2);
            scheduleList.add(schedule);
        }
    }

}
