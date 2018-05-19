
package com.example.miaojie.ptest.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.pojo.User;

import java.util.ArrayList;
import java.util.List;

public class UserManageActivity extends AppCompatActivity implements View.OnClickListener {

    private List<User> userList = new ArrayList<>();
    private List<User> userSearchList = new ArrayList<>();
    private ListView ls = null;
    private Button addUser;
    private Toolbar user_toolbar;
    private boolean isSearch =false;
    private ListView listView;
    private SearchView user_searchview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage);
        init();

    }
    public void init()
    {

        //·µ»Ø¼ü
        user_toolbar = (Toolbar)findViewById(R.id.user_toolbar);
        user_toolbar.setTitle("");
        setSupportActionBar(user_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ls = (ListView)findViewById(R.id.user_list);
        addUser  = (Button)findViewById(R.id.add_user);
        addUser.setOnClickListener(this);
    }



    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        setResult(1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.add_user:
                Toast.makeText(this,"adduser",Toast.LENGTH_SHORT).show();
                break;
                default:
                    break;

        }
    }
}
