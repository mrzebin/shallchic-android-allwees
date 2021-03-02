package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.HomeTopTipsBean;
import com.project.app.contract.OrderListContract;

import java.util.HashMap;

public class OrderListModel implements OrderListContract.Model {


    public OrderListModel() {

    }


    @Override
    public void fetchActionTopTips(BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.HOME_HORSE_RACE_LAMP_URL;
        HashMap hMap = new HashMap();

        OkHttpUtils.get(url, requestType,hMap,new OkHttpUtils.ResultCallback<BaseObjectBean<HomeTopTipsBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<HomeTopTipsBean> response) {
                if(response.getCode() == 1){
                    listener.onSuccess(response.getData());
                }else{
                    listener.onFail(response.getMsg());
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}
