package com.project.app.model;


import android.text.TextUtils;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseUserInfo;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.hb.basemodel.utils.JsonUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.LoginTokenMeBean;
import com.project.app.contract.LoginContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginModel implements LoginContract.Model {

    @Override
    public void regist(Map<String, String> requestMap, BaseModelResponeListener listener) {
        String url = BaseUrlConfig.getRootHost() + UrlConfig.LOGIN_REGIST_URL;
        String requestType = "0";

        List<OkHttpUtils.Param> params = new ArrayList<>();
        for(Map.Entry<String,String> cell : requestMap.entrySet()){
            params.add(new OkHttpUtils.Param(cell.getKey(),cell.getValue()));
        }

        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<BaseObjectBean<String>>() {
            @Override
            public void onSuccess(BaseObjectBean<String> response) {
                if(response.getCode() == 1){
                    listener.onSuccess("success");
                }else{
                    listener.onFail(response.getMsg());
                }
            }
            @Override
            public void onFailure(Exception e) {
//                listener.onFail(e.getMessage());
            }
        },params);
    }

    @Override
    public void login(Map<String, String> requestMap, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.LOGIN_LOGIN_ULR;
        HashMap<String,String> paramMap = new HashMap<>();

        for(Map.Entry<String,String> cell : requestMap.entrySet()){
            paramMap.put(cell.getKey(),cell.getValue());
        }

        String converUrl = OkHttpUtils.attachHttpGetParams(url,paramMap);    //注意这个接口不是标准的post请求
        List<OkHttpUtils.Param> params = new ArrayList<>();

        OkHttpUtils.post(converUrl, requestType,new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response){
                if(!TextUtils.isEmpty(response)){
                    LoginTokenMeBean tokenBean = JsonUtils.deserialize(response,LoginTokenMeBean.class);
                    if(tokenBean.getCode() == 1){
                        listener.onSuccess(tokenBean);
                    }else{
                        listener.onFail(tokenBean.getMsg());
                    }
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        },params);
    }

    @Override
    public void fbLogin(Map<String, String> requestMap, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.LOGIN_THIRD_FACEBOOK_URL;

        OkHttpUtils.get(url, requestType,requestMap,new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response){
                if(!TextUtils.isEmpty(response)){
                    listener.onSuccess(response);
                }else{
                    listener.onFail("login error");
                }
            }
            @Override
            public void onFailure(Exception e) {
//                listener.onFail(e.getMessage());
            }
        });
    }

    @Override
    public void fetchUserInfo(String accessToken, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.USER_INFO_URL;
        HashMap<String,String> params = new HashMap<>();

        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<BaseUserInfo>>() {
            @Override
            public void onSuccess(BaseObjectBean<BaseUserInfo> response) {
                if(response.getCode() == 1){
                    listener.onSuccess(response.getData());
                }else{
                    listener.onFail(response.getMsg());
                }
            }
            @Override
            public void onFailure(Exception e) {
//                listener.onFail(e.getMessage());
            }
        });
    }
}
