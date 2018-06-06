package com.example.miaojie.ptest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.MyDatabaseHelper;
import com.example.miaojie.ptest.pojo.User;
import com.example.miaojie.ptest.pojo.UserWeb;
import com.google.gson.Gson;

public class SplashActivity extends AppCompatActivity {

    private Boolean autoLogin=false;
    private Boolean wrong = false;
    RequestQueue requestQueue;
    User user = null;
    Boolean start =true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置没有标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        RelativeLayout layoutSplash=(RelativeLayout) findViewById(R.id.activity_splash);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.1f,1.0f);
        alphaAnimation.setDuration(1000);//设置动画播放时长1000毫秒（1秒）
        layoutSplash.startAnimation(alphaAnimation);
        //设置动画监听
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                //获得保存在SharedPredPreferences中的用户名和密码
                if(sp.getBoolean("antoLogin",false))
                {
                    String account = sp.getString("account", "");
                    String password = sp.getString("password", "");
                    //在用户名和密码的输入框中显示用户名和密码

                    MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
                    SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getReadableDatabase();
                    Cursor cursor = sqLiteDatabase.query("user",null," emp_no = ? AND emp_pass = ?",new String[]{account,password},null,null,null);
                    if(cursor.moveToFirst()){
                        user = new User();
                        user.setEmp_no(cursor.getString(cursor.getColumnIndex("emp_no")));
                        user.setEmp_pass(cursor.getString(cursor.getColumnIndex("emp_pass")));
                        user.setType(cursor.getInt(cursor.getColumnIndex("type")));
                        user.setHead_path(cursor.getString(cursor.getColumnIndex("head_path")));
                    }
                    cursor.close();
//                    get("http://128.0.0.241:8080/mobileUser/login?name="+account+"&"+"password="+password);

                    if(user==null)
                    {
                        wrong=true;
                    }else{
                        autoLogin=true;
                    }
                }
            }
            //动画结束
            @Override
            public void onAnimationEnd(Animation animation) {
                //页面的跳转
                if(autoLogin)
                {
                    if(wrong)
                    {
                        Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        MainActivity.isLogin=true;
                        MainActivity.user = user;
                        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
//                        intent.putExtra("user",user);
                        Toast.makeText(SplashActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        SplashActivity.this.finish();
                    }
                }else{
                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    /**
     * get
     */
    public void get(String url){
        //创建一个请求队列
        requestQueue = Volley.newRequestQueue(this);
        //创建一个请求
//        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
//        String url1 = "http://128.0.0.241:8080/mobileUser/login?name=000001&password=123456";

        StringRequest stringRequest =new StringRequest(url, new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {

                //                    JSONObject jsonObject = new JSONObject(s);
                Log.e("ooooooooooopopop",s);
                UserWeb userweb = new Gson().fromJson(s, UserWeb.class);
                if(userweb!=null)
                {
                    user=new User();
                    user.setHead_path(userweb.getHeadPath());
                    user.setType(userweb.getType());
                    user.setEmp_pass(userweb.getEmpPass());
                    user.setEmp_no(userweb.getEmpNo());
                }
                start=false;
//                Log.e("LoginUser",user.getEmp_no());

            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("LoginUser","加载错误"+volleyError);
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
    }

}
