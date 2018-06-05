package com.example.miaojie.ptest.Activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.MyDatabaseHelper;
import com.example.miaojie.ptest.pojo.Play;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_play);
        init();
    }
    private void init()
    {
        play_name = (EditText)findViewById(R.id.play_name_add);
        play_time = (EditText)findViewById(R.id.play_time_add);
        play_type = (EditText)findViewById(R.id.play_type_add);
        play_lang = (EditText)findViewById(R.id.play_lang_add);
        play_introduction = (EditText)findViewById(R.id.play_introduction_add);
        play_ticket=(EditText)findViewById(R.id.play_add_ticket);
        radioButton0 = (RadioButton)findViewById(R.id.add_status_0);
        radioButton1 = (RadioButton)findViewById(R.id.add_status_1);
        play_image=(TextView)findViewById(R.id.play_image_add);
        sure_addplay=(Button)findViewById(R.id.sure_addplay);
        cancel_addplay=(Button)findViewById(R.id.cancel_addplay);
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
                    Play p = new Play();
                    p.setPlay_name(play_name.getText().toString());
                    p.setPlay_introdution(play_introduction.getText().toString());
                    p.setPlay_image(play_image.getText().toString()+"我是图片");
                    p.setPlay_lang(play_lang.getText().toString());
                    p.setPlay_length(Integer.valueOf(play_time.getText().toString()));
                    if(radioButton1.isChecked()){
                        p.setPlay_status(1);
                    }else {
                        p.setPlay_status(0);
                    }
                    p.setPlay_ticket_price(Double.valueOf(play_ticket.getText().toString()));
                    p.setPlay_type(play_type.getText().toString());
                    //然后保存数据库
                    MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
                    SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("play_type",p.getPlay_type());
                    contentValues.put("play_name",p.getPlay_name());
                    contentValues.put("play_introduction",p.getPlay_introdution());
                    contentValues.put("play_image",p.getPlay_image());
                    contentValues.put("play_lang",p.getPlay_lang());
                    contentValues.put("play_length",p.getPlay_length());
                    contentValues.put("play_ticket_price",p.getPlay_ticket_price());
                    contentValues.put("play_status",p.getPlay_status());
                    long in = sqLiteDatabase.insert("play",null,contentValues);
                if(in==-1)
                {
                    this.setResult(0);
                }else{
                    this.setResult(1);
                }
                finish();


                break;
            case R.id.cancel_addplay:
                this.setResult(-1);
                finish();
                break;
                default:
                    break;

        }
    }
}
