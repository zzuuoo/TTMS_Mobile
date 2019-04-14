package com.example.miaojie.ptest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.miaojie.ptest.pojo.Employee;
import com.example.miaojie.ptest.pojo.EmployeeWeb;
import com.example.miaojie.ptest.pojo.Grob_var;
import com.example.miaojie.ptest.pojo.User;
import com.example.miaojie.ptest.pojo.UserWeb;
import com.google.gson.Gson;

public class SplashActivity extends AppCompatActivity {

    private Boolean autoLogin=false;
    private Boolean wrong = false;
    RequestQueue requestQueue;
    User user = null;
    Employee employee = null;
    Boolean start =true;
    Handler handler;
    int c=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置没有标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        RelativeLayout layoutSplash= findViewById(R.id.activity_splash);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.1f,1.0f);
        alphaAnimation.setDuration(2000);//设置动画播放时长1000毫秒（1秒）
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
//                    MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
//                    SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getReadableDatabase();
//                    Cursor cursor = sqLiteDatabase.query("user",null," emp_no = ? AND emp_pass = ?",new String[]{account,password},null,null,null);
//                    if(cursor.moveToFirst()){
//                        user = new User();
//                        user.setEmp_no(cursor.getString(cursor.getColumnIndex("emp_no")));
//                        user.setEmp_pass(cursor.getString(cursor.getColumnIndex("emp_pass")));
//                        user.setType(cursor.getInt(cursor.getColumnIndex("type")));
//                        user.setHead_path(cursor.getString(cursor.getColumnIndex("head_path")));
//                    }
//                    cursor.close();
                    login(account,password);
//                    if(user==null)
//                    {
//                        wrong=true;
//                    }else{
//                        autoLogin=true;
//                    }
                }else{
                    wrong=true;
                }

            }
            //动画结束
            @Override
            public void onAnimationEnd(Animation animation) {

                if(wrong)
                {
                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

//                页面的跳转
//                if(autoLogin)
//                {
//                    if(wrong)
//                    {
//                        Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }else{
//                        MainActivity.isLogin=true;
//                        MainActivity.user = user;
//                        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
////                        intent.putExtra("user",user);
//                        Toast.makeText(SplashActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
//                        startActivity(intent);
//                        SplashActivity.this.finish();
//                    }
//                }else{
//                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
//                    startActivity(intent);
//                    finish();
//                }


            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what)
                {
                    case 1:
                        c++;
                        if(c>1)
                        {
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
                                    MainActivity.employee =employee;
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
                            finish();
                        }

                        break;
                    case 2:
                        Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }

            }
        };

    }
    /**
     * get
     */
    public void login(String name,String password){
        //创建一个请求队列
        requestQueue = Volley.newRequestQueue(this);
        //创建一个请求
//        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
//        String url1 = "http://128.0.0.241:8080/mobileUser/login?name=000001&password=123456";

        StringRequest stringRequest =new StringRequest(Grob_var.host+"mobileUser/login?name="+name+"&password="+password, new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {

                //                    JSONObject jsonObject = new JSONObject(s);
//                Log.e("ooooooooooopopop",s);
//                UserWeb userweb = new Gson().fromJson(s, UserWeb.class);
//                if(userweb!=null)
//                {
//                    user=new User();
//                    user.setHead_path(userweb.getHeadPath());
//                    user.setType(userweb.getType());
//                    user.setEmp_pass(userweb.getEmpPass());
//                    user.setEmp_no(userweb.getEmpNo());
//                }
                if(s.equals("succeed"))
                {
                    c=0;
                    getUser(name);
                    getEmployee(name);
                    autoLogin=true;
                    wrong=false;
                }
                else{
                    wrong=true;
                    autoLogin=false;
                    Message message = new Message();
                    message.what=2;
                    handler.sendMessage(message);


                }

            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                wrong=true;
                autoLogin=false;
                Log.e("LoginUser","加载错误"+volleyError);
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
    }
    public void getUser(String name){
        //创建一个请求队列
        requestQueue = Volley.newRequestQueue(this);
        //创建一个请求
//        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
//        String url1 = "http://128.0.0.241:8080/mobileUser/login?name=000001&password=123456";

        StringRequest stringRequest =new StringRequest(Grob_var.host+"mobileUser/getUserById?emp_no="+name, new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {

                //                    JSONObject jsonObject = new JSONObject(s);
//                Log.e("ooooooooooopopop",s);
                UserWeb userweb = new Gson().fromJson(s, UserWeb.class);
                if(userweb!=null)
                {
                    user=new User();
                    user.setHead_path(userweb.getHeadPath());
                    user.setType(userweb.getType());
                    user.setEmp_pass(userweb.getEmpPass());
                    user.setEmp_no(userweb.getEmpNo());
                }
                Message message = new Message();
                message.what=1;
                handler.sendMessage(message);
                wrong=false;

            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("LoginUser","加载错误"+volleyError);
                wrong=true;
                autoLogin=false;
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
    }

    public void getEmployee(String empNo){
        //创建一个请求队列
        requestQueue = Volley.newRequestQueue(this);
        //创建一个请求
//        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
//        String url1 = "http://128.0.0.241:8080/mobileUser/login?name=000001&password=123456";

        StringRequest stringRequest =new StringRequest(Grob_var.host+"mobileEmployee/selectByEmpNo?empNo="+empNo, new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {

                //                    JSONObject jsonObject = new JSONObject(s);
//                Log.e("ooooooooooopopop",s);
                EmployeeWeb employeeWeb = new Gson().fromJson(s, EmployeeWeb.class);
                if(employeeWeb!=null)
                {
                    employee=new Employee();
                    employee.setEmp_addr(employeeWeb.getEmpAddr());
                    employee.setEmp_email(employeeWeb.getEmpEmail());
                    employee.setEmp_id(employeeWeb.getEmpId());
                    employee.setEmp_no(employeeWeb.getEmpNo());
                    employee.setEmp_tel_num(employeeWeb.getEmpTelNum());
                    employee.setEmp_name(employeeWeb.getEmpName());
                }
                Message message = new Message();
                message.what=1;
                handler.sendMessage(message);
                wrong=false;

            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("LoginUser","加载错误"+volleyError);
                wrong=true;
                autoLogin=false;
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
    }

}
