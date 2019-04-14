package com.example.miaojie.ptest.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

/**
 * Created by miaojie on 2017/5/22.
 */

public class LoginActivity extends Activity {
    private EditText userName;
    private EditText passWord;
    private Button loginButton;
    private Button registerButton;
    private CheckBox remeber;
    private CheckBox antoLogin;
    private User user;
    private Employee employee;
    int c =0;
    private RequestQueue requestQueue;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        userName= findViewById(R.id.login_userName);
        passWord= findViewById(R.id.login_passWord);
        loginButton= findViewById(R.id.login_login);
        registerButton= findViewById(R.id.login_register);
        remeber = findViewById(R.id.remeber_password);
        antoLogin = findViewById(R.id.auto_login);
        readAccount();

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        Toast.makeText(LoginActivity.this,"���������쳣",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        c++;
                        if(c>1)
                        {
                            if(remeber.isChecked()){
                                //����sharedPreference����info��ʾ�ļ�����MODE_PRIVATE��ʾ����Ȩ��Ϊ˽�е�
                                SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                                //���sp�ı༭��
                                SharedPreferences.Editor ed = sp.edit();
                                //�Լ�ֵ�Ե���ʾ���û��������뱣�浽sp��
                                ed.putString("account", userName.getText().toString());
                                ed.putString("password", passWord.getText().toString());
                                ed.putBoolean("remeber",true);
//                            Toast.makeText(getApplication(),"��������ɹ�",Toast.LENGTH_SHORT).show();
                                //�ύ�û���������
                                if(antoLogin.isChecked())
                                {
                                    ed.putBoolean("antoLogin",true);
                                }else{
                                    ed.putBoolean("antoLogin",false);
                                }
                                ed.commit();
                            }else{
                                SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.remove("account");
                                editor.remove("password");
                                editor.putBoolean("remeber",false);
                                editor.putBoolean("antoLogin",false);

                                editor.commit();
//                            Toast.makeText(getApplication(),"ȡ������ɹ�",Toast.LENGTH_SHORT).show();
                            }
                            MainActivity.isLogin=true;
//                          MainActivity.user = (User)msg.obj;
                            MainActivity.user=user;
                            MainActivity.employee=employee;
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//                        intent.putExtra("user",(User)msg.obj);
//                        Log.e("��¼",(((User) msg.obj).getEmp_no()));
                            Toast.makeText(LoginActivity.this,"��¼�ɹ�",Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }

                        break;
                    case 3:
                        Toast.makeText(LoginActivity.this,"�û������������",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                new Thread(){
//                    @Override
//                    public void run() {
//                        super.run();
//                        Message message=new Message();
//                        User user = null;
//                        MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
//                        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getReadableDatabase();
//                        Cursor cursor = sqLiteDatabase.query("user",null," emp_no = ? AND emp_pass = ?",new String[]{userName.getText().toString(),passWord.getText().toString()},null,null,null);
//                        if(cursor.moveToFirst()){
//                            user = new User();
//                            user.setEmp_no(cursor.getString(cursor.getColumnIndex("emp_no")));
//                            user.setEmp_pass(cursor.getString(cursor.getColumnIndex("emp_pass")));
//                            user.setType(cursor.getInt(cursor.getColumnIndex("type")));
//                            user.setHead_path(cursor.getString(cursor.getColumnIndex("head_path")));
//                        }
//                        cursor.close();
//                        if(user==null )
//                        {
//                            message.what=1;
//                            handler.sendMessage(message);
//                            return;
//                        }
//
//                        message.what=2;
//                        message.obj=user;
//                        handler.sendMessage(message);
//                    }
//                }.start();
                login(userName.getText().toString(),passWord.getText().toString());

            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
//                startActivity(intent);
                Toast.makeText(getApplicationContext(),"�����",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //��ȡ�����ڱ��ص��û���������
    public void readAccount() {

        //����SharedPreferences����
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        //��ñ�����SharedPredPreferences�е��û���������
        if(sp.getBoolean("remeber",false))
        {
            String account = sp.getString("account", "");
            String password = sp.getString("password", "");
            //���û�������������������ʾ�û���������
            userName.setText(account);
            passWord.setText(password);
            remeber.setChecked(true);
            if(sp.getBoolean("antoLogin",false))
            {
                antoLogin.setChecked(true);
            }
        }
       else{
            remeber.setChecked(false);
            antoLogin.setChecked(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK==keyCode)
        {
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);

    }

    public void login(String name,String password){
        //����һ���������
        requestQueue = Volley.newRequestQueue(this);
        //����һ������
//        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
//        String url1 = "http://128.0.0.241:8080/mobileUser/login?name=000001&password=123456";

        StringRequest stringRequest =new StringRequest(Grob_var.host+"mobileUser/login?name="+name+"&password="+password, new Response.Listener<String>() {
            //��ȷ�������ݻص�
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
                Toast.makeText(LoginActivity.this,s,Toast.LENGTH_SHORT).show();
                if(s.equals("succeed"))
                {
                    c=0;
                    getUser(name);
                    getEmployee(name);
                }else{
                    Message message = new Message();
                    message.what=3;
                    handler.sendMessage(message);
                }

            }
        }, new Response.ErrorListener() {//�쳣��ļ�������
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("LoginUser","���ش���"+volleyError);
                Message message = new Message();
                message.what=1;
                handler.sendMessage(message);
            }
        });
        //��get������ӵ�������
        requestQueue.add(stringRequest);
    }
    public void getUser(String name){
        //����һ���������
        requestQueue = Volley.newRequestQueue(this);
        //����һ������
//        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
//        String url1 = "http://128.0.0.241:8080/mobileUser/login?name=000001&password=123456";

        StringRequest stringRequest =new StringRequest(Grob_var.host+"mobileUser/getUserById?emp_no="+name, new Response.Listener<String>() {
            //��ȷ�������ݻص�
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
                message.what=2;
                handler.sendMessage(message);

            }
        }, new Response.ErrorListener() {//�쳣��ļ�������
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("LoginUser","���ش���"+volleyError);
            }
        });
        //��get������ӵ�������
        requestQueue.add(stringRequest);
    }

    public void getEmployee(String empNo){
        //����һ���������
        requestQueue = Volley.newRequestQueue(this);
        //����һ������
//        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
//        String url1 = "http://128.0.0.241:8080/mobileUser/login?name=000001&password=123456";

        StringRequest stringRequest =new StringRequest(Grob_var.host+"mobileEmployee/selectByEmpNo?empNo="+empNo, new Response.Listener<String>() {
            //��ȷ�������ݻص�
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
                message.what=2;
                handler.sendMessage(message);

            }
        }, new Response.ErrorListener() {//�쳣��ļ�������
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("LoginUser","���ش���"+volleyError);
            }
        });
        //��get������ӵ�������
        requestQueue.add(stringRequest);
    }


}
