package com.example.miaojie.ptest.Utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// 访问服务器接口类
public class HttpAccess
{
    private static final String
//            SERVER_URL="http://222.24.63.104:8080/ttmsall/";
    SERVER_URL = "http://10.0.2.2:8080/";
//    "http://127.0.0.1:8080/mobileUser/login/？name=000001&password=123456"
    private static String sessionid="";
    @SuppressWarnings("finally")
    public static String checkUser(String userId, String password)
    {
        String result="0";// 默认为0，表示验证失败
        try
        {
//            String urlString=SERVER_URL + "ClientLogin?name=" +
//                    userId + "&pass=" + password;
            String urlString=SERVER_URL + "mobileUser/login?name=" +
                    userId + "&password=" + password;
            Log.e("uuuuuuurrrlll",urlString);
            URL myUrl=new URL(urlString);
// 使用HttpURLConnection打开连接
            HttpURLConnection urlConn = (HttpURLConnection)
                    myUrl.openConnection();
// set-cookie: JSESSIONID=E575C722CF20783370B51597B944010B;
//            Path=/ttmsall; HttpOnly
//            String cookieval=urlConn.getHeaderField("set-cookie");
//            Log.i("--------->", cookieval);
//// 获取sessionid
//            if(cookieval != null) sessionid=cookieval.substring(0,
//                    cookieval.indexOf(";"));
//            Log.i("--------->", sessionid);
            urlConn.connect();
            urlConn.setConnectTimeout(3000);
            urlConn.setReadTimeout(3000);
// 得到读取的内容(流)
            InputStreamReader in = new
                    InputStreamReader(urlConn.getInputStream());
// 为输出创建BufferedReader
            BufferedReader buffer=new BufferedReader(in);
            result=buffer.readLine();
            Log.i("--------->", result);
            in.close();
            urlConn.disconnect();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            return result;
        }
    }
    @SuppressWarnings("finally")
    public static String getUser() {
        String result = null;
        try {
            String urlString = SERVER_URL + "client/GetUser";
            URL myUrl = new URL(urlString);
// 使用HttpURLConnection打开连接
            HttpURLConnection urlConn =
                    (HttpURLConnection) myUrl.openConnection();
            if (sessionid != null) {
// 发送请求时带上cookie(包含sessionid)
                urlConn.setRequestProperty("cookie", sessionid);
            }
            urlConn.connect();
// 设置为超时时间为3秒，如果3秒内不能连接就被认为是有错误发生
            urlConn.setConnectTimeout(3000);
            urlConn.setReadTimeout(3000);
// 得到读取的内容(流)
            InputStreamReader in =
                    new InputStreamReader(urlConn.getInputStream());
// 为输出创建BufferedReader
            BufferedReader buffer = new BufferedReader(in);
            result = buffer.readLine();
            Log.i("--------->", result);
            in.close();
            urlConn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }
}