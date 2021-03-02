package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.hb.basemodel.utils.JsonUtils;
import com.project.app.bean.CartCouponsBaean;
import com.project.app.contract.CartCouponsDialogContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CartCouponDialogModel implements CartCouponsDialogContract.Model {

    public CartCouponDialogModel() {

    }

    @Override
    public void fetchCouponList(BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.CART_USER_VALID_COUPON_URL;
        HashMap<String,String> params = new HashMap<>();

        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response){
                try {
                    int code;
                    String msg;
                    JSONObject jsonObject = new JSONObject(response);
                    code = jsonObject.getInt("code");
                    msg  = jsonObject.getString("msg");
                    if(code == 1){
                        CartCouponsBaean bean = JsonUtils.deserialize(response,CartCouponsBaean.class);
                        if(bean != null){
                            listener.onSuccess(bean);
                        }
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
