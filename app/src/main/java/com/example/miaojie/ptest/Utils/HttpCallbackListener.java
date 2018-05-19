package com.example.miaojie.ptest.Utils;

public interface HttpCallbackListener {
    void onFinish(String response);
    void onErroe(Exception e);
}
