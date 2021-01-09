package com.hb.basemodel.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hb.basemodel.base.ApplicationHolder;

/**
 * Created by zb
 */

public class SPManager {
    private static final String DEFAULT_SP_NAME = "cp";

    private static final Context sContext = ApplicationHolder.getApplicationContext();
    private final SharedPreferences sharedPreferences;

    private SPManager(String spName) {
        sharedPreferences = sContext.getSharedPreferences(spName, 0);
    }

    public static SPManager get(String spName) {
        return new SPManager(spName);
    }

    public static SPManager getDefault() {
        return DefaultInstanceHolder.instance;
    }

    public static void sPutString(String key, String value) {
        getDefault().putString(key, value);
    }

    public static String sGetString(String key) {
        return getDefault().getString(key);
    }

    public static void sPutInt(String key, int value) {
        getDefault().putInt(key, value);
    }

    public static int sGetInt(String key) {
        return getDefault().getInt(key);
    }

    public static int sGetInt(String key, int defValue) {
        return getDefault().sharedPreferences.getInt(key, defValue);
    }

    public static void sPutLong(String key, long value) {
        getDefault().putLong(key, value);
    }

    public static long sGetLong(String key) {
        return getDefault().getLong(key);
    }

    public static long sGetLong(String key, long defValue) {
        return getDefault().sharedPreferences.getLong(key, defValue);
    }

    public static void sClear(String key) {
        getDefault().clear(key);
    }

    public static void sPutBoolean(String key, boolean value) {
        getDefault().putBoolean(key, value);
    }

    public static boolean sGetBoolean(String key) {
        return getDefault().getBoolean(key);
    }

    public static boolean sGetBoolean(String key, boolean defaultValue) {
        return getDefault().getBoolean(key, defaultValue);
    }

    public void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public void putInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public void putLong(String key, long value) {
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public void clear(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public void clearAll() {
        sharedPreferences.edit().clear();
    }

    public void putBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    private static class DefaultInstanceHolder {
        private static final SPManager instance = new SPManager(DEFAULT_SP_NAME);
    }
}
