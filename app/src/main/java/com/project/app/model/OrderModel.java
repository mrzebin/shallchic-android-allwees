package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.OrderBean;
import com.project.app.contract.OrderContract;

import java.util.HashMap;

public class OrderModel implements OrderContract.Model {


    public OrderModel() {

    }

    @Override
    public void fetchOrderList(HashMap<String,String> hsMap, BaseModelResponeListener listener) {
        String url = BaseUrlConfig.getRootHost() + UrlConfig.ORDER_COMMON_URL;
        String requestType = "0";
        HashMap<String,String> params = new HashMap<>();
        url = OkHttpUtils.attachHttpGetParams(url,hsMap);
        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<OrderBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<OrderBean> response) {
                if(response.getCode() == 1){
                    listener.onSuccess(response.getData());
                }else{
                    listener.onFail(response.getMsg());
                }
            }
            @Override
            public void onFailure(Exception e) {
                listener.onFail(e.getMessage());
            }
        });
    }
}
