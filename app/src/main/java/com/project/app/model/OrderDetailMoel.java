package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.LogisticTrackBean;
import com.project.app.bean.OrderDetailBean;
import com.project.app.contract.OrderDetailContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderDetailMoel implements OrderDetailContract.Model {

    @Override
    public void fetchDetailData(String orderNo, BaseModelResponeListener listener) {
        String url = BaseUrlConfig.getRootHost() + UrlConfig.ORDER_DETAIL_URL + orderNo;
        String requestType = "0";
        HashMap<String,String> params = new HashMap<>();

        OkHttpUtils.get(url, requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<OrderDetailBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<OrderDetailBean> response) {
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

    @Override
    public void checkLogisticTrack(String trackUuid, BaseModelResponeListener listener) {
        String url = BaseUrlConfig.getRootHost() + UrlConfig.ORDER_LOGISTIC_TRACK_URL + trackUuid;
        String requestType = "0";
        HashMap<String,String> params = new HashMap<>();

        OkHttpUtils.get(url, requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<LogisticTrackBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<LogisticTrackBean> response){
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

    @Override
    public void receiveGoodsRequest(String orderId, BaseModelResponeListener listener) {
        String url = BaseUrlConfig.getRootHost() + UrlConfig.ORDER_DETAIL_URL + orderId + "/confirmShipping";
        String requestType = "1";
        List<OkHttpUtils.Param> params = new ArrayList<>();
//        params.add(new OkHttpUtils.Param("orderUuid",orderId));

        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                listener.onSuccess(response);
            }
            @Override
            public void onFailure(Exception e) {
//                listener.onFail(e.getMessage());
            }
        },params);
    }
}
