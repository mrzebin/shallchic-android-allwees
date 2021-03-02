package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.hb.basemodel.utils.JsonUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.MeBindCPBean;
import com.project.app.bean.RedeemBean;
import com.project.app.contract.RedeemContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RedeemModel implements RedeemContract.Model {
    public RedeemModel() {

    }

    @Override
    public void upUsePointChangeCoupon(String cpUuid, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.FETCH_REDEEM_POINT_PREFIX + cpUuid + "/point-exchange-coupon";
        List<OkHttpUtils.Param> params = new ArrayList<>();

        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {

            }
            @Override
            public void onFailure(Exception e) {
//                listener.onFail(e.getMessage());
            }
        },params);
    }

    @Override
    public void fetchRedeemInfo(int page, int pageSize, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.FETCH_REDEEM_INFO_URL;
        HashMap<String,String> params = new HashMap<>();
        params.put("current",String.valueOf(page));
        params.put("size",String.valueOf(pageSize));

        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response){
                RedeemBean redeemBean = JsonUtils.deserialize(response,RedeemBean.class);
                int code = redeemBean.getCode();
                String msg = redeemBean.getMsg();
                if(code == 1){
                    listener.onSuccess(redeemBean);
                }else{
                    listener.onFail(msg);
                }
            }
            @Override
            public void onFailure(Exception e) {
//                listener.onFail(e.getMessage());
            }
        });
    }

    @Override
    public void fetchBindPointInfo(BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.FETCH_ME_RED_POING_URL;
        HashMap<String,String> params = new HashMap<>();

        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<MeBindCPBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<MeBindCPBean> response) {
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
