package com.project.app.model;


import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.contract.ContactUsContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ContactUsModel implements ContactUsContract.Model {

    @Override
    public void fetctContactUsMethods(BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.CONTACT_US_METHODS_URL;
        HashMap<String,String> params = new HashMap<>();

        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    int code = 0;
                    String msg = "";
                    JSONObject resultJsonObject = new JSONObject(response);
                    code = resultJsonObject.getInt("code");
                    msg  = resultJsonObject.getString("msg");
                    if(code == 1){
                        listener.onSuccess(response);
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
