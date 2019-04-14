package com.example.miaojie.ptest.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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

public class AddStudioActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView studio_name;
    private TextView studio_row;
    private TextView studio_col;
    private RadioGroup radioGroup;
    private RadioButton radioButton0;
    private RadioButton radioButton1;
    private TextView studio_intro;
    private Button sure;
    private Button cancle;
    public Handler handler;
    LoadingDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_studio);
        init();
    }
    public void init()
    {
        studio_name = findViewById(R.id.studio_name_add);
        studio_col = findViewById(R.id.studio_col_add);
        studio_row = findViewById(R.id.studio_row_add);
        radioGroup = findViewById(R.id.add_radiogroup_studio);
        radioButton0= findViewById(R.id.add_status_studio_0);
        radioButton1= findViewById(R.id.add_status_studio_1);
        studio_intro = findViewById(R.id.studio_introduction_add);
        sure= findViewById(R.id.sure_addstudio);
        cancle= findViewById(R.id.cancel_addstudio);
        sure.setOnClickListener(this);
        cancle.setOnClickListener(this);
        dialog = new LoadingDialog(this,"添加中 ...");
        radioButton1.setClickable(false);
        radioButton0.setClickable(false);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        dialog.close();
                        Toast.makeText(AddStudioActivity.this,"生成座位成功",Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case 2:
                        dialog.close();
                        Toast.makeText(AddStudioActivity.this,"生成座位失败",Toast.LENGTH_SHORT).show();
                        finish();

                }
            }
        };

    }

    private void postDataWithParame() {
        Map<String,String> map = new HashMap<>();
        map.put("studioName",studio_name.getText().toString());//传入参数
        map.put("studioRowCount",studio_row.getText().toString());//传入参数
        map.put("studioColCount",studio_col.getText().toString());//传入参数
        map.put("studioIntroduction",studio_intro.getText().toString());//传入参数
        map.put("studioFlag","0");//传入参数

        JSONObject jsonObject = new JSONObject(map);

        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Grob_var.host+"mobileStudio/insertStudio")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                AddStudioActivity.this.setResult(0);
                finish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();
                message.what = 1;
                if(response.body().string().equals("succeed"))
                {
                    AddStudioActivity.this.setResult(1);
                }else{
                    AddStudioActivity.this.setResult(0);
                    message.what = 2;
                }
                handler.sendMessage(message);
            }


        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.sure_addstudio:
                if(Integer.valueOf(studio_row.getText().toString())>50||Integer
                        .valueOf(studio_col.getText().toString())>50)
                {
                    Toast.makeText(this,"行列数不得超过50",Toast.LENGTH_SHORT).show();
                    break;
                }
//                MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
//                SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
//                ContentValues contentValues = new ContentValues();
//                contentValues.put("studio_name",studio_name.getText().toString());
//                contentValues.put("studio_row_count",studio_row.getText().toString());
//                contentValues.put("studio_col_count",studio_col.getText().toString());
//                contentValues.put("studio_introduction",studio_intro.getText().toString());
//                contentValues.put("studio_flag",0);
//                long in = sqLiteDatabase.insert("studio",null,contentValues);
//                if(in==-1)
//                {
//                    this.setResult(0);
//                }else{
//                    this.setResult(1);
//                }
//                Cursor cursor = sqLiteDatabase.rawQuery("select last_insert_rowid() from studio",null);
//                int studio_id =0;
//                if(cursor.moveToFirst()){
//                    studio_id=cursor.getInt(0);
//                }
//                cursor.close();
                /**
                 * 重新生成座位
                 * 数据量大会导致应用卡死，开线程执行
                 */
                dialog.show();
                postDataWithParame();
//                int finalStudio_id = studio_id;
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
//                        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
//                        ContentValues contentValues = new ContentValues();
//                        for(int i = 0;i<Integer.valueOf(studio_row.getText().toString());i++)
//                        {
//                            for(int j = 0;j<  Integer.valueOf(studio_col.getText().toString());j++)
//                            {
//                                contentValues.clear();
//                                contentValues.put("studio_id", finalStudio_id);
//                                contentValues.put("seat_row",i+1);
//                                contentValues.put("seat_column",j+1);
//                                contentValues.put("seat_status",1);
//                                sqLiteDatabase.insert("seat",null,contentValues);
//                            }
//                        }
//                        Message message = new Message();
//                        message.what = 1;
//                        handler.sendMessage(message);
//                    }
//                }).start();

                break;
            case R.id.cancel_addstudio:
                this.setResult(-1);
                finish();
                break;
                default:
                    break;
        }
    }
}
