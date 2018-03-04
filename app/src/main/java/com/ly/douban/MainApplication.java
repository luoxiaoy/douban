package com.ly.douban;

import android.app.Application;

/**
 * Created by ly on 2018/2/9.
 */

public class MainApplication extends Application {

    private static MainApplication INSTANCE;

    public static MainApplication getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
