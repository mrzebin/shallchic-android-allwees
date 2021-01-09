package com.project.app.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.hb.basemodel.base.BaseUserInfo;
import com.hb.basemodel.base.LocalUserInfo;
import com.hb.basemodel.config.AppConfig;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.other.UserUtil;
import com.hb.basemodel.utils.AppUtils;
import com.hb.basemodel.utils.MD5;
import com.hb.basemodel.utils.RxRegTool;
import com.hb.basemodel.utils.SPManager;
import com.hb.basemodel.utils.ToastUtil;
import com.hb.basemodel.view.SuperEditView;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.contract.LoginContract;
import com.project.app.presenter.LoginPresenter;
import com.project.app.utils.LocaleUtil;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * careate by zb
 * 登录注册页面
 */
public class LoginOutfFragment extends BaseMvpQmuiFragment<LoginPresenter> implements LoginContract.View {
    @BindView(R.id.et_regist_fName)
    SuperEditView et_regist_fName;
    @BindView(R.id.et_regist_lName)
    SuperEditView et_regist_lName;
    @BindView(R.id.et_registEmail)
    SuperEditView et_registEmail;
    @BindView(R.id.et_registPassword)
    SuperEditView et_registPassword;
    @BindView(R.id.btn_registPost)
    QMUIRoundButton btn_registPost;
    @BindView(R.id.iv_thirdLogin_facebook)
    ImageView iv_thirdLogin_facebook;
    @BindView(R.id.login_button)
    LoginButton mFackbookLogin;

    private final String mGrantType = "password";
    private String mFbToken;
    private String mGoPrivacyp;
    private LoginPresenter mPresenter;
    private CallbackManager mCallbackManager;
    private String mRegistFName,mRegistLName,mRegistEmail,mRegistPassword;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_loginout;
    }

    @Override
    public void initView() {
        mGoPrivacyp = getContext().getResources().getString(R.string.login_pp);
        initWidget();
    }

    @Override
    protected void lazyFetchData() {

    }

    private void initWidget() {
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
        mGoPrivacyp = getContext().getResources().getString(R.string.login_pp);
        mCallbackManager = CallbackManager.Factory.create();
        AccessToken mLocaleAccessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = mLocaleAccessToken != null && !mLocaleAccessToken.isExpired();
        if(isLoggedIn){
            mFbToken = mLocaleAccessToken.getToken();
        }
        mFackbookLogin.setReadPermissions("email");
        mFackbookLogin.setFragment(this);
        mFackbookLogin.registerCallback(mCallbackManager,new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String token = loginResult.getAccessToken().getToken();
                fbLogin(token);
            }
            @Override
            public void onCancel() {

            }
            @Override
            public void onError(FacebookException error) {

            }
        });
        btn_registPost.setChangeAlphaWhenPress(true);
    }

    private boolean inputValid(){
        String warn_input = getContext().getResources().getString(R.string.str_input_warn);
        mRegistFName = et_regist_fName.editText.getText().toString().trim();
        mRegistLName = et_regist_lName.editText.getText().toString().trim();
        mRegistEmail = et_registEmail.editText.getText().toString().trim();
        mRegistPassword = et_registPassword.editText.getText().toString().trim();

        if(TextUtils.isEmpty(mRegistFName)){
            ToastUtil.showToast(warn_input);
            return false;
        }
        if(TextUtils.isEmpty(mRegistLName)){
            ToastUtil.showToast(warn_input);
            return false;
        }

        if(TextUtils.isEmpty(mRegistEmail)){
            ToastUtil.showToast(warn_input);
            return false;
        }

        if(TextUtils.isEmpty(mRegistPassword)){
            ToastUtil.showToast(warn_input);
            return false;
        }

        return true;
    }

    private boolean verifyInputValid() {
        if(TextUtils.isEmpty(mRegistEmail)){
            return false;
        }
        if(!RxRegTool.isEmail(mRegistEmail)){
            String validHint = getResources().getString(R.string.hint_email_valid);
            ToastUtil.showToast(validHint);
            return false;
        }
//        if(mRegistPassword.length() <8){
//            ToastUtil.showToast("The password must contain 8 to 16 \n characters with both alphabets and numbers!");
//            return false;
//        }

//        if(!RxRegTool.isPassword(mRegistPassword)){
//            ToastUtil.showToast("The password must contain 8 to 16\n" +
//                    "Passwords 8 to 16 Numbers mixed with letters cannot have first letters as Numbers");
//            return false;
//        }
        return true;
    }

    @OnClick({R.id.btn_registPost,R.id.ll_login_goPrivacy,R.id.iv_thirdLogin_facebook})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.btn_registPost:
                if(inputValid() && verifyInputValid()){
                    registApp();
                }
                break;
            case R.id.ll_login_goPrivacy:
                Bundle goP = new Bundle();
                String skipUrl = "";
                if(LocaleUtil.getInstance().getLanguage().equals("en")){
                    skipUrl = UrlConfig.PRIVACY_POLICY_EN;
                }else{
                    skipUrl = UrlConfig.PRIVACY_POLICY;
                }
                goP.putString("type","1");
                goP.putString("webUrl",skipUrl);
                goP.putString("title",mGoPrivacyp);
                Intent intent = HolderActivity.of(getContext(), WebExplorerFragment.class,goP);
                getContext().startActivity(intent);
                break;
            case R.id.iv_thirdLogin_facebook:
                if(!TextUtils.isEmpty(mFbToken)){
                    fbLogin(mFbToken);
                }else{
                    mFackbookLogin.performClick();
                }
                break;
        }
    }

    private void fbLogin(String token){
        Map<String,String> requestParams = new HashMap<>();
        String appName      = AppUtils.getAppName(getContext());
        String appVersion   = AppUtils.getVersionCode(getContext()) + "";
        String market       = Constant.CHANNEL_ID;
        String deviceId     = SPManager.sGetString(Constant.SP_DEVICE_ID_FLAG);
        String deviceModel  = SPManager.sGetString(Constant.SP_DEVICE_MODEL);
        String deviceSource = Constant.DEVICE_RESOURCE;
        String sourceType   = Constant.APP_SOURCE_TYPE;
        String systemVersion = Build.VERSION.SDK_INT + "";
        String pushId       = Constant.PUSH_ID;

        requestParams.put("appName",appName);
        requestParams.put("appVersion",appVersion);
        requestParams.put("deviceId",deviceId);
        requestParams.put("deviceModel",deviceModel);
        requestParams.put("deviceSource",deviceSource);
        requestParams.put("market",market);
        requestParams.put("pushId",pushId);
        requestParams.put("token",token);
        requestParams.put("type","FACEBOOK");
        requestParams.put("sourceType",sourceType);
        requestParams.put("systemVersion",systemVersion);
        mPresenter.fbLogin(requestParams);
    }

    private void registApp() {
        Map<String,String> requestParams = new HashMap<>();
        String appName = AppUtils.getAppName(getContext());
        String appVersion = AppUtils.getVersionCode(getContext()) + "";
        String bundleId = AppConfig.BUNDID;
        String deviceId = SPManager.sGetString(Constant.SP_DEVICE_ID_FLAG);
        String deviceModel = SPManager.sGetString(Constant.SP_DEVICE_MODEL);
        String deviceSource = Constant.DEVICE_RESOURCE;
        String shareCode    = Constant.SHARE_CODE;
        String sourceType   = Constant.APP_SOURCE_TYPE;
        String time         = System.currentTimeMillis() + "";
        String systemVersion = Build.VERSION.SDK_INT + "";

        requestParams.put("appName",appName);
        requestParams.put("appVersion",appVersion);
        requestParams.put("bundleId",bundleId);
        requestParams.put("deviceId",deviceId);
        requestParams.put("deviceModel",deviceModel);
        requestParams.put("deviceSource",deviceSource);
        requestParams.put("email",mRegistEmail);
        requestParams.put("firstName",mRegistFName);
        requestParams.put("lastName",mRegistLName);
        requestParams.put("password",mRegistPassword);
        requestParams.put("shareCode",shareCode);
        requestParams.put("market",Constant.APP_MARKET);
        requestParams.put("referer",Constant.HTTP_REFER);
        requestParams.put("systemVersion",systemVersion);
        requestParams.put("sourceType",sourceType);
        requestParams.put("time",time);

        String recretLink = mRegistEmail +
                "_APP_" + mRegistFName+
                "_APP_"  + mRegistLName +
                "_APP_" + shareCode +
                "_APP_" + sourceType +
                "_APP_" + deviceSource  +
                "_APP_" + deviceId +
                "_APP_"  + time;
        
        requestParams.put("signature",MD5.secret(recretLink));
        mPresenter.regist(requestParams);
    }

    private void login() {
        Map<String,String> requestParams = new HashMap<>();
        String appName          = AppUtils.getAppName(getContext());
        String appVersion       = AppUtils.getVersionCode(getContext()) + "";
        String bundleId         = AppConfig.BUNDID;
        String deviceId         = SPManager.sGetString(Constant.SP_DEVICE_ID_FLAG);
        String deviceModel      = SPManager.sGetString(Constant.SP_DEVICE_MODEL);
        String deviceSource     = Constant.DEVICE_RESOURCE;
        String shareCode        = Constant.SHARE_CODE;
        String sourceType       = Constant.APP_SOURCE_TYPE;
        String time             = System.currentTimeMillis() + "";
        String systemVersion    = Build.VERSION.SDK_INT + "";
        String pushId           = Constant.PUSH_ID;

        requestParams.put("username",mRegistEmail);
        requestParams.put("password",mRegistPassword);
        requestParams.put("grant_type",mGrantType);
        requestParams.put("sourceType",sourceType);
        requestParams.put("pushId",pushId);
        requestParams.put("appVersion",appVersion);
        mPresenter.login(requestParams);
    }

    @Override
    public void fetchRegistSuccess(String result) {
        if(result.equals("success")){
            login();
        }else{
            ToastUtil.showToast(result);
        }
    }

    @Override
    public void fetchLoginSuccess(LocalUserInfo result) {
        UserUtil.getInstance().setUserInfo(result);
        mPresenter.fetchUserInfo();
    }

    @Override
    public void fetchFbLoginSuccess(LocalUserInfo result) {
        UserUtil.getInstance().setUserInfo(result);
        mPresenter.fetchUserInfo();
    }

    @Override
    public void fetchFail(String failReason){
        ToastUtil.showToast(failReason);
    }

    @Override
    public void fetchUserInfoSuccess(BaseUserInfo result) {
        String toast_loginSuccess =  getResources().getString(R.string.login_success);
        ToastUtil.showToast(toast_loginSuccess);
        UserUtil.getInstance().setUserLoginInfo(result);
        EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_LOGIN_SUCCESS));
        getActivity().finish();
    }

    @Override
    public void startLoading() {
        startProgressDialog(getContext());
    }

    @Override
    public void stopLoading() {
        stopProgressDialog();
    }

    @Override
    public void showErrorTip(String msg) {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
