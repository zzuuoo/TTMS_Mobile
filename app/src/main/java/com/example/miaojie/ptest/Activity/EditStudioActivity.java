package com.example.miaojie.ptest.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.LoadingDialog;
import com.example.miaojie.ptest.pojo.Grob_var;
import com.example.miaojie.ptest.pojo.Studio;

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
//        dialog = new LoadingDialog(EditStudioActivity.this,"加载中");
        Intent intent = this.getIntent();
        studio = (Studio) intent.getSerializableExtra("studio");
        studio_name = findViewById(R.id.studio_name_edit);
        studio_name.setText(studio.getStudio_name());
        studio_col = findViewById(R.id.studio_col_edit);
        studio_col.setText(studio.getStudio_col_count()+"");
        studio_row = findViewById(R.id.studio_row_edit);
        studio_row.setText(studio.getStudio_row_count()+"");
        radioGroup = findViewById(R.id.edit_radiogroup_studio);
        radioButton0= findViewById(R.id.edit_status_studio_0);
        radioButton1= findViewById(R.id.edit_status_studio_1);
        if(studio.getStudio_flag()==0){
            radioButton0.setChecked(true);
        }else {
            radioButton1.setChecked(true);
        }
        radioButton1.setClickable(false);
        radioButton0.setClickable(false);
        studio_intro = findViewById(R.id.studio_introduction_edit);
        studio_intro.setText(studio.getStudio_introduction());
        sure= findViewById(R.id.sure_editstudio);
        cancle= findViewById(R.id.cancel_editstudio);
        sure.setOnClickListener(this);
        cancle.setOnClickListener(this);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
//                        dialog.close();
                        Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case 2:
//                        dialog.close();
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
                    Toast.makeText(this,"行列数不得超过50",Toast.LENGTH_SHORT).show();
                    break;
                }
//                MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
//                SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
//                ContentValues contentValues = new ContentValues();
//                contentValues.put("studio_name",studio_name.getText().toString());
//                contentValues.put("studio_row_count",studio_row.getText().toString());
//                contentValues.put("studio_col_count",studio_col.getText().toString());
//                contentValues.put("studio_introduction",studio_intro.getText().toString());
//                if(radioButton1.isChecked()){
//                    contentValues.put("studio_flag",1);
//                }else {
//                    contentValues.put("studio_flag",0);
//                }
//
//                int up=    sqLiteDatabase.update("studio",contentValues,"studio_id = ?",
//                            new String[]{String.valueOf(studio.getStudio_id())});
//                if(up==1)
//                {
//                    this.setResult(1);
//                }else{
//                    this.setResult(0);
//                }
//
//                /**
//                 * 重新生成座位
//                 * 数据量大会导致应用卡死，开线程执行
//                 */
//                if(Integer.valueOf(studio_row.getText().toString())!=studio.getStudio_row_count()||
//                        Integer.valueOf(studio_col.getText().toString())!=studio.getStudio_col_count() )
//                {
//                    dialog = new LoadingDialog(EditStudioActivity.this,"修改中...");
//                    dialog.show();
//                    /**
//                     * 删除该厅的所有座位
//                     */
//                    sqLiteDatabase.delete("seat","studio_id  =  ?",new String[]{String.valueOf(studio.getStudio_id())});
//
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance();
//                            SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
//                            ContentValues contentValues = new ContentValues();
//                            for(int i = 0;i<Integer.valueOf(studio_row.getText().toString());i++)
//                            {
//                                for(int j = 0;j<  Integer.valueOf(studio_col.getText().toString());j++)
//                                {
//                                    contentValues.clear();
//                                    contentValues.put("studio_id", studio.getStudio_id());
//                                    contentValues.put("seat_row",i+1);
//                                    contentValues.put("seat_column",j+1);
//                                    contentValues.put("seat_status",1);
//                                    sqLiteDatabase.insert("seat",null,contentValues);
//                                }
//                            }
//                            Message message = new Message();
//                            message.what = 1;
//                            handler.sendMessage(message);
//                        }
//                    }).start();
//                }else{
//                    Message message = new Message();
//                    message.what = 2;
//                    handler.sendMessage(message);
//                }
//                dialog.show();
                postDataWithParame(Grob_var.host+"mobileStudio/updateStudio");
                break;
            case R.id.cancel_editstudio:
                this.setResult(-1);
                finish();
                break;
            default:
                break;
        }
    }

    private void postDataWithParame(String url) {

        Map<String,String> map = new HashMap<>();
        map.put("studioId",studio.getStudio_id()+"");
        map.put("studioName",studio_name.getText().toString());
        map.put("studioRowCount",studio_row.getText().toString());//传入参数
        map.put("studioColCount",studio_col.getText().toString());//传入参数
        map.put("studioIntroduction",studio_intro.getText().toString());//传入参数
        map.put("studioFlag",studio.getStudio_flag()+"");//传入参数
        JSONObject jsonObject = new JSONObject(map);

        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("EditPlayyyyyyyy","失败了",e);Log.e("EditPlayyyyyyyy","失败了",e);
//                Message message = new Message();
//                message.what = 2;
//                handler.sendMessage(message);
                EditStudioActivity.this.setResult(0);
                finish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().equals("succeed"))
                {

                    EditStudioActivity.this.setResult(1);
                }else{

                    EditStudioActivity.this.setResult(0);
                    Log.e("EditPlayyyyyyyy","failed");
                }
                finish();
            }

        });
    }
}
