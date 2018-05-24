package com.example.miaojie.ptest.Activity;

import android.content.ContentValues;
import android.content.Intent;
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
import com.example.miaojie.ptest.pojo.Studio;

public class EditStudioActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView studio_name;
    private TextView studio_row;
    private TextView studio_col;
    private RadioGroup radioGroup;
    private RadioButton radioButton0;
    private RadioButton radioButton1;
    private TextView studio_intro;
    private Button sure;
    private Button cancle;
    private Studio studio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_studio);
        init();
    }
    public void init()
    {
        Intent intent = this.getIntent();
        studio = (Studio) intent.getSerializableExtra("studio");
        studio_name = (TextView)findViewById(R.id.studio_name_edit);
        studio_name.setText(studio.getStudio_name());
        studio_col = (TextView)findViewById(R.id.studio_col_edit);
        studio_col.setText(studio.getStudio_col_count()+"");
        studio_row = (TextView)findViewById(R.id.studio_row_edit);
        studio_row.setText(studio.getStudio_row_count()+"");
        radioGroup = (RadioGroup)findViewById(R.id.edit_radiogroup_studio);
        radioButton0=(RadioButton)findViewById(R.id.edit_status_studio_0);
        radioButton1=(RadioButton)findViewById(R.id.edit_status_studio_1);
        if(studio.getStudio_flag()==0){
            radioButton0.setChecked(true);
        }else {
            radioButton1.setChecked(true);
        }
        studio_intro = (TextView)findViewById(R.id.studio_introduction_edit);
        studio_intro.setText(studio.getStudio_introduction());
        sure=(Button)findViewById(R.id.sure_editstudio);
        cancle=(Button)findViewById(R.id.cancel_editstudio);
        sure.setOnClickListener(this);
        cancle.setOnClickListener(this);}

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.sure_editstudio:
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
                    sqLiteDatabase.update("studio",contentValues,"studio_id = ?",
                            new String[]{String.valueOf(studio.getStudio_id())});
                }catch (Exception e)
                {
                    this.setResult(0);
                    finish();
                }
                this.setResult(1);
                finish();
                break;
            case R.id.cancel_editstudio:
                this.setResult(-1);
                finish();
                break;
            default:
                break;
        }
    }
}
