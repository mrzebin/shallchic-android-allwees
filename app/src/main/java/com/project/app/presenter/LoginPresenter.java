package com.project.app.presenter;


import android.text.TextUtils;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseUserInfo;
import com.hb.basemodel.base.LocalUserInfo;
import com.hb.basemodel.other.UserUtil;
import com.project.app.base.BasePresenter;
import com.project.app.contract.LoginContract;
import com.project.app.model.LoginModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    private final LoginContract.Model model;
    public LoginPresenter(){
        model = new LoginModel();
    }

    @Override
    public void regist(Map<String, String> params) {
        model.regist(params, new BaseModelResponeListener<String>() {
            @Override
            public void onSuccess(String data) {
                mView.fetchRegistSuccess(data);
            }

            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void login(Map<String, String> params) {
        model.login(params, new BaseModelResponeListener<String>() {
            @Override
            public void onSuccess(String data) {
                if(!TextUtils.isEmpty(data)){
                    try {
                        int code;
                        String msg;
                        JSONObject object = new JSONObject(data);
                        code = object.getInt("code");
                        msg   = object.getString("msg");
                        if(code == 1){
                            LocalUserInfo localUserInfo = new LocalUserInfo();
                            localUserInfo.setLogin(true);
                            localUserInfo.setAccess_token(object.getString("access_token"));
                            localUserInfo.setToken_type(object.getString("token_type"));
                            mView.fetchLoginSuccess(localUserInfo);
                        }else{
                            mView.fetchFail(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void fbLogin(Map<String, String> params) {
        model.fbLogin(params, new BaseModelResponeListener<String>() {
            @Override
            public void onSuccess(String data) {
                if(!TextUtils.isEmpty(data)){
                    try {
                        int code;
                        String msg;
                        JSONObject object = new JSONObject(data);
                        code = object.getInt("code");
                        msg   = object.getString("msg");
                        if(code == 1){
                            LocalUserInfo localUserInfo = new LocalUserInfo();
                            localUserInfo.setLogin(true);
                            localUserInfo.setAccess_token(object.getString("access_token"));
                            localUserInfo.setToken_type(object.getString("token_type"));
                            mView.fetchFbLoginSuccess(localUserInfo);
                        }else{
                            mView.fetchFail(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
            }
        });
    }

    //获取用户信息
    @Override
    public void fetchUserInfo() {
        mView.startLoading();
        String accessToken = UserUtil.getInstance().getUserAccessToken();
        model.fetchUserInfo(accessToken, new BaseModelResponeListener<BaseUserInfo>() {
            @Override
            public void onSuccess(BaseUserInfo data) {
                mView.fetchUserInfoSuccess(data);
                mView.stopLoading();
            }
            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
                mView.stopLoading();
            }
        });
    }
}
