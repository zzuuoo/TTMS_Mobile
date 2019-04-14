package com.example.miaojie.ptest.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.LoadingDialog;
import com.example.miaojie.ptest.pojo.Grob_var;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emp_name;
    private EditText emp_no;
    private EditText emp_addr;
    private EditText emp_email;
    private EditText emp_pass;
    private ImageView head_path;
    private EditText emp_tel_num;
    private RadioGroup radioGroup;
    private RadioButton radioButtonManager;
    private RadioButton radioButtonSaler;
    private Button sure,cancle;
    private Handler handler;
    private LoadingDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        init();
        handler = new Handler(){
            @Override
            public void handleMessage(Message m)
            {
                switch (m.what)
                {
                    case 1:
                        dialog.close();
                        AddUserActivity.this.setResult(1);
                        Toast.makeText(AddUserActivity.this,"成功",Toast.LENGTH_SHORT).show();
                        AddUserActivity.this.finish();
                        break;
                    case 2:
                        dialog.close();
                        AddUserActivity.this.setResult(0);
                        Toast.makeText(AddUserActivity.this,"失败",Toast.LENGTH_SHORT).show();
                        AddUserActivity.this.finish();
                        break;

                }
            }

        };
    }
    public void init()
    {
        emp_name= findViewById(R.id.add_emp_name);
        emp_no = findViewById(R.id.add_emp_no);
        emp_addr= findViewById(R.id.add_emp_addr);
        emp_email= findViewById(R.id.add_emp_email);
        emp_pass = findViewById(R.id.add_emp_pass);
        head_path= findViewById(R.id.add_head_path);
        emp_tel_num = findViewById(R.id.add_emp_tel);
        radioGroup = findViewById(R.id.add_radiogroup_user);
        radioButtonManager = findViewById(R.id.add_userTypeManager);
        radioButtonSaler = findViewById(R.id.add_userTypeSaler);
        sure = findViewById(R.id.sure_addUser);
        cancle = findViewById(R.id.cancel_addUser);
        sure.setOnClickListener(this);
        cancle.setOnClickListener(this);

    }
    private void putUser() {
        Map<String,String> map = new HashMap<>();

        map.put("empNo",emp_no.getText().toString());
        map.put("empPass",emp_pass.getText().toString());
        if(radioButtonManager.isChecked()){
            map.put("type","1");
                }else {
            map.put("type","0");
                }
        JSONObject jsonObject = new JSONObject(map);

        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Grob_var.host+"mobileUser/insertUser")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what=2;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().equals("succeed"))
                {
                    Message message = new Message();
                    message.what=1;
                    handler.sendMessage(message);
                }else{
                    Message message = new Message();
                    message.what=2;
                    handler.sendMessage(message);
                }

            }


        });
    }
    private void putEmployee() {
        Map<String,String> map = new HashMap<>();
//        contentValues.put("emp_no",emp_no.getText().toString());
//        contentValues.put("emp_name",emp_name.getText().toString());
//        contentValues.put("emp_tel_num",emp_tel_num.getText().toString());
//        contentValues.put("emp_addr",emp_addr.getText().toString());
        map.put("empNo",emp_no.getText().toString());
        map.put("empName",emp_name.getText().toString());
        map.put("empTelNum",emp_tel_num.getText().toString());
        map.put("empAddr",emp_addr.getText().toString());
        map.put("empEmail",emp_email.getText().toString());

        JSONObject jsonObject = new JSONObject(map);

        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Grob_var.host+"mobileEmployee/insertEmployee")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what=2;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().equals("succeed"))
                {
                    putUser();

                }else{
                    Message message = new Message();
                    message.what=2;
                    handler.sendMessage(message);
                }

            }


        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.sure_addUser:
//                MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
//                SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
//                ContentValues contentValues = new ContentValues();
//                ContentValues contentValues1 = new ContentValues();
//                contentValues.put("emp_no",emp_no.getText().toString());
//                contentValues.put("emp_name",emp_name.getText().toString());
//                contentValues.put("emp_tel_num",emp_tel_num.getText().toString());
//                contentValues.put("emp_addr",emp_addr.getText().toString());
//                contentValues.put("emp_email",emp_email.getText().toString());
//                contentValues1.put("emp_no",emp_no.getText().toString());
//                contentValues1.put("emp_pass",emp_pass.getText().toString());
//                if(radioButtonManager.isChecked()){
//                    contentValues1.put("type",1);
//                }else {
//                    contentValues1.put("type",0);
//                }
//                contentValues1.put("head_path","我是头像路径");
//
//                long in = sqLiteDatabase.insert("employee",null,contentValues);
//                long in1 = sqLiteDatabase.insert("user",null,contentValues1);
//                if(in==-1||in1==-1)
//                {
//                       this.setResult(0);
//                }else{
//                       this.setResult(1);
//                   }
//                finish();
                dialog = new LoadingDialog(AddUserActivity.this,"请求中");
                dialog.show();
                putEmployee();
                break;
            case R.id.cancel_addUser:
                this.setResult(-1);
                finish();
                break;
        }
    }
}
