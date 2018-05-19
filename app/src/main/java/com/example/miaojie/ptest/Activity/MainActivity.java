package com.example.miaojie.ptest.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.miaojie.ptest.Adapter.VPadapter;
import com.example.miaojie.ptest.Fragment.BBSFragment;
import com.example.miaojie.ptest.Fragment.ManageFragment;
import com.example.miaojie.ptest.Fragment.MoiveListFragment;
import com.example.miaojie.ptest.Fragment.PersonalFragment;
import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.SeatTable;
import com.example.miaojie.ptest.pojo.OrderInfo;
import com.example.miaojie.ptest.pojo.UserInfo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static OrderInfo orderInfo=new OrderInfo();
    public static UserInfo userInfo;
    public static boolean isLogin=false;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RecyclerView recyclerView;
    private SeatTable seatTableView;
    private ArrayList<Fragment>fragmentArrayList;
    private ArrayList<Integer>piclist;
    private ArrayList<String> title;
    private BBSFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        tabLayout= (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        viewPager= (ViewPager) findViewById(R.id.viewpager);


        fragmentArrayList=new ArrayList<>();



        title=new ArrayList<>();
        title.add("系统管理");
        title.add("电影");
//        title.add("讨论版");
        title.add("个人信息");
        piclist=new ArrayList<>();
        piclist.add(R.mipmap.tab_movie_a);
        piclist.add(R.mipmap.cinema);
        piclist.add(R.mipmap.personal);



    }

    @Override
    protected void onStart() {
        super.onStart();
        userInfo= (UserInfo) getIntent().getSerializableExtra("userInfo");
        BBSFragment fragment=new BBSFragment();
        fragment.name="";
        if(userInfo!=null) {
            fragment.name = userInfo.getUserNickName();
            Log.e("NickName",userInfo.getUserNickName());
        }
        if(fragmentArrayList.size()<3)
        {
            fragmentArrayList.add(new ManageFragment());
            fragmentArrayList.add(new MoiveListFragment());
//            fragmentArrayList.add(new BBSFragment());
            fragmentArrayList.add(new PersonalFragment());
        }

        VPadapter vPadapter=new VPadapter(getSupportFragmentManager(),fragmentArrayList,title,piclist,this);
        viewPager.setAdapter(vPadapter);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(vPadapter.getTabView(i));
        }


        Log.e("onStart",(userInfo==null)+"--"+(getIntent().getSerializableExtra("userInfo")==null));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case 1:
                    Log.d("onactivityresult","反悔了");
                break;
        }
    }


}
