package com.example.miaojie.ptest.Utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

    public static void seandHttpRequestGet(String address,HttpCallbackListener listener){
        String addr = address;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(addr);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line= bufferedReader.readLine())!=null){
                        response.append(line);
                    }
                    if(listener!=null){
                        listener.onFinish(response.toString());
                    }

                } catch (Exception e) {
                    if(listener!=null){
                        listener.onErroe(e);
                    }
                } finally {
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
    public static void seandHttpRequestPost(String address,HttpCallbackListener listener)
    {
        String addr = address;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(addr);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line= bufferedReader.readLine())!=null){
                        response.append(line);
                    }
                    if(listener!=null){
                        listener.onFinish(response.toString());
                    }

                } catch (Exception e) {
                    if(listener!=null){
                        listener.onErroe(e);
                    }
                } finally {
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

}
