package com.project.app.model;

import android.text.TextUtils;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.contract.ChangePasswordContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChangePasswordModel implements ChangePasswordContract.Model {
    public ChangePasswordModel() {

    }

    @Override
    public void requestChangePassowrd(String originPs, String newPs, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.SETTING_CHANGE_PASSWORD;

        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(new OkHttpUtils.Param("changePasswordType","ORIGIN_PWD"));
        params.add(new OkHttpUtils.Param("originPassword",originPs));
        params.add(new OkHttpUtils.Param("password",newPs));

        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    if(!TextUtils.isEmpty(response)) {
                        JSONObject cpJson = new JSONObject(response);
                        int code = cpJson.getInt("code");
                        String msg = cpJson.getString("msg");
                        if(code == 1){
                            listener.onSuccess(msg);
                        }else{
                            listener.onFail(msg);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFail(e.getMessage());
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        },params);
    }
}
