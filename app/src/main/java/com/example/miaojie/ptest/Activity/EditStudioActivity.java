package com.example.miaojie.ptest.Activity;

import android.content.ContentValues;
import android.content.Intent;
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
    private Handler handler;
    private LoadingDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_studio);
        init();
    }
    public void init()
    {
        dialog= new LoadingDialog(this,"�޸���...");
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
        cancle.setOnClickListener(this);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        dialog.close();
                        Toast.makeText(getApplicationContext(),"�޸���λ�ɹ�",Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case 2:
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
            case R.id.sure_editstudio:
                if(Integer.valueOf(studio_row.getText().toString())>50||Integer
                        .valueOf(studio_col.getText().toString())>50)
                {
                    Toast.makeText(this,"���������ó���50",Toast.LENGTH_SHORT).show();
                    break;
                }
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

                int up=    sqLiteDatabase.update("studio",contentValues,"studio_id = ?",
                            new String[]{String.valueOf(studio.getStudio_id())});
                if(up==1)
                {
                    this.setResult(1);
                }else{
                    this.setResult(0);
                }

                /**
                 * ����������λ
                 * ��������ᵼ��Ӧ�ÿ��������߳�ִ��
                 */
                if(Integer.valueOf(studio_row.getText().toString())!=studio.getStudio_row_count()||
                        Integer.valueOf(studio_col.getText().toString())!=studio.getStudio_col_count() )
                {
                    dialog.show();
                    /**
                     * ɾ��������������λ
                     */
                    sqLiteDatabase.delete("seat","studio_id  =  ?",new String[]{String.valueOf(studio.getStudio_id())});

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
                                    contentValues.put("studio_id", studio.getStudio_id());
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
                }else{
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                }


//                new Thread(new ThreadEditSeat(studio.getStudio_id(),
//                        Integer.valueOf(studio_row.getText().toString()),
//                        Integer.valueOf(studio_col.getText().toString()))).start();



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
