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

import com.example.miaojie.ptest.Adapter.StudioAdapter;
import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.MyApplication;
import com.example.miaojie.ptest.pojo.Studio;

import java.util.ArrayList;
import java.util.List;

public class StudioManageActivity extends AppCompatActivity {

    private List<Studio> studio_list;
    private List<Studio> searchlist;
    private boolean isSearch = false;
    Button add_studio ;
    Toolbar studio_toolbar;
    SearchView studio_searchview;
    StudioAdapter studioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio_manage);
        setStudioData();
        init();

    }

    public void init()
    {        studioAdapter = new StudioAdapter(this, R.layout.studio_item, studio_list);
        ListView listView = (ListView) findViewById(R.id.studio_list);
        add_studio = (Button)findViewById(R.id.add_studio);
        add_studio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"add",Toast.LENGTH_SHORT).show();
            }
        });
        studio_searchview = (SearchView)findViewById(R.id.studioSearchView);

        studio_searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchlist = new ArrayList<Studio>();
                for(int i=0;i<studio_list.size();i++){
                    if(studio_list.get(i).getStudio_name().contains(newText)){
                        searchlist.add(studio_list.get(i));
                    }
                }
                if(searchlist.size()>0&&newText.length()>0){
                    studioAdapter = new StudioAdapter(MyApplication.getContext(),R.layout.studio_item,searchlist);
                    listView.setAdapter(studioAdapter);
                    isSearch=true;

                }
                if(newText.length()==0)
                {
                    studioAdapter = new StudioAdapter(MyApplication.getContext(),R.layout.studio_item,studio_list);
                    listView.setAdapter(studioAdapter);
                    isSearch=false;
                }
                return false;
            }
        });


        listView.setAdapter(studioAdapter);
        studio_toolbar = (Toolbar)findViewById(R.id.studio_toolbar);
        studio_toolbar.setTitle("");
        setSupportActionBar(studio_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        studio_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Studio s;
                if(isSearch){
                    s = searchlist.get(i);
                }else {
                    s = studio_list.get(i);
                }
                Toast.makeText(getApplication(), s.getStudio_name(), Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Studio s = studio_list.get(i);
                AlertDialog.Builder dialog = new AlertDialog.Builder(StudioManageActivity.this);
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
        });}

    public void setStudioData() {
        studio_list = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            Studio s = new Studio();
            s.setStudio_col_count(i + 5);
            s.setStudio_flag(i % 2);
            s.setStudio_id(i + 1);
            s.setStudio_introduction("我是厅" + i);
            s.setStudio_name("厅" + i);
            s.setStudio_row_count(i + 3);
            studio_list.add(s);
        }
    }
}
