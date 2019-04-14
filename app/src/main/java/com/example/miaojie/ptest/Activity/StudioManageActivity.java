package com.example.miaojie.ptest.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.miaojie.ptest.Adapter.StudioAdapter;
import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.LoadingDialog;
import com.example.miaojie.ptest.Utils.MyApplication;
import com.example.miaojie.ptest.pojo.Grob_var;
import com.example.miaojie.ptest.pojo.Studio;
import com.example.miaojie.ptest.pojo.StudioWeb;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class StudioManageActivity extends AppCompatActivity {

    private List<Studio> studio_list = new ArrayList<>();
    private List<Studio> searchlist = new ArrayList<>();
    ListView listView;
    private boolean isSearch = false;
    Button add_studio ;
    Toolbar studio_toolbar;
    SearchView studio_searchview;
    StudioAdapter studioAdapter;
    RequestQueue requestQueue;
    LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio_manage);
        setStudioData();
        init();


    }

    public void init()
    {
//        studioAdapter = new StudioAdapter(this, R.layout.studio_item, studio_list);
        listView = findViewById(R.id.studio_list);
        add_studio = findViewById(R.id.add_studio);
        add_studio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudioManageActivity.this,AddStudioActivity.class);
                startActivityForResult(intent,1);
            }
        });
        studio_searchview = findViewById(R.id.studioSearchView);

        studio_searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchlist.clear();
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


//        listView.setAdapter(studioAdapter);
        studio_toolbar = findViewById(R.id.studio_toolbar);
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
                Intent intent = new Intent(StudioManageActivity.this, EditStudioActivity.class);
                intent.putExtra("studio", s);
                startActivityForResult(intent,2);//到时候重写那个返回调用函数
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Studio s;
                if(isSearch){
                    s=searchlist.get(i);
                }else{
                    s=studio_list.get(i);
                }
                AlertDialog.Builder adialog = new AlertDialog.Builder(StudioManageActivity.this);
                adialog.setTitle("警告");
                adialog.setMessage("确认删除吗？");
                adialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

//                        SQLiteDatabase sqLiteDatabase = MyDatabaseHelper.getInstance().getWritableDatabase();
//                        sqLiteDatabase.delete("studio","studio_id  =  ?",new String[]{String.valueOf(s.getStudio_id())});
//                        sqLiteDatabase.delete("seat","studio_id = ?",new String[]{String.valueOf(s.getStudio_id())});
//                        Toast.makeText(getApplicationContext(), "删除成功"+s.getStudio_id(), Toast.LENGTH_SHORT).show();
//                        setStudioData();
//                        studioAdapter = new StudioAdapter(getApplicationContext(), R.layout.studio_item, studio_list);
//                        listView.setAdapter(studioAdapter);
                        delete(s.getStudio_id());

                    }
                });
                adialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "取消", Toast.LENGTH_SHORT).show();
                    }
                });
                adialog.show();
                return true;
            }
        });}

    public void delete(int studio_id)
    {
        //创建一个请求队列
        requestQueue = Volley.newRequestQueue(this);
        dialog = new LoadingDialog(StudioManageActivity.this,"删除中...");
        dialog.show();

        StringRequest stringRequest =new StringRequest(Grob_var.host+"mobileStudio/deleteStudio?studio_id="+studio_id, new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {

                dialog.close();
                if(s.equals("succeed"))
                {
                    get();
                    Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"删除失败",Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.close();
                Toast.makeText(getApplicationContext(),"加载异常",Toast.LENGTH_SHORT).show();
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
    }

    public void setStudioData() {
//        studio_list = new ArrayList<>();
//        SQLiteDatabase sqLiteDatabase = MyDatabaseHelper.getInstance().getWritableDatabase();
//        Cursor cursor = sqLiteDatabase.query("studio", null, null, null, null, null, null);
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
//                studio_list.add(studio);
//            } while (cursor.moveToNext());
//
//        }
//
//        cursor.close();
        get();
    }
    public void get(){
        //创建一个请求队列
        studio_list.clear();
        dialog = new LoadingDialog(StudioManageActivity.this,"加载...");
        dialog.show();
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
                        studio_list.add(studio);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    dialog.close();
                    studioAdapter = new StudioAdapter(StudioManageActivity.this, R.layout.studio_item, studio_list);
                    listView.setAdapter(studioAdapter);
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

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        switch (requestCode)
        {
            case 1:
                //添加，成功则更新list
                if(resultCode==1)
                {
                    setStudioData();
                    studioAdapter = new StudioAdapter(this, R.layout.studio_item, studio_list);
                    listView.setAdapter(studioAdapter);
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
                    setStudioData();
                    studioAdapter = new StudioAdapter(this, R.layout.studio_item, studio_list);
                    listView.setAdapter(studioAdapter);
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
