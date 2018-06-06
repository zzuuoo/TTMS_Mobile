package com.example.miaojie.ptest.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.HttpAccess;
import com.example.miaojie.ptest.Utils.MyDatabaseHelper;
import com.example.miaojie.ptest.pojo.User;

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
//    private ArrayList<UserInfo>userInfos;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        userName= (EditText) findViewById(R.id.login_userName);
        passWord= (EditText) findViewById(R.id.login_passWord);
        loginButton= (Button) findViewById(R.id.login_login);
        registerButton= (Button) findViewById(R.id.login_register);
        remeber = (CheckBox)findViewById(R.id.remeber_password);
        antoLogin = (CheckBox)findViewById(R.id.auto_login);
        readAccount();

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                        passWord.setText("");
                        break;
                    case 2:
                        if(remeber.isChecked()){
                            //创建sharedPreference对象，info表示文件名，MODE_PRIVATE表示访问权限为私有的
                            SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                            //获得sp的编辑器
                            SharedPreferences.Editor ed = sp.edit();
                            //以键值对的显示将用户名和密码保存到sp中
                            ed.putString("account", userName.getText().toString());
                            ed.putString("password", passWord.getText().toString());
                            ed.putBoolean("remeber",true);
//                            Toast.makeText(getApplication(),"保存密码成功",Toast.LENGTH_SHORT).show();
                            //提交用户名和密码
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
//                            Toast.makeText(getApplication(),"取消密码成功",Toast.LENGTH_SHORT).show();
                         }
                        MainActivity.isLogin=true;
                        MainActivity.user = (User)msg.obj;
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//                        intent.putExtra("user",(User)msg.obj);
//                        Log.e("登录",(((User) msg.obj).getEmp_no()));
                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        LoginActivity.this.finish();
                        break;
                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Message message=new Message();
                        User user = null;
                        MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
                        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getReadableDatabase();
                        Cursor cursor = sqLiteDatabase.query("user",null," emp_no = ? AND emp_pass = ?",new String[]{userName.getText().toString(),passWord.getText().toString()},null,null,null);
                        if(cursor.moveToFirst()){
                            user = new User();
                            user.setEmp_no(cursor.getString(cursor.getColumnIndex("emp_no")));
                            user.setEmp_pass(cursor.getString(cursor.getColumnIndex("emp_pass")));
                            user.setType(cursor.getInt(cursor.getColumnIndex("type")));
                            user.setHead_path(cursor.getString(cursor.getColumnIndex("head_path")));
                        }
                        cursor.close();
                        if(user==null )
                        {
                            message.what=1;
                            handler.sendMessage(message);
                            return;
                        }

                        message.what=2;
                        message.obj=user;
                        handler.sendMessage(message);
                    }
                }.start();

            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
//                startActivity(intent);
                Toast.makeText(getApplicationContext(),"待完成",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //读取保存在本地的用户名和密码
    public void readAccount() {

        //创建SharedPreferences对象
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        //获得保存在SharedPredPreferences中的用户名和密码
        if(sp.getBoolean("remeber",false))
        {
            String account = sp.getString("account", "");
            String password = sp.getString("password", "");
            //在用户名和密码的输入框中显示用户名和密码
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
    class MyAsyncTask extends AsyncTask<String, Integer, String>
    {
        // 在后台线程访问网络
        public String doInBackground(String... params)
        {
            if(HttpAccess.checkUser(userName.getText().toString(), passWord.getText().toString()).equals("1"))
            {
// 1.选中记住密码，保存用户名和密码到首选项中
                if(remeber.isChecked()){
                    //创建sharedPreference对象，info表示文件名，MODE_PRIVATE表示访问权限为私有的
                    SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                    //获得sp的编辑器
                    SharedPreferences.Editor ed = sp.edit();
                    //以键值对的显示将用户名和密码保存到sp中
                    ed.putString("account", userName.getText().toString());
                    ed.putString("password", passWord.getText().toString());
                    ed.putBoolean("remeber",true);
//                            Toast.makeText(getApplication(),"保存密码成功",Toast.LENGTH_SHORT).show();
                    //提交用户名和密码
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
//                            Toast.makeText(getApplication(),"取消密码成功",Toast.LENGTH_SHORT).show();
                }
                MainActivity.isLogin=true;
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//                MainActivity.user = ....从服务器获取user对象
// intent.putExtra("user",(User)msg.obj);
//                Log.e("登录",(((User) msg.obj).getEmp_no()));
                Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                startActivity(intent);
                LoginActivity.this.finish();
// 2.跳转到下一个页面
        // 如果需要更新UI组件，调用如下方法到onProgressUpdate
        // publishProgress(1);
            }
            return "";
        }
        // UI线程调用
        public void onProgressUpdate(Integer... values)
        {
// 如果需要更新UI组件，代码写在次处

        }
    }

}
