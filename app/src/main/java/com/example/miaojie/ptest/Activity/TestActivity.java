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

            case R.id.volley_get://get请求

                get();

                break;

            case R.id.volley_post://post请求

                post();
                break;

            case R.id.volley_json://json请求 JsonObjectRequest{} JsonArrayRequest[]

                json();
                break;

            case R.id.volley_imageRequest://imageRequest加载图片

                image();
                break;

            case R.id.volley_imageLader://imageLader加载图片
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
        //创建一个请求队列
        requestQueue = Volley.newRequestQueue(this);
        //创建一个请求
        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
        String url1 = "http://128.0.0.241:8080/mobileUser/login?name=000001&password=123456";

        StringRequest stringRequest =new StringRequest(url1, new Response.Listener<String>() {
            //正确接收数据回调
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
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volley_result.setText("加载错误"+volleyError);
            }
        });
        //将get请求添加到队列中
        requestQueue.add(stringRequest);
    }

    /**
     * post
     */
    private void post(){
        //创建一个请求队列
        requestQueue = Volley.newRequestQueue(this);
        //创建一个请求
        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                volley_result.setText(s);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volley_result.setText("加载错误"+volleyError);
            }
        }){
            @Override
            protected Map<String, String> getParams() {

                Map<String,String> map = new HashMap<>();
                // map.put("value1","param1");//传入参数

                return map;
            }
        };

        //将post请求添加到队列中
        requestQueue.add(stringRequest);
    }

    /**
     * json
     */
    private void json(){
        //创建一个请求队列
        requestQueue = Volley.newRequestQueue(this);

        //创建一个请求
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
                volley_result.setText("加载错误"+volleyError);

            }
        });

        //将创建的请求添加到队列中
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * 加载图片
     */
    private void image(){
        //创建一个请求队列
        requestQueue = Volley.newRequestQueue(this);

        //创建一个请求
        String url = "http://img5.mtime.cn/mg/2016/12/26/164311.99230575.jpg";
        //第二个参数,第三个：宽高，第四个：图片质量
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                //正确接收图片
                volley_image.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volley_image.setImageResource(R.mipmap.ic_launcher);
            }
        });

        //将创建的请求添加到队列中
        requestQueue.add(imageRequest);
    }

    /**
     * imageLoader
     */
    private void imageLoader(){
        //创建一个请求队列
        requestQueue = Volley.newRequestQueue(this);

        //创建一个请求

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

        ImageLoader imageLoader = new ImageLoader(requestQueue,new BitmapCache());//带缓存

        //加载图片
        String url = "http://img5.mtime.cn/mg/2016/12/26/164311.99230575.jpg";
        //加载不到，加载失败
        ImageLoader.ImageListener imageLister = ImageLoader.getImageListener(volley_imageNet,R.mipmap.ic_launcher,R.mipmap.ic_launcher);
        imageLoader.get(url,imageLister);
    }

    /**
     * netWorkImageView
     */
    private void netWorkImageView(){
        //创建一个请求队列
        requestQueue = Volley.newRequestQueue(this);

        //创建一个imageLoader
        ImageLoader imageLoader = new ImageLoader(requestQueue,new BitmapCache());

        //默认图片设置
        volley_imageNet.setImageResource(R.mipmap.ic_launcher);

        //加载图片
        String url = "http://img5.mtime.cn/mg/2016/12/26/164311.99230575.jpg";
        volley_imageNet.setImageURI(url,imageLoader);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //取消队列里所有的请求
        requestQueue.cancelAll(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
