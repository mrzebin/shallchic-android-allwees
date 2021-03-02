package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.ClassifyListBean;
import com.project.app.bean.OrderDetailBean;
import com.project.app.contract.PayStatusContract;

import java.util.HashMap;

public class PayStatusModel implements PayStatusContract.Model {

    @Override
    public void fetchCLikeList(int currentPage, int pagetSize, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.CART_MIGHT_LIKE_URL;
        HashMap<String,String> params = new HashMap<>();
        params.put("categoryNo","P01");
        params.put("current",currentPage+"");
        params.put("size",pagetSize+"");

        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<ClassifyListBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<ClassifyListBean> response) {
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
    public void fetchOrderCashBackAmt(String orderNo, BaseModelResponeListener listener) {
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
}
