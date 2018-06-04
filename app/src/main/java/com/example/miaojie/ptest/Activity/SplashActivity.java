package com.example.miaojie.ptest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.MyDatabaseHelper;
import com.example.miaojie.ptest.pojo.User;

public class SplashActivity extends AppCompatActivity {

    private Boolean autoLogin=false;
    private Boolean wrong = false;
    User user = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ����û�б�����
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        RelativeLayout layoutSplash=(RelativeLayout) findViewById(R.id.activity_splash);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.1f,1.0f);
        alphaAnimation.setDuration(1000);//���ö�������ʱ��1000���루1�룩
        layoutSplash.startAnimation(alphaAnimation);
        //���ö�������
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                //��ñ�����SharedPredPreferences�е��û���������
                if(sp.getBoolean("antoLogin",false))
                {
                    String account = sp.getString("account", "");
                    String password = sp.getString("password", "");
                    //���û�������������������ʾ�û���������

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
                    if(user==null)
                    {
                        wrong=true;
                    }else{
                        autoLogin=true;
                    }
                }
            }
            //��������
            @Override
            public void onAnimationEnd(Animation animation) {
                //ҳ�����ת
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
                        Toast.makeText(SplashActivity.this,"��¼�ɹ�",Toast.LENGTH_SHORT).show();
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
}