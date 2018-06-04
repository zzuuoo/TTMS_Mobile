package com.example.miaojie.ptest.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.MyDatabaseHelper;
import com.example.miaojie.ptest.pojo.Play;

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
        play_ticket = (EditText)findViewById(R.id.play_edit_ticket);
        play_lang = (EditText)findViewById(R.id.play_lang);
        play_name = (EditText)findViewById(R.id.play_name);
        play_type = (EditText)findViewById(R.id.play_type);
        play_time = (EditText)findViewById(R.id.play_time);
        play_introduction = (EditText)findViewById(R.id.play_introduction);
        play_image = (TextView) findViewById(R.id.play_image);
        radioButton0 = (RadioButton)findViewById(R.id.status_0);
        radioButton1 = (RadioButton)findViewById(R.id.status_1);
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
//        play_status=(EditText) findViewById(R.id.play_edit_status);
        sure_editplay = (Button) findViewById(R.id.sure_editplay);
        sure_editplay.setOnClickListener(this);
        cancel_editplay = (Button)findViewById(R.id.cancel_editplay);
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

                    MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
                    SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("play_type",play_type.getText().toString());
                    contentValues.put("play_name",play_name.getText().toString());
                    contentValues.put("play_introduction",play_introduction.getText().toString());
                    contentValues.put("play_image",play_image.getText().toString());
                    contentValues.put("play_lang",play_lang.getText().toString());
                    contentValues.put("play_length",play_time.getText().toString());
                    contentValues.put("play_ticket_price",play_ticket.getText().toString());
                    if(radioButton1.isChecked()){
                        contentValues.put("play_status",String.valueOf(1));
                    }else {
                        contentValues.put("play_status",String.valueOf(0));
                    }

                    int up = db.update("play",contentValues,"play_id = ?",
                            new String[]{String.valueOf(play.getPlay_id())});
                    if(up==1)
                    {
                        this.setResult(1);
                    }else{
                        this.setResult(0);
                    }

              finish();
                break;
            case R.id.cancel_editplay:
                this.setResult(-1);
                finish();
                break;
                default:
                    break;
        }
    }
}
