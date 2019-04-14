package com.example.miaojie.ptest.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.BitmapCache;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TestActivity extends AppCompatActivity implements View.OnClickListener{


    private Button volley_get;
    private Button volley_post;
    private Button volley_json;
    private Button volley_imageRequest;
    private Button volley_imageLader;
    private Button netWorkImageView;
    private ImageView volley_image;
    private SimpleDraweeView volley_imageNet;
    private TextView volley_result;
    private final String TAG = "MainActivity";

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initView();
    }

    private void initView() {
        volley_get = findViewById(R.id.volley_get);
        volley_get.setOnClickListener(this);

        volley_post = findViewById(R.id.volley_post);
        volley_post.setOnClickListener(this);

        volley_json = findViewById(R.id.volley_json);
        volley_json.setOnClickListener(this);

        volley_imageRequest = findViewById(R.id.volley_imageRequest);
        volley_imageRequest.setOnClickListener(this);

        volley_imageLader = findViewById(R.id.volley_imageLader);
        volley_imageLader.setOnClickListener(this);

        netWorkImageView = findViewById(R.id.netWorkImageView);
        netWorkImageView.setOnClickListener(this);

        volley_image = findViewById(R.id.volley_image);

        volley_imageNet = findViewById(R.id.volley_imageNet);

        volley_result = findViewById(R.id.volley_result);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.volley_get://get����

                get();

                break;

            case R.id.volley_post://post����

                post();
                break;

            case R.id.volley_json://json���� JsonObjectRequest{} JsonArrayRequest[]

                json();
                break;

            case R.id.volley_imageRequest://imageRequest����ͼƬ

                image();
                break;

            case R.id.volley_imageLader://imageLader����ͼƬ
                imageLoader();

                break;

            case R.id.netWorkImageView:
                netWorkImageView();
                break;

            default:
                break;

        }
    }

    /**
     * get
     */
    public void get(){
        //����һ���������
        requestQueue = Volley.newRequestQueue(this);
        //����һ������
        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
        String url1 = "http://128.0.0.241:8080/mobileUser/login?name=000001&password=123456";

        StringRequest stringRequest =new StringRequest(url1, new Response.Listener<String>() {
            //��ȷ�������ݻص�
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    // TestData testData= new Gson().fromJson(s, TestData.class);
                    // Log.e(TAG,"length="+jsonObject.getJSONObject("trailers").length());
                    //   for (int i = 0;i<jsonObject.getJSONObject("trailers").length();i++){
                    volley_result.setText(s);
                    Log.e(TAG,"s="+jsonObject.getJSONArray("trailers").get(0)+"\n");
                    // }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {//�쳣��ļ�������
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volley_result.setText("���ش���"+volleyError);
            }
        });
        //��get������ӵ�������
        requestQueue.add(stringRequest);
    }

    /**
     * post
     */
    private void post(){
        //����һ���������
        requestQueue = Volley.newRequestQueue(this);
        //����һ������
        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                volley_result.setText(s);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volley_result.setText("���ش���"+volleyError);
            }
        }){
            @Override
            protected Map<String, String> getParams() {

                Map<String,String> map = new HashMap<>();
                // map.put("value1","param1");//�������

                return map;
            }
        };

        //��post������ӵ�������
        requestQueue.add(stringRequest);
    }

    /**
     * json
     */
    private void json(){
        //����һ���������
        requestQueue = Volley.newRequestQueue(this);

        //����һ������
        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                //TestData data = new Gson().fromJson(String.valueOf(jsonObject),TestData.class);

                volley_result.setText(jsonObject.toString());


                Log.e(TAG,"data="+jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volley_result.setText("���ش���"+volleyError);

            }
        });

        //��������������ӵ�������
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * ����ͼƬ
     */
    private void image(){
        //����һ���������
        requestQueue = Volley.newRequestQueue(this);

        //����һ������
        String url = "http://img5.mtime.cn/mg/2016/12/26/164311.99230575.jpg";
        //�ڶ�������,����������ߣ����ĸ���ͼƬ����
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                //��ȷ����ͼƬ
                volley_image.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volley_image.setImageResource(R.mipmap.ic_launcher);
            }
        });

        //��������������ӵ�������
        requestQueue.add(imageRequest);
    }

    /**
     * imageLoader
     */
    private void imageLoader(){
        //����һ���������
        requestQueue = Volley.newRequestQueue(this);

        //����һ������

//        ImageLoader imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
//            @Override
//            public Bitmap getBitmap(String s) {
//                return null;
//            }
//
//            @Override
//            public void putBitmap(String s, Bitmap bitmap) {
//
//            }
//        });

        ImageLoader imageLoader = new ImageLoader(requestQueue,new BitmapCache());//������

        //����ͼƬ
        String url = "http://img5.mtime.cn/mg/2016/12/26/164311.99230575.jpg";
        //���ز���������ʧ��
        ImageLoader.ImageListener imageLister = ImageLoader.getImageListener(volley_imageNet,R.mipmap.ic_launcher,R.mipmap.ic_launcher);
        imageLoader.get(url,imageLister);
    }

    /**
     * netWorkImageView
     */
    private void netWorkImageView(){
        //����һ���������
        requestQueue = Volley.newRequestQueue(this);

        //����һ��imageLoader
        ImageLoader imageLoader = new ImageLoader(requestQueue,new BitmapCache());

        //Ĭ��ͼƬ����
        volley_imageNet.setImageResource(R.mipmap.ic_launcher);

        //����ͼƬ
        String url = "http://img5.mtime.cn/mg/2016/12/26/164311.99230575.jpg";
        volley_imageNet.setImageURI(url,imageLoader);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //ȡ�����������е�����
        requestQueue.cancelAll(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
