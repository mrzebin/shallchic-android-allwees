package com.hb.basemodel.base;

import android.content.Context;

/**
 * create by zb
 * 获取Application 实例
 */
public class ApplicationHolder {
    private static Context applicationContext;

    public static Context getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(Context context){
        applicationContext = context;
    }

}
