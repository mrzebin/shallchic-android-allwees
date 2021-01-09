package com.hb.basemodel.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.hb.basemodel.config.Constant;

import androidx.core.content.ContextCompat;

public class AppUtils {

    /**
     * 第一次登录初始化数据
     */
    public static synchronized void initDataApp(Context context){
        String deviceId      =  getIMEI(context);
        String deviceModel   = SystemUtil.getSystemModel();
        String deviceBrand   = SystemUtil.getDeviceBrand();
        String systemVersion = SystemUtil.getSystemVersion();
        String appVersion    = getVersionName(context);

        SPManager.sPutString(Constant.SP_DEVICE_ID_FLAG,MD5.secret(deviceId));
        SPManager.sPutString(Constant.SP_APP_VERSION,appVersion);
        SPManager.sPutString(Constant.SP_DEVICE_VERSIONCODE, systemVersion);

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
            if(!TextUtils.isEmpty(SystemUtil.getSystemModel())){
                SPManager.sPutString(Constant.SP_DEVICE_MODEL,deviceModel);
            }else{
                SPManager.sPutString(Constant.SP_DEVICE_MODEL,"unknow");
            }
            if(!TextUtils.isEmpty(SystemUtil.getDeviceBrand())){
                SPManager.sPutString(Constant.SP_DEVICE_BRAND,deviceBrand);
            }else{
                SPManager.sPutString(Constant.SP_DEVICE_BRAND,"unknow");
            }
        }
        LoggerUtil.i("imei:" + deviceId + "--phoneModel:" + deviceModel + "-deviceBrand:" + deviceBrand + "-version:" + systemVersion);
    }

    /**
     * 获取手机IMEI
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            if (imei == null) {
                imei = MD5.secret("a" + System.currentTimeMillis() + StringAppUtils.getRandomString(16));
            }
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return MD5.secret("a" + System.currentTimeMillis() + StringAppUtils.getRandomString(16));
        }
    }

    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.APP_VERSION_NAME;
        }
    }


    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取图标 bitmap
     * @param context
     */
    public static synchronized Bitmap getBitmap(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo;
        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
        BitmapDrawable bd = (BitmapDrawable) d;
        return bd.getBitmap();
    }

}
