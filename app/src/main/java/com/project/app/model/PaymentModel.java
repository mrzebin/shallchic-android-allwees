package com.project.app.model;

import android.text.TextUtils;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.hb.basemodel.utils.JsonUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.CartItemReqBean;
import com.project.app.bean.ChionWrapperBean;
import com.project.app.bean.PayOrderBean;
import com.project.app.bean.PaymentCheckBean;
import com.project.app.contract.PaymentContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaymentModel implements PaymentContract.Model {

    public PaymentModel() {

    }

    @Override
    public void checkCod(String orderUuid, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.PAYMENT_CHECK_CODE_URL;
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(new OkHttpUtils.Param("orderUuid",orderUuid));

        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<BaseObjectBean<PaymentCheckBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<PaymentCheckBean> response) {
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
        },params);
    }

    @Override
    public void smsVerify(String orderUuid, String phoneNum, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.PAYMENT_SEND_SMS;
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(new OkHttpUtils.Param("orderUuid",orderUuid));
        params.add(new OkHttpUtils.Param("phone",phoneNum));

        OkHttpUtils.post(url, requestType,new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response){
                try {
                    int code = 0;
                    String msg;
                    JSONObject resultObj = new JSONObject(response);
                    code = resultObj.getInt("code");
                    msg  = resultObj.getString("msg");
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
        },params);
    }

    @Override
    public void codPay(String orderUuid, String phone, String code, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.PAYMENT_PAY_ORDER;
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(new OkHttpUtils.Param("orderUuid",orderUuid));
        params.add(new OkHttpUtils.Param("phone",phone));
        if(!TextUtils.isEmpty(code)){
            params.add(new OkHttpUtils.Param("code",code));
        }

        OkHttpUtils.post(url, requestType,new OkHttpUtils.ResultCallback<BaseObjectBean<PayOrderBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<PayOrderBean> response) {
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
        },params);
    }

    @Override
    public void creditCardPay(String orderUuid, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.PAY_CHANNEL_CREDIT_URL;
        HashMap hMap = new HashMap();
        hMap.put("orderUuid",orderUuid);

        OkHttpUtils.get(url, requestType,hMap,new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
               if(response.contains("<!doctype html>")){
                   listener.onSuccess(response);
               }else{
                   listener.onFail("fail");
               }
            }
            @Override
            public void onFailure(Exception e) {
//                listener.onFail(e.getMessage());
            }
        });
    }

    @Override
    public void goPayPal(String orderUuid, BaseModelResponeListener listener) {
        String requestType = "1";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.PAY_GET_PAYPAL_WEB_URL;
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(new OkHttpUtils.Param("orderUuid",orderUuid));
        params.add(new OkHttpUtils.Param("platform", Constant.APP_SOURCE_TYPE));

        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<BaseObjectBean<ChionWrapperBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<ChionWrapperBean> response) {
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
        },params);
    }

    @Override
    public void refreshPayOrder(CartItemReqBean reqBean, BaseModelResponeListener listener) {
        String requestType = "4";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.REQUEST_ORDER_PAY_URL;
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(new OkHttpUtils.Param("params", JsonUtils.serialize(reqBean)));

        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<BaseObjectBean<PayOrderBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<PayOrderBean> response) {
                if(response.getCode() == 1){
                    listener.onSuccess(response.getData());
                }else{
                    listener.onFail(response.getMsg());
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        },params);
    }

    @Override
    public void createPayOrder(CartItemReqBean reqBean, BaseModelResponeListener listener) {
        String requestType = "4";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.PAYMENT_PAY_CREATE_ORDER;
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(new OkHttpUtils.Param("params", JsonUtils.serialize(reqBean)));
        
        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<BaseObjectBean<PayOrderBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<PayOrderBean> response) {
                if(response.getCode() == 1){
                    listener.onSuccess(response.getData());
                }else{
                    listener.onFail(response.getMsg());
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        },params);
    }

}
