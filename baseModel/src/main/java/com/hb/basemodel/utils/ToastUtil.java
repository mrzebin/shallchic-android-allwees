package com.hb.basemodel.utils;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.basemodel.R;
import com.hb.basemodel.base.ApplicationHolder;

/**
 * create by zb
 * 吐司的工具类
 */
public class ToastUtil {
    private static Toast sToast;
    private static int textColor = R.color.white;

    public static int getTextColor() {
        return textColor;
    }

    public static void setTextColor(int textColor) {
        ToastUtil.textColor = textColor;
    }

    public static void showToast(String msg){
        if(!TextUtils.isEmpty(msg)){
//            showToast(msg,Toast.LENGTH_SHORT);
            Toast.makeText(ApplicationHolder.getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToast(int resId){
        String toastMsg = ApplicationHolder.getApplicationContext().getResources().getString(resId);
        if(!TextUtils.isEmpty(toastMsg)){
            showToast(toastMsg,Toast.LENGTH_SHORT);
        }
    }

    public static void showLongToast(String msg){
        if(!TextUtils.isEmpty(msg)){
            showToast(msg,Toast.LENGTH_LONG);
        }
    }

    public static void showLongToast(int resId){
        String resMsg = ApplicationHolder.getApplicationContext().getResources().getString(resId);
        if(!TextUtils.isEmpty(resMsg)){
            showToast(resMsg,Toast.LENGTH_LONG);
        }
    }

    public static void showToast(final String msg, final int dur){
        if(Looper.getMainLooper() == Looper.myLooper()){
            showInterval(msg,dur);
        }else{
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showInterval(msg,dur);
                }
            });
        }
    }

    private static void showInterval(String msg,int durationTime){
        if(sToast == null){
            sToast = new Toast(ApplicationHolder.getApplicationContext());
            LayoutInflater inflater = (LayoutInflater) ApplicationHolder.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.toast_layout,null);
            TextView tv_toast = v.findViewById(R.id.tv_toast);
            tv_toast.setText(msg);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tv_toast.setTextColor(ApplicationHolder.getApplicationContext().getColor(textColor));
            }
            sToast.setDuration(durationTime);
            sToast.setGravity(Gravity.CENTER_VERTICAL,0,0);
            sToast.setView(v);
        }else{
            TextView tv_toast = sToast.getView().findViewById(R.id.tv_toast);
            tv_toast.setText(msg);
            tv_toast.setTextColor(ApplicationHolder.getApplicationContext().getResources().getColor(R.color.white));
        }
        sToast.show();
    }


}
