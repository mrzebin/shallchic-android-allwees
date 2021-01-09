package com.project.app;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hb.basemodel.base.ApplicationHolder;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.AppManager;
import com.project.app.config.AppEnvironmentResConfig;
import com.project.app.net.NetWorkMonitorManager;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;

import java.io.File;
import java.util.Map;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class MyApp extends MultiDexApplication {
    public static MyApp mContext;
    public static FirebaseAnalytics mFbAnalytics;
    public static AppEnvironmentResConfig mAppEnvironmentResConfig;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        NetWorkMonitorManager.getInstance().init(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = MyApp.this;
        mFbAnalytics = FirebaseAnalytics.getInstance(this);
        /**
         * 解决7.0无法使用file://格式的URL的第二种方式
         */
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            builder.detectFileUriExposure();
        }
        QMUISwipeBackActivityManager.init(this);
        AppManager.init();
        initTools();
        configCrash();
        initSmartRefreshLayout();
        initAF();
    }

    private void initAF() {
        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {
                for (String attrName : conversionData.keySet()) {
                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + conversionData.get(attrName));
                }
            }
            @Override
            public void onConversionDataFail(String errorMessage) {
                Log.d("LOG_TAG", "error getting conversion data: " + errorMessage);
            }
            @Override
            public void onAppOpenAttribution(Map<String, String> conversionData) {

                for (String attrName : conversionData.keySet()) {
                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + conversionData.get(attrName));
                }
            }
            @Override
            public void onAttributionFailure(String errorMessage) {
                Log.d("LOG_TAG", "error onAttributionFailure : " + errorMessage);
            }
        };
        String afKey = getResources().getString(R.string.af_dev_key);
        AppsFlyerLib.getInstance().init(afKey, conversionListener, getApplicationContext());
        AppsFlyerLib.getInstance().start(this);
    }

    public void initResConfig(Context context){
        mAppEnvironmentResConfig = null;
        mAppEnvironmentResConfig = AppEnvironmentResConfig.getInstance(context);
    }

    private void initTools() {
        ApplicationHolder.setApplicationContext(mContext);
    }

    public static void initEnvironment() {
        String rootDirPath = Environment.getExternalStorageDirectory()+"/" + Constant.APP_ROOT_DIR_NAME;
        File rootDirFile = new File(rootDirPath);
        if(!rootDirFile.exists()){
            rootDirFile.mkdirs();
        }
    }


    private void initSmartRefreshLayout() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                ClassicsHeader header = new ClassicsHeader(context);
                header.setAccentColor(getResources().getColor(R.color.app_refresh_bg));
                header.setPrimaryColor(getResources().getColor(R.color.app_background_default));
                return header;
            }
        });

        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                ClassicsFooter footer = new ClassicsFooter(context);
                footer.setAccentColor(getResources().getColor(R.color.app_refresh_bg));
                footer.setPrimaryColor(getResources().getColor(R.color.app_background_default));
                return footer;
            }
        });
    }

    private void configCrash() {

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
        //1.清理内存中的图片2.清理掉Activity只保留Root Activity
        switch (level) {
            case TRIM_MEMORY_COMPLETE:
                //表示 App 退出到后台，并且已经处于 LRU List 比较考靠前的位置
                break;
            case TRIM_MEMORY_RUNNING_CRITICAL:
                //表示 App 正在正常运行，但是系统已经开始根据 LRU List 的缓存规则杀掉了一部分缓存的进程
                break;
            case TRIM_MEMORY_UI_HIDDEN:
                Glide.get(this).clearMemory();
                break;
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

}
