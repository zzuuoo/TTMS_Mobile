package com.example.miaojie.ptest.Activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.miaojie.ptest.R;
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.sure_addstudio:
                MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
                SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("studio_name",studio_name.getText().toString());
                contentValues.put("studio_row_count",studio_row.getText().toString());
                contentValues.put("studio_col_count",studio_col.getText().toString());
                contentValues.put("studio_introduction",studio_intro.getText().toString());
                if(radioButton1.isChecked()){
                    contentValues.put("studio_flag",1);
                }else {
                    contentValues.put("studio_flag",0);
                }
                try {
                    sqLiteDatabase.insert("studio",null,contentValues);
                }catch (Exception e)
                {
                    this.setResult(0);
                    finish();
                }
                this.setResult(1);
                finish();
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
