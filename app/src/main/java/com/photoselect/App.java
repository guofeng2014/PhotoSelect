package com.photoselect;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * 作者：guofeng
 * ＊ 日期:16/11/24
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
