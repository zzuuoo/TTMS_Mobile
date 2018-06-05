package com.example.miaojie.ptest.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.miaojie.ptest.Activity.ChooseSchedule;
import com.example.miaojie.ptest.Adapter.PlayAdapter;
import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.MyApplication;
import com.example.miaojie.ptest.Utils.MyDatabaseHelper;
import com.example.miaojie.ptest.pojo.Play;

import java.util.ArrayList;



/**
 * Created by zuo on 2018/6/13.
 */

public class MoiveListFragment extends Fragment {
    private ArrayList<Play>list;
    private ListView listView;
    private SearchView searchView;
    private boolean isSearch =false;
    PlayAdapter adapter;
    private ArrayList<Play>searchlist=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_play_list,container,false);
        listView = (ListView) view.findViewById(R.id.sale_Play_list);
        list=new ArrayList<>();
        setData();
        searchView= (SearchView) view.findViewById(R.id.salePlaySearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchlist.clear();
                for(int i=0;i<list.size();i++){
                    if(list.get(i).getPlay_name().contains(newText)){
                        searchlist.add(list.get(i));
                    }
                }
                if(searchlist.size()>0&&newText.length()>0){
                    adapter = new PlayAdapter(MyApplication.getContext(),R.layout.play_item,searchlist);
                    listView.setAdapter(adapter);
                    isSearch=true;

                }
                if(newText.length()==0)
                {
                    adapter = new PlayAdapter(MyApplication.getContext(),R.layout.play_item,list);
                    listView.setAdapter(adapter);
                    isSearch=false;
                }
                return false;
            }
        });

        adapter=new PlayAdapter(getContext(),R.layout.play_item,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Play p;
                if(isSearch){
                    p = searchlist.get(i);
                }else{
                    p = list.get(i);
                }
                Intent intent = new Intent(getContext(), ChooseSchedule.class);
                intent.putExtra("play", p);
                startActivityForResult(intent,2);//到时候重写那个返回调用函数
            }
        });
        //长按查看详细信息
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return true;
            }
        });


        return view;
    }
    private void setData()
    {
        list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = MyDatabaseHelper.getInstance().getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("play",null," play_status > ?",new String[]{"0"},null,null,null);
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
                list.add(p);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

}
