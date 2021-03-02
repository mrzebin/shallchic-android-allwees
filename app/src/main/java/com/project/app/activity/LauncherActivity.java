package com.project.app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.appsflyer.AFInAppEventParameterName;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.AppUtils;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.SPManager;
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
import com.qmuiteam.qmui.arch.QMUILatestVisit;
import com.qmuiteam.qmui.arch.annotation.ActivityScheme;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

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
        QMUIStatusBarHelper.setStatusBarLightMode(this);
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
        //如果定义了语言则直接进入app
        if(!SPManager.sGetBoolean(Constant.SP_DEFINE_LANGUATE)){
            mPresenter.fetchLocaleInfo();
        }else{
            initPermission(); //直接进入
        }
    }

    private void afRecoceOpen() {
        String deviceId = SPManager.sGetString(Constant.SP_DEVICE_ID_FLAG);
        Map<String,Object> openEventMap = new HashMap<>();
        openEventMap.put(AFInAppEventParameterName.CUSTOMER_USER_ID,deviceId);
        AppsFlyEventUtils.sendAppInnerEvent(openEventMap, AppsFlyConfig.AF_EVENT_APP_OPEN);
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
    private void initLanguage() {
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
        if(!SPManager.sGetBoolean(Constant.SP_INIT_GUIDE_FEATURE)){
            Intent actionFeature = new Intent(this,FeatureGuideActivity.class);
            startActivity(actionFeature);
            finish();
        }else{
            Intent intent = QMUILatestVisit.intentOfLatestVisit(this);
            if(intent == null){
                intent = new Intent(this, MainActivity.class);
            }
            startActivity(intent);
            finish();
        }
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
            SPManager.sPutBoolean(Constant.SP_DEFINE_LANGUATE,true);
//            mPresenter.fetchNormTitles();
        }
    }

    @Override
    public void fetchLocaleInfoFail(String msg) {
        initLanguage();                   //第一次默认选择中东
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
    public void exceptNetError() {                          //没有网络的情况
        initLanguage();                   //第一次默认选择中东
        initPermission();
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
        overridePendingTransition(R.anim.launcher_scale_enter,R.anim.launcher_scale_exit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.onDestoryView();
            mPresenter = null;
            System.gc();
        }
    }
}