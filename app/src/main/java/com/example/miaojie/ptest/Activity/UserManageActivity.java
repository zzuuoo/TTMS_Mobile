
package com.example.miaojie.ptest.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.example.miaojie.ptest.Adapter.EmployeeAdapter;
import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.MyDatabaseHelper;
import com.example.miaojie.ptest.pojo.Employee;
import com.example.miaojie.ptest.pojo.User;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage);
        init();

    }
    public void init()
    {

        //返回键
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
        setData();
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


        user_searchview = (SearchView) findViewById(R.id.userSearchView);

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
        SQLiteDatabase sqLiteDatabase = MyDatabaseHelper.getInstance().getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("employee", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {

            do {
                Employee employee = new Employee();
                employee.setEmp_id(cursor.getInt(cursor.getColumnIndex("emp_id")));
                employee.setEmp_no(cursor.getString(cursor.getColumnIndex("emp_no")));
                employee.setEmp_name(cursor.getString(cursor.getColumnIndex("emp_name")));
                employee.setEmp_email(cursor.getString(cursor.getColumnIndex("emp_email")));
                employee.setEmp_addr(cursor.getString(cursor.getColumnIndex("emp_addr")));
                employee.setEmp_tel_num(cursor.getString(cursor.getColumnIndex("emp_tel_num")));
                empList.add(employee);
            } while (cursor.moveToNext());

        }
        cursor = sqLiteDatabase.query("user", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {

            do {
                User user = new User();
                user.setEmp_no(cursor.getString(cursor.getColumnIndex("emp_no")));
                user.setEmp_pass(cursor.getString(cursor.getColumnIndex("emp_pass")));
                user.setType(cursor.getInt(cursor.getColumnIndex("type")));
                user.setHead_path(cursor.getString(cursor.getColumnIndex("head_path")));
                userList.add(user);
            } while (cursor.moveToNext());

        }
        cursor.close();

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
                    employeeAdapter = new EmployeeAdapter(this,R.layout.employee_item,userList);
                    ls.setAdapter(employeeAdapter);

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
                    employeeAdapter = new EmployeeAdapter(this,R.layout.employee_item,userList);
                    ls.setAdapter(employeeAdapter);
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
