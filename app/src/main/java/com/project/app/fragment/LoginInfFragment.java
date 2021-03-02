package com.project.app.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.hb.basemodel.utils.RxRegTool;
import com.hb.basemodel.utils.SPManager;
import com.hb.basemodel.utils.ToastUtil;
import com.hb.basemodel.view.SuperEditView;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.config.AppsFlyConfig;
import com.project.app.contract.LoginContract;
import com.project.app.presenter.LoginPresenter;
import com.project.app.utils.AppsFlyEventUtils;
import com.project.app.utils.LocaleUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * create by zb
 * 登录
 */
public class LoginInfFragment extends BaseMvpQmuiFragment<LoginPresenter> implements LoginContract.View{
    @BindView(R.id.et_loginEmail)
    SuperEditView et_loginEmail;
    @BindView(R.id.et_loginPassword)
    SuperEditView et_loginPassword;
    @BindView(R.id.btn_postLogin)
    Button btn_postLogin;
    @BindView(R.id.tv_retrivePw)
    TextView tv_retrivePw;
    @BindView(R.id.iv_thirdLogin_facebook)
    ImageView iv_thirdLogin_facebook;
    @BindView(R.id.iv_thirdLogin_google)
    ImageView iv_thirdLogin_google;
    @BindView(R.id.login_button)
    LoginButton mFackbookLogin;

    private String mAccount;
    private String mAccountPw;
    private String mGrantType;
    private String mGoPrivacyp;
    private String mFbToken;
    private LoginPresenter mPresenter;
    private CallbackManager mCallbackManager;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_loginin;
    }

    @Override
    public void initView() {
        initWidget();
    }

    @Override
    protected void lazyFetchData() {

    }

    private void initWidget() {
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
        mGoPrivacyp = getContext().getResources().getString(R.string.login_pp);

        mFackbookLogin.setReadPermissions("email");
        mFackbookLogin.setFragment(this);
        mCallbackManager = CallbackManager.Factory.create();
        AccessToken mLocaleAccessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = mLocaleAccessToken != null && !mLocaleAccessToken.isExpired();
        if(isLoggedIn){
            mFbToken = mLocaleAccessToken.getToken();
        }
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
    }

    private boolean inputValid(){
        String warn_input = getContext().getResources().getString(R.string.str_input_warn);
        mAccount = et_loginEmail.editText.getText().toString().trim();
        mAccountPw = et_loginPassword.editText.getText().toString().trim();

        if(TextUtils.isEmpty(mAccount)){
            ToastUtil.showToast(warn_input);
            return false;
        }
        if(TextUtils.isEmpty(mAccountPw)){
            ToastUtil.showToast(warn_input);
            return false;
        }
        return true;
    }

    private boolean verifyInputValid() {
        if(TextUtils.isEmpty(mAccount)){
            return false;
        }
        if(!RxRegTool.isEmail(mAccount)){
            String invalidEmail = getContext().getResources().getString(R.string.hint_email_valid);
            ToastUtil.showToast(invalidEmail);
            return false;
        }
        if(mAccountPw.length() <6){
            String invalidPs = getContext().getResources().getString(R.string.hint_ps_length);
            ToastUtil.showToast(invalidPs);
            return false;
        }
        return true;
    }

    @OnClick({R.id.btn_postLogin,R.id.tv_retrivePw,R.id.ll_login_goPrivacy,R.id.iv_thirdLogin_facebook,R.id.iv_thirdLogin_google})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.btn_postLogin:
                if(inputValid() && verifyInputValid()){
                    mGrantType = "password";
                    login();
                    AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_LOGIN_APPLY);
                }
                break;
            case R.id.tv_retrivePw:
                Bundle bundle = new Bundle();
                String targetE = et_loginEmail.editText.getText().toString().trim();
                bundle.putString("email",targetE);
                HolderActivity.startFragment(getContext(),ForgetPasswordFragment.class,bundle);
                AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_LOGIN_FORGET_PASSWORD);
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
                goP.putString("webUrl", skipUrl);
                goP.putString("title",mGoPrivacyp);
                HolderActivity.startFragment(getContext(),WebExplorerFragment.class,goP);
                break;
            case R.id.iv_thirdLogin_facebook:
                AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_LOGIN_FACEBOOK);
                if(!TextUtils.isEmpty(mFbToken)){
                    fbLogin(mFbToken);
                }else{
                    mFackbookLogin.performClick();
                }
                break;
            case R.id.iv_thirdLogin_google:

                break;
        }
    }

    private void login() {
        Map<String,String> requestParams = new HashMap<>();
        String appName      = AppUtils.getAppName(getContext());
        String appVersion   = AppUtils.getVersionCode(getContext()) + "";
        String bundleId     = AppConfig.BUNDID;
        String deviceId     = SPManager.sGetString(Constant.SP_DEVICE_ID_FLAG);
        String deviceModel  = SPManager.sGetString(Constant.SP_DEVICE_MODEL);
        String deviceSource = Constant.DEVICE_RESOURCE;
        String shareCode    = Constant.SHARE_CODE;
        String sourceType   = Constant.APP_SOURCE_TYPE;
        String time         = System.currentTimeMillis() + "";
        String systemVersion = Build.VERSION.SDK_INT + "";
        String pushId       = Constant.PUSH_ID;

        requestParams.put("username",mAccount);
        requestParams.put("password",mAccountPw);
        requestParams.put("grant_type",mGrantType);
        requestParams.put("sourceType",sourceType);
        requestParams.put("pushId",pushId);
        requestParams.put("appVersion",appVersion);
        mPresenter.login(requestParams);
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

    @Override
    public void fetchRegistSuccess(String msg) {

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
    public void fetchFail(String failReason) {
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

