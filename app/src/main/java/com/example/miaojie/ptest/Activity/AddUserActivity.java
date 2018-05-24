package com.example.miaojie.ptest.Activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.MyDatabaseHelper;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        init();
    }
    public void init()
    {
        emp_name=(EditText)findViewById(R.id.add_emp_name);
        emp_no = (EditText)findViewById(R.id.add_emp_no);
        emp_addr=(EditText)findViewById(R.id.add_emp_addr);
        emp_email=(EditText)findViewById(R.id.add_emp_email);
        emp_pass = (EditText)findViewById(R.id.add_emp_pass);
        head_path=(ImageView)findViewById(R.id.add_head_path);
        emp_tel_num = (EditText)findViewById(R.id.add_emp_tel);
        radioGroup = (RadioGroup)findViewById(R.id.add_radiogroup_user);
        radioButtonManager = (RadioButton)findViewById(R.id.add_userTypeManager);
        radioButtonSaler = (RadioButton)findViewById(R.id.add_userTypeSaler);
        sure = (Button)findViewById(R.id.sure_addUser);
        cancle = (Button)findViewById(R.id.cancel_addUser);
        sure.setOnClickListener(this);
        cancle.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.sure_addUser:
                MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
                SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                ContentValues contentValues1 = new ContentValues();
                contentValues.put("emp_no",emp_no.getText().toString());
                contentValues.put("emp_name",emp_name.getText().toString());
                contentValues.put("emp_tel_num",emp_tel_num.getText().toString());
                contentValues.put("emp_addr",emp_addr.getText().toString());
                contentValues.put("emp_email",emp_email.getText().toString());
                contentValues1.put("emp_no",emp_no.getText().toString());
                contentValues1.put("emp_pass",emp_pass.getText().toString());
                if(radioButtonManager.isChecked()){
                    contentValues1.put("type",1);
                }else {
                    contentValues1.put("type",0);
                }
                contentValues1.put("head_path","我是头像路径");
                try {
                    sqLiteDatabase.insert("employee",null,contentValues);
                    sqLiteDatabase.insert("user",null,contentValues1);
                }catch (Exception e)
                {
                    this.setResult(0);
                }
                this.setResult(1);
                finish();
                break;
            case R.id.cancel_addUser:
                this.setResult(-1);
                finish();
                break;
        }
    }
}
