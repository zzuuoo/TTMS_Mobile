
package com.example.miaojie.ptest.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.miaojie.ptest.Adapter.EmployeeAdapter;
import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.LoadingDialog;
import com.example.miaojie.ptest.Utils.MyDatabaseHelper;
import com.example.miaojie.ptest.pojo.Employee;
import com.example.miaojie.ptest.pojo.EmployeeWeb;
import com.example.miaojie.ptest.pojo.Grob_var;
import com.example.miaojie.ptest.pojo.User;
import com.example.miaojie.ptest.pojo.UserWeb;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class  UserManageActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Employee> empList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
//    private List<Employee> empSearchList = new ArrayList<>();
    private List<User> userSearchList = new ArrayList<>();
    private ListView ls = null;
    private Button addUser;
    private Toolbar user_toolbar;
    private boolean isSearch =false;
    private SearchView user_searchview;
    private EmployeeAdapter employeeAdapter;
    private RequestQueue requestQueue;
    Handler handler;
    LoadingDialog dialog;
    int c = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage);
        setData();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        c++;
                        if(c>1)
                        {
                            dialog.close();
                            init();
                        }
                        break;

                }
            }
        };
//                init();

    }
    public void init()
    {

        //返回键
        user_toolbar = findViewById(R.id.user_toolbar);
        user_toolbar.setTitle("");
        setSupportActionBar(user_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ls = findViewById(R.id.user_list);
        addUser  = findViewById(R.id.add_user);
        addUser.setOnClickListener(this);
        employeeAdapter = new EmployeeAdapter(this,R.layout.employee_item,userList);
        ls.setAdapter(employeeAdapter);
        ls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Employee e;
//                if(isSearch){
//                    e = empSearchList.get(i);
//                }else{
//                    e = empList.get(i);
//                }
                User u;
                if(isSearch){
                    u=userSearchList.get(i);
                }else {
                    u=userList.get(i);
                }
                AlertDialog.Builder dialog = new AlertDialog.Builder(UserManageActivity.this);
                dialog.setTitle("警告");
                dialog.setMessage("确认删除吗？");
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteDatabase sqLiteDatabase = MyDatabaseHelper.getInstance().getWritableDatabase();
                        sqLiteDatabase.delete("user","emp_no  =  ?",new String[]{u.getEmp_no()});
                        sqLiteDatabase.delete("employee","emp_no  =  ?",new String[]{u.getEmp_no()});
                        Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                        setData();
                        employeeAdapter = new EmployeeAdapter(getApplication(),R.layout.employee_item,userList);
                        ls.setAdapter(employeeAdapter);
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
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Employee e=null;
                User u = null;
                if(isSearch){
                    u=userSearchList.get(i);
                }else{
                    u=userList.get(i);
                }
                for(Employee employee:empList){
                    if(employee.getEmp_no().equals(u.getEmp_no())){
                        e=employee;
                    }
                }
                Intent intent = new Intent(UserManageActivity.this,EditUserActivity.class);
                intent.putExtra("user",u);
                intent.putExtra("employee",e);
                startActivityForResult(intent,2);
//                Toast.makeText(getApplicationContext(),empList.get(i).getEmp_name(),Toast.LENGTH_SHORT).show();
            }
        });


        user_searchview = findViewById(R.id.userSearchView);

        user_searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userSearchList.clear();
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getEmp_no().contains(newText)) {
                        userSearchList.add(userList.get(i));
                    }
                }
                if (userSearchList.size() > 0 && newText.length() > 0) {
                    employeeAdapter = new EmployeeAdapter(getApplicationContext(),R.layout.employee_item,userSearchList);
                    ls.setAdapter(employeeAdapter);
                    isSearch = true;

                }
                else if (newText.length() == 0) {
                    employeeAdapter = new EmployeeAdapter(getApplication(),R.layout.employee_item,userList);
                    ls.setAdapter(employeeAdapter);
                    isSearch = false;
                }
                return false;
            }
        });
    }

    public void setData()
    {

        empList.clear();
        userList.clear();
//        SQLiteDatabase sqLiteDatabase = MyDatabaseHelper.getInstance().getWritableDatabase();
//        Cursor cursor = sqLiteDatabase.query("employee", null, null, null, null, null, null);
//        if (cursor.moveToFirst()) {
//
//            do {
//                Employee employee = new Employee();
//                employee.setEmp_id(cursor.getInt(cursor.getColumnIndex("emp_id")));
//                employee.setEmp_no(cursor.getString(cursor.getColumnIndex("emp_no")));
//                employee.setEmp_name(cursor.getString(cursor.getColumnIndex("emp_name")));
//                employee.setEmp_email(cursor.getString(cursor.getColumnIndex("emp_email")));
//                employee.setEmp_addr(cursor.getString(cursor.getColumnIndex("emp_addr")));
//                employee.setEmp_tel_num(cursor.getString(cursor.getColumnIndex("emp_tel_num")));
//                empList.add(employee);
//            } while (cursor.moveToNext());
//
//        }
//        cursor = sqLiteDatabase.query("user", null, null, null, null, null, null);
//        if (cursor.moveToFirst()) {
//
//            do {
//                User user = new User();
//                user.setEmp_no(cursor.getString(cursor.getColumnIndex("emp_no")));
//                user.setEmp_pass(cursor.getString(cursor.getColumnIndex("emp_pass")));
//                user.setType(cursor.getInt(cursor.getColumnIndex("type")));
//                user.setHead_path(cursor.getString(cursor.getColumnIndex("head_path")));
//                userList.add(user);
//            } while (cursor.moveToNext());
//
//        }
//        cursor.close();
        c=0;
        dialog = new LoadingDialog(UserManageActivity.this,"数据请求中...");
        dialog.show();
        getEmployee();
        getUser();

    }

    public void getUser(){
        //创建一个请求队列
        requestQueue = Volley.newRequestQueue(this);
        //创建一个请求
//        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
//        String url1 = "http://128.0.0.241:8080/mobileUser/login?name=000001&password=123456";

        StringRequest stringRequest =new StringRequest(Grob_var.host+"mobileUser/getUser", new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {

                //                    JSONObject jsonObject = new JSONObject(s);
//                Log.e("ooooooooooopopop",s);
                List<UserWeb> userwebs = new Gson().fromJson(s, new TypeToken<List<UserWeb>>() {}.getType());
                for(int i=0;i<userwebs.size();i++)
                {
                    User user=new User();
                    user.setHead_path(userwebs.get(i).getHeadPath());
                    user.setType(userwebs.get(i).getType());
                    user.setEmp_pass(userwebs.get(i).getEmpPass());
                    user.setEmp_no(userwebs.get(i).getEmpNo());
                    userList.add(user);
                }
                Message message = new Message();
                message.what =1;
                handler.sendMessage(message);

            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("LoginUser","加载错误"+volleyError);
                Message message = new Message();
                message.what =1;
                handler.sendMessage(message);
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
    }
    public void getEmployee(){
        //创建一个请求队列
        requestQueue = Volley.newRequestQueue(this);
        //创建一个请求
//        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
//        String url1 = "http://128.0.0.241:8080/mobileUser/login?name=000001&password=123456";

        StringRequest stringRequest =new StringRequest(Grob_var.host+"mobileEmployee/getEmployee", new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String s) {

                //                    JSONObject jsonObject = new JSONObject(s);
//                Log.e("ooooooooooopopop",s);
                List<EmployeeWeb> employeeWebs = new Gson().fromJson(s, new TypeToken<List<EmployeeWeb>>() {}.getType());
                for(int i=0;i<employeeWebs.size();i++)
                {
                    Employee employee=new Employee();
                    employee.setEmp_addr(employeeWebs.get(i).getEmpAddr());
                    employee.setEmp_email(employeeWebs.get(i).getEmpEmail());
                    employee.setEmp_id(employeeWebs.get(i).getEmpId());
                    employee.setEmp_no(employeeWebs.get(i).getEmpNo());
                    employee.setEmp_tel_num(employeeWebs.get(i).getEmpTelNum());
                    employee.setEmp_name(employeeWebs.get(i).getEmpName());
                    empList.add(employee);
                }
                Message message = new Message();
                message.what =1;
                handler.sendMessage(message);

            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("LoginUser","加载错误"+volleyError);
                Message message = new Message();
                message.what =1;
                handler.sendMessage(message);
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
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
                Intent i = new Intent(UserManageActivity.this,AddUserActivity.class);
                startActivityForResult(i,1);
                break;
                default:
                    break;

        }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        switch (requestCode)
        {
            case 1://add
                if(resultCode==1)
                {
                    Toast.makeText(getApplication(),"插入成功",Toast.LENGTH_SHORT).show();
                    setData();
//                    employeeAdapter = new EmployeeAdapter(this,R.layout.employee_item,userList);
//                    ls.setAdapter(employeeAdapter);

                }else if (resultCode==0){
                    Toast.makeText(getApplication(),"添加失败",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplication(),"取消",Toast.LENGTH_SHORT).show();
                }
                break;
            case 2://edit
                if(resultCode==1)
                {
                    Toast.makeText(getApplication(),"修改成功",Toast.LENGTH_SHORT).show();
                    setData();
//                    employeeAdapter = new EmployeeAdapter(this,R.layout.employee_item,userList);
//                    ls.setAdapter(employeeAdapter);
                }else if (resultCode==0){
                    Toast.makeText(getApplication(),"修改失败",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplication(),"取消",Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                    break;
        }
    }
}
