package com.project.app.config;

import android.content.Context;
import android.content.res.Resources;

import com.project.app.R;

import java.util.HashMap;
import java.util.Map;

public class AppEnvironmentResConfig {
    private Context mContext;
    private static com.project.app.config.AppEnvironmentResConfig mInstance;
    public  static Map<String,String> mToastCacheMap;

    public AppEnvironmentResConfig(Context context){
        mContext = context;
        initToast();
        initRes();
    }

    public static com.project.app.config.AppEnvironmentResConfig getInstance(Context context){
        if(mInstance == null){
            mInstance = new com.project.app.config.AppEnvironmentResConfig(context);
        }
        return mInstance;
    }

    /**
     * 初始化吐司
     */
    public void initToast() {
        Resources mAppResources = mContext.getResources();
        mToastCacheMap = new HashMap<>();
        mToastCacheMap.put("login_fail",(String) mAppResources.getText(R.string.login_fail));
        mToastCacheMap.put("login_success",(String) mAppResources.getText(R.string.login_success));
    }

    public HashMap<String,String> initFreeGift(){
        HashMap<String,String> freeGiftMap = new HashMap<>();
        Resources mAppResources = mContext.getResources();
        freeGiftMap.put("str_use", (String) mAppResources.getText(R.string.free_gift_res_use));
        freeGiftMap.put("str_useIn", (String) mAppResources.getText(R.string.free_gift_res_use_in));
        freeGiftMap.put("str_expire", (String) mAppResources.getText(R.string.free_gift_res_expire));
        freeGiftMap.put("free_gift_hint_choose", (String) mAppResources.getText(R.string.free_gift_hint_choose));
        return freeGiftMap;
    }


    public HashMap<String,String> initReward(){
        HashMap<String,String> dataMap = new HashMap<>();
        Resources mAppResources = mContext.getResources();
        dataMap.put("str_points", (String) mAppResources.getText(R.string.reward_res_point));
        return dataMap;
    }


    //初始化资源
    private void initRes() {

    }

}
