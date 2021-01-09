package com.hb.basemodel.utils;

import android.util.Log;

/**
 * create by zb
 *
 */
public class LoggerUtil {
    public static final boolean isDebug = true;
    private static final String TAG = "Capricorn";

    public static void v(String logMsg){
        if(isDebug){
            Log.v(TAG,logMsg);
        }
    }

    public static void v(String tag,String logMsg){
        if(isDebug){
            Log.v(tag,logMsg);
        }
    }

    public static void i(String logMsg){
        if(isDebug){
            Log.i(TAG,logMsg);
        }
    }

    public static void i(String tag,String logMsg){
        if(isDebug){
            Log.i(tag,logMsg);
        }
    }

    public static void e(String logMsg){
        if(isDebug){
            Log.e(TAG,logMsg);
        }
    }

    public static void e(String tag,String logMsg){
        if(isDebug){
            Log.e(tag,logMsg);
        }
    }

}
