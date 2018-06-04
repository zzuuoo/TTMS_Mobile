package com.example.miaojie.ptest.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.example.miaojie.ptest.Adapter.PlayAdapter;
import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.MyApplication;
import com.example.miaojie.ptest.Utils.MyDatabaseHelper;
import com.example.miaojie.ptest.pojo.Play;

import java.util.ArrayList;
import java.util.List;

public class PlayManageActivity extends AppCompatActivity {

    private List<Play> list;
    private List<Play> searchlist;
    private Button play_add;
    private Toolbar play_toolbar;
    private boolean isSearch =false;
    ListView listView;
    SearchView play_searchview;
    PlayAdapter adapter;
    MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manage);

        //���ݻ�ȡ
        setData();
        //��ʼ��ҳ��
        init();


    }

    /**
     * listview����������
     * ����
     */
    public void init()
    {
        adapter=new PlayAdapter(this,R.layout.play_item,list);
        listView = (ListView) findViewById(R.id.play_list);
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
                Intent intent = new Intent(PlayManageActivity.this, PlayEditActivity.class);
                intent.putExtra("play", p);
                startActivityForResult(intent,2);//��ʱ����д�Ǹ����ص��ú���
//                Toast.makeText(getApplication(),p.getPlay_name(),Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Play p;
                if(isSearch){
                    p = searchlist.get(i);
                }else{
                    p = list.get(i);
                }
                AlertDialog.Builder dialog = new AlertDialog.Builder(PlayManageActivity.this);
                dialog.setTitle("����");
                dialog.setMessage("ȷ��ɾ����");
                dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
                        sqLiteDatabase.delete("play","play_id  =  ?",new String[]{String.valueOf(p.getPlay_id())});
                        Toast.makeText(getApplicationContext(), "ɾ���ɹ�"+p.getPlay_id(), Toast.LENGTH_SHORT).show();
                        setData();
                        adapter=new PlayAdapter(getApplicationContext(),R.layout.play_item,list);
                        listView.setAdapter(adapter);
                    }
                });
                dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "ȡ��", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                return true;
            }
        });
        play_toolbar = (Toolbar)findViewById(R.id.play_toolbar);
        play_toolbar.setTitle("");
        setSupportActionBar(play_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        play_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        play_add = (Button)findViewById(R.id.add_play);

        play_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayManageActivity.this, AddPlayActivity.class);
                startActivityForResult(intent,1);//��ʱ����д�Ǹ����ص��ú���
            }
        });

        play_searchview = (SearchView)findViewById(R.id.playSearchView);

        play_searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Toast.makeText(getApplicationContext(),newText, Toast.LENGTH_SHORT).show();
                searchlist = new ArrayList<Play>();
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

    }

//��ʼ��list�ϵ�����
    public void setData()
    {
        list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
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
                list.add(p);
            }while (cursor.moveToNext());
        }
        cursor.close();


    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        switch (requestCode)
        {
            case 1:
                //��ӣ��ɹ������list
                if(resultCode==1)
                {
                    setData();
                    adapter=new PlayAdapter(this,R.layout.play_item,list);
                    listView.setAdapter(adapter);
                    Toast.makeText(this,"��ӳɹ�",Toast.LENGTH_SHORT).show();
                }else if(resultCode==0)
                {
                    Toast.makeText(this,"���ʧ��",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"ȡ�����",Toast.LENGTH_SHORT).show();
                }

                break ;
            case 2://edit
//                �ɹ������list
                if(resultCode==1)
                {
                    setData();
                    adapter=new PlayAdapter(this,R.layout.play_item,list);
                    listView.setAdapter(adapter);
                    Toast.makeText(this,"�޸ĳɹ�",Toast.LENGTH_SHORT).show();
                }else if(resultCode==0)
                {
                    Toast.makeText(this,"�޸�ʧ��",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"ȡ���޸�",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


}
