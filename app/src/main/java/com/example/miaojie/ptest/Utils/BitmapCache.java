package com.example.miaojie.ptest.Utils;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Í¼Æ¬»º´æ
 */
public class BitmapCache implements ImageLoader.ImageCache{

    private LruCache<String,Bitmap> mCache;

    public BitmapCache() {
        int maxSize = 10* 1024 *1024;//10m
        mCache = new LruCache<String,Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url,bitmap);
    }
}