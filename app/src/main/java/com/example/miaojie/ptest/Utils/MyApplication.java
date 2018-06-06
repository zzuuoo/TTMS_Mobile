package com.example.miaojie.ptest.Utils;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

public class MyApplication extends Application{
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        Fresco.initialize(context);
    }
    public static Context getContext()
    {
        return context;
    }
}
