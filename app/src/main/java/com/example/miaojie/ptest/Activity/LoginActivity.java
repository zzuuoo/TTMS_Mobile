package com.example.miaojie.ptest.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.example.miaojie.ptest.R;
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
        readAccount();

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        Toast.makeText(LoginActivity.this,"�û������������",Toast.LENGTH_SHORT).show();
                        passWord.setText("");
                        break;
                    case 2:
                        if(remeber.isChecked()){
                            //����sharedPreference����info��ʾ�ļ�����MODE_PRIVATE��ʾ����Ȩ��Ϊ˽�е�
                            SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                            //���sp�ı༭��
                            SharedPreferences.Editor ed = sp.edit();
                            //�Լ�ֵ�Ե���ʾ���û��������뱣�浽sp��
                            ed.putString("account", userName.getText().toString());
                            ed.putString("password", passWord.getText().toString());
                            //�ύ�û���������
                            ed.commit();
                        }else{
                            SharedPreferences sp = getSharedPreferences(userName.getText().toString(), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit(); editor.clear(); editor.commit();
                        }
                        MainActivity.isLogin=true;
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra("user",(User)msg.obj);
                        Log.e("��¼",(((User) msg.obj).getEmp_no()));
                        Toast.makeText(LoginActivity.this,"��¼�ɹ�",Toast.LENGTH_SHORT).show();
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
                        Cursor cursor = sqLiteDatabase.query("user",null," emp_no = ?",new String[]{userName.getText().toString()},null,null,null);
                        if(cursor.moveToFirst()){
                            user = new User();
                            user.setEmp_no(cursor.getString(cursor.getColumnIndex("emp_no")));
                            user.setEmp_pass(cursor.getString(cursor.getColumnIndex("emp_pass")));
                            user.setType(cursor.getInt(cursor.getColumnIndex("type")));
                            user.setHead_path(cursor.getString(cursor.getColumnIndex("head_path")));
                        }
                        if(user==null)
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
                Toast.makeText(getApplicationContext(),"�����",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //��ȡ�����ڱ��ص��û���������
    public void readAccount() {

        //����SharedPreferences����
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        //��ñ�����SharedPredPreferences�е��û���������
        String account = sp.getString("account", "");
        String password = sp.getString("password", "");
        //���û�������������������ʾ�û���������
        userName.setText(account);
        passWord.setText(password);
        if(account!=null&&!account.equals("")){
            remeber.setChecked(true);
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
}
