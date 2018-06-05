package com.example.miaojie.ptest.Activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.miaojie.ptest.Utils.MyDatabaseHelper;

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
        studio_name = (TextView)findViewById(R.id.studio_name_add);
        studio_col = (TextView)findViewById(R.id.studio_col_add);
        studio_row = (TextView)findViewById(R.id.studio_row_add);
        radioGroup = (RadioGroup)findViewById(R.id.add_radiogroup_studio);
        radioButton0=(RadioButton)findViewById(R.id.add_status_studio_0);
        radioButton1=(RadioButton)findViewById(R.id.add_status_studio_1);
        studio_intro = (TextView)findViewById(R.id.studio_introduction_add);
        sure=(Button)findViewById(R.id.sure_addstudio);
        cancle=(Button)findViewById(R.id.cancel_addstudio);
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
                }
            }
        };

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
                MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
                SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("studio_name",studio_name.getText().toString());
                contentValues.put("studio_row_count",studio_row.getText().toString());
                contentValues.put("studio_col_count",studio_col.getText().toString());
                contentValues.put("studio_introduction",studio_intro.getText().toString());
//                if(radioButton1.isChecked()){
//                    contentValues.put("studio_flag",1);
//                }else {
                    contentValues.put("studio_flag",0);
//                }
                long in = sqLiteDatabase.insert("studio",null,contentValues);
                if(in==-1)
                {
                    this.setResult(0);
                }else{
                    this.setResult(1);
                }
                Cursor cursor = sqLiteDatabase.rawQuery("select last_insert_rowid() from studio",null);
                int studio_id =0;
                if(cursor.moveToFirst()){
                    studio_id=cursor.getInt(0);
                }
                cursor.close();
                /**
                 * 重新生成座位
                 * 数据量大会导致应用卡死，开线程执行
                 */
                dialog.show();
                int finalStudio_id = studio_id;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
                        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        for(int i = 0;i<Integer.valueOf(studio_row.getText().toString());i++)
                        {
                            for(int j = 0;j<  Integer.valueOf(studio_col.getText().toString());j++)
                            {
                                contentValues.clear();
                                contentValues.put("studio_id", finalStudio_id);
                                contentValues.put("seat_row",i+1);
                                contentValues.put("seat_column",j+1);
                                contentValues.put("seat_status",1);
                                sqLiteDatabase.insert("seat",null,contentValues);
                            }
                        }
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }).start();

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
