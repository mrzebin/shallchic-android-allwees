package com.project.app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.AppUtils;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.SPManager;
import com.qmuiteam.qmui.arch.QMUILatestVisit;
import com.qmuiteam.qmui.arch.annotation.ActivityScheme;
import com.project.app.MyApp;
import com.project.app.R;
import com.project.app.base.BaseActivity;
import com.project.app.bean.AppLocaleBean;
import com.project.app.bean.CategoryBean;
import com.project.app.config.AppsFlyConfig;
import com.project.app.contract.LauncherContract;
import com.project.app.presenter.LauncherPresenter;
import com.project.app.utils.AppsFlyEventUtils;
import com.project.app.utils.FileManager;
import com.project.app.utils.HomeTitlesUtils;
import com.project.app.utils.LocaleUtil;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


/**
 * create by hb
 * create time 2020/08/06
 *
 */
@ActivityScheme(name = "launcher")
public class LauncherActivity extends BaseActivity implements LauncherContract.View {
    private static final int PERMISSION_REQUEST_CODE = 10;
    private static final String[] PERMISSION_REQUEST= {
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE
    };
    private int mAdvType = 2;      //1是首页banner 2是开屏 3类目banner 4商品列表 5商品详情 6支付成功
    private LauncherPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) >0){
            /**为了防止重复启动多个闪屏页面**/
            finish();
            return;
        }
        initView();
        refreshLocaleEnvironment();
        MyApp.mContext.initResConfig(this);
    }

    private void initView() {
        mPresenter = new LauncherPresenter();
        mPresenter.attachView(this);
        if(!SPManager.sGetBoolean(Constant.SP_INIT)){
            AppUtils.initDataApp(this);
            SPManager.sPutBoolean(Constant.SP_INIT,true);
        }
        afRecoceOpen();
        mPresenter.fetchLocaleInfo();
    }

    private void afRecoceOpen() {
        String deviceId = SPManager.sGetString(Constant.SP_DEVICE_MODEL);
        Map<String,Object> openEventMap = new HashMap<>();
        openEventMap.put(AppsFlyConfig.AF_EVENT_APP_OPEN,deviceId);
        AppsFlyEventUtils.sendAppInnerEvent(openEventMap,"af_app_opened");
    }

    private void initPermission() {
        if(allPermissionsGranted()){
            doAfterPermissionsGranted();
        }else{
            ActivityCompat.requestPermissions(this,PERMISSION_REQUEST,PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * 初始话语言
     */
    private void initLanguage(String languateType) {
        boolean isInit = SPManager.sGetBoolean(Constant.SP_INIT_LOCALE);
        if(isInit){
            return;
        }
        String defaultLocale =  FileManager.readAssetFile(Constant.ASSERT_CATEGORY_LOCALE_AR);      //默认选择中东语言
        if(!TextUtils.isEmpty(defaultLocale)){
            AppLocaleBean bean = JsonUtils.deserialize(defaultLocale,AppLocaleBean.class);
            LocaleUtil.getInstance().setLocaleBean(bean);
        }
    }

    public boolean allPermissionsGranted(){
        for(String permission:PERMISSION_REQUEST){
           if( ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
               return false;
           }
           if(permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
               SPManager.sPutBoolean(Constant.SP_HAS_WRITE_STORAGE_PERMISSION,true);
               MyApp.initEnvironment();
           }
        }
        return true;
    }

    public void doAfterPermissionsGranted(){
        Intent intent = QMUILatestVisit.intentOfLatestVisit(this);
        if(intent == null){
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String miss = getResources().getString(R.string.str_permission_miss);
        if(requestCode == PERMISSION_REQUEST_CODE){
            if(allPermissionsGranted()){
                doAfterPermissionsGranted();
            }else{
                doAfterPermissionsGranted();
                Toast.makeText(this,miss,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void fetchLocaleInofSuccess(AppLocaleBean bean) {
        if(bean != null){
            LocaleUtil.getInstance().setLocaleBean(bean);   //设置语言环境
            initPermission();
            mPresenter.fetchNormTitles();
        }
    }

    @Override
    public void fetchLocaleInfoFail(String msg) {
        initLanguage("ar");                   //第一次默认选择中东
        SPManager.sPutBoolean(Constant.SP_INIT_LOCALE,true);
        initPermission();
    }

    @Override
    public void fetchHomeTitleSuccess(CategoryBean bean) {
        if(bean != null){
            HomeTitlesUtils instance =  HomeTitlesUtils.getInstance();
            instance.setmCategoryBean(bean);
        }
    }

    @Override
    public void fetchHomeTitleFail(String msg) {

    }

    @Override
    public void fetchAdvInfoSuccess() {

    }

    @Override
    public void fetchAdvInfoFail() {

    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }

}
