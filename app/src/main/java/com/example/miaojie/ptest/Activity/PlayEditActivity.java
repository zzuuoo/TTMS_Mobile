package com.example.miaojie.ptest.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.pojo.Grob_var;
import com.example.miaojie.ptest.pojo.Play;

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

public class PlayEditActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText play_name;
    private EditText play_time;
    private EditText play_type;
    private EditText play_lang;
    private EditText play_introduction;
    private EditText play_ticket;
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton0;
    private TextView play_image;
    private Button sure_editplay;
    private Button cancel_editplay;
    private Play play;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_edit);
        init();

    }
    public void init()
    {
        Intent intent = this.getIntent();
        play = (Play)intent.getSerializableExtra("play");
        play_ticket = findViewById(R.id.play_edit_ticket);
        play_lang = findViewById(R.id.play_lang);
        play_name = findViewById(R.id.play_name);
        play_type = findViewById(R.id.play_type);
        play_time = findViewById(R.id.play_time);
        play_introduction = findViewById(R.id.play_introduction);
        play_image = findViewById(R.id.play_image);
        radioButton0 = findViewById(R.id.status_0);
        radioButton1 = findViewById(R.id.status_1);
        radioGroup = findViewById(R.id.radiogroup);
        radioButton1.setClickable(false);
        radioButton0.setClickable(false);
//        play_status=(EditText) findViewById(R.id.play_edit_status);
        sure_editplay = findViewById(R.id.sure_editplay);
        sure_editplay.setOnClickListener(this);
        cancel_editplay = findViewById(R.id.cancel_editplay);
        cancel_editplay.setOnClickListener(this);

        if (play.getPlay_status()==0){
            radioButton0.setChecked(true);
        }else{
            radioButton1.setChecked(true);
        }

        play_name.setText(play.getPlay_name());
        play_lang.setText(String.valueOf(play.getPlay_lang()));
        play_image.setText(play.getPlay_image());
        play_introduction.setText(play.getPlay_introdution());
        play_type.setText(String.valueOf(play.getPlay_type()));
        play_time.setText(String.valueOf(play.getPlay_length()));
        play_ticket.setText(String.valueOf(play.getPlay_ticket_price()));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.sure_editplay:

//                    MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
//                    SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("play_type",play_type.getText().toString());
//                    contentValues.put("play_name",play_name.getText().toString());
//                    contentValues.put("play_introduction",play_introduction.getText().toString());
//                    contentValues.put("play_image",play_image.getText().toString());
//                    contentValues.put("play_lang",play_lang.getText().toString());
//                    contentValues.put("play_length",play_time.getText().toString());
//                    contentValues.put("play_ticket_price",play_ticket.getText().toString());
//                    if(radioButton1.isChecked()){
//                        contentValues.put("play_status",String.valueOf(1));
//                    }else {
//                        contentValues.put("play_status",String.valueOf(0));
//                    }
//
//                    int up = db.update("play",contentValues,"play_id = ?",
//                            new String[]{String.valueOf(play.getPlay_id())});
//                    if(up==1)
//                    {
//                        this.setResult(1);
//                    }else{
//                        this.setResult(0);
//                    }
//
//              finish();
                postDataWithParame(Grob_var.host+"mobilePlay/updatePlay");
                break;
            case R.id.cancel_editplay:
                this.setResult(-1);
                finish();
                break;
                default:
                    break;
        }
    }

    private void postDataWithParame(String url) {
        Map<String,String> map = new HashMap<>();
        map.put("playId",play.getPlay_id()+"");
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
                Log.e("EditPlayyyyyyyy","失败了",e);Log.e("EditPlayyyyyyyy","失败了",e);
                PlayEditActivity.this.setResult(0);
                finish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().equals("succeed"))
                {
                    PlayEditActivity.this.setResult(1);
                }else{
                    PlayEditActivity.this.setResult(0);
                    Log.e("EditPlayyyyyyyy","failed");
                }

                finish();
            }


        });
    }
}
