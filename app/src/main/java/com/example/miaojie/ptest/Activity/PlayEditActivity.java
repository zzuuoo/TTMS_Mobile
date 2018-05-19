package com.example.miaojie.ptest.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText play_status;
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
        play_status=(EditText) findViewById(R.id.play_edit_status);
        sure_editplay = (Button) findViewById(R.id.sure_editplay);
        sure_editplay.setOnClickListener(this);
        cancel_editplay = (Button)findViewById(R.id.cancel_editplay);
        cancel_editplay.setOnClickListener(this);

        play_name.setText(play.getPlay_name());
        play_lang.setText(String.valueOf(play.getPlay_lang()));
        play_image.setText(play.getPlay_image());
        play_introduction.setText(play.getPlay_introdution());
        play_type.setText(String.valueOf(play.getPlay_type()));
        play_time.setText(String.valueOf(play.getPlay_length()));
        play_ticket.setText(String.valueOf(play.getPlay_ticket_price()));
        play_status.setText(String.valueOf(play.getPlay_status()));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.sure_editplay:
                try {
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
                    contentValues.put("play_status",play_status.getText().toString());
                    db.update("play",contentValues,"play_id = ?",
                            new String[]{String.valueOf(play.getPlay_id())});
                    this.setResult(1);

                }catch (Exception e){
                    this.setResult(0);
                }finally {
                    finish();
                }
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
