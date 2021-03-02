package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.contract.LoginForgetPContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginForgetModel implements LoginForgetPContract.Model {

    public LoginForgetModel() {

    }

    @Override
    public void sendTargetEmailCode(String targetEmail, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.LOGIN_FORGET_PASSWORD + "?email=" + targetEmail;
        HashMap<String,String> params = new HashMap<>();

        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response){
                try {
                    int code;
                    String msg;
                    JSONObject resObj = new JSONObject(response);
                    code = resObj.getInt("code");
                    msg = resObj.getString("msg");
                    if(code == 1){
                        listener.onSuccess(msg);
                    }else{
                        listener.onFail(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}
