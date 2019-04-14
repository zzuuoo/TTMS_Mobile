package com.example.miaojie.ptest.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.miaojie.ptest.R;
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

public class AddPlayActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText play_name;
    private EditText play_time;
    private EditText play_type;
    private EditText play_lang;
    private EditText play_introduction;
    private EditText play_ticket;
    private RadioButton radioButton1;
    private RadioButton radioButton0;
    private TextView play_image;
    private Button sure_addplay;
    private Button cancel_addplay;
//    LoadingDialog dialog;
    RequestQueue requestQueue ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_play);
//        dialog = new LoadingDialog(AddPlayActivity.this,"添加中...");
        init();
    }
    private void init()
    {
        play_name = findViewById(R.id.play_name_add);
        play_time = findViewById(R.id.play_time_add);
        play_type = findViewById(R.id.play_type_add);
        play_lang = findViewById(R.id.play_lang_add);
        play_introduction = findViewById(R.id.play_introduction_add);
        play_ticket= findViewById(R.id.play_add_ticket);
        radioButton0 = findViewById(R.id.add_status_0);
        radioButton1 = findViewById(R.id.add_status_1);
        play_image= findViewById(R.id.play_image_add);
        sure_addplay= findViewById(R.id.sure_addplay);
        cancel_addplay= findViewById(R.id.cancel_addplay);
        sure_addplay.setOnClickListener(this);
        cancel_addplay.setOnClickListener(this);
        radioButton1.setClickable(false);
        radioButton0.setClickable(false);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.sure_addplay:
                //本地数据库
//                    Play p = new Play();
//                p.setPlay_name(play_name.getText().toString());
//                p.setPlay_introdution(play_introduction.getText().toString());
//                p.setPlay_image(play_image.getText().toString()+"我是图片");
//                p.setPlay_lang(play_lang.getText().toString());
//                p.setPlay_length(Integer.valueOf(play_time.getText().toString()));
//                if(radioButton1.isChecked()){
//                    p.setPlay_status(1);
//                }else {
//                    p.setPlay_status(0);
//                }
//                p.setPlay_ticket_price(Double.valueOf(play_ticket.getText().toString()));
//                p.setPlay_type(play_type.getText().toString());
//                //然后保存数据库
//                MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
//                SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
//                ContentValues contentValues = new ContentValues();
//                contentValues.put("play_type",p.getPlay_type());
//                contentValues.put("play_name",p.getPlay_name());
//                contentValues.put("play_introduction",p.getPlay_introdution());
//                contentValues.put("play_image",p.getPlay_image());
//                contentValues.put("play_lang",p.getPlay_lang());
//                contentValues.put("play_length",p.getPlay_length());
//                contentValues.put("play_ticket_price",p.getPlay_ticket_price());
//                contentValues.put("play_status",p.getPlay_status());
//                long in = sqLiteDatabase.insert("play",null,contentValues);
//                if(in==-1)
//                {
//                    this.setResult(0);
//                }else{
//                    this.setResult(1);
//                }
//                finish();
//                dialog.show();
                postDataWithParame(Grob_var.host+"mobilePlay/insertPlay");
                break;
            case R.id.cancel_addplay:
                this.setResult(-1);
                finish();
                break;
                default:
                    break;

        }
    }


    private void postDataWithParame(String url) {
        Map<String,String> map = new HashMap<>();
        map.put("playType",play_type.getText().toString());//传入参数
        map.put("playName",play_name.getText().toString());//传入参数
        map.put("playIntroduction",play_introduction.getText().toString());//传入参数
        map.put("playLang",play_lang.getText().toString());//传入参数
        map.put("playLength",play_time.getText().toString());//传入参数
        map.put("playTicketPrice",play_ticket.getText().toString());//传入参数
        map.put("playStatus","0");//传入参数
        JSONObject jsonObject = new JSONObject(map);

        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
//        String jsonStr = "{\"username\":\"lisi\",\"nickname\":\"李四\"}";//json数据.
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                AddPlayActivity.this.setResult(0);
                finish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().equals("succeed"))
                {
                    AddPlayActivity.this.setResult(1);
                }else{
                    AddPlayActivity.this.setResult(0);
                }

                finish();
            }


        });
    }
    @Override
    protected void onStop() {
        super.onStop();


    }
}
