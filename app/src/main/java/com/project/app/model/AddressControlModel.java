package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.CreateAddressBean;
import com.project.app.contract.AddressControlContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddressControlModel implements AddressControlContract.Model {

    @Override
    public void editAddress(HashMap<String, String> reqBean, BaseModelResponeListener listener) {
        String requestType = "1";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.CART_ADDRESS_LIST_MANAGER + "/" + reqBean.get("uuid");
        List<OkHttpUtils.Param> params = new ArrayList<>();

        for(String key:reqBean.keySet()){
            if(key.equals("uuid")){             //过滤掉uuid
                continue;
            }
            OkHttpUtils.Param paramCell = new OkHttpUtils.Param(key,reqBean.get(key));
            params.add(paramCell);
        }

        OkHttpUtils.put(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<CreateAddressBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<CreateAddressBean> response) {
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

    @Override
    public void createAddress(HashMap<String, String> reqBean, BaseModelResponeListener listener) {
        String requestType = "1";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.CART_ADDRESS_LIST_MANAGER;
        List<OkHttpUtils.Param> params = new ArrayList<>();
        for(String key:reqBean.keySet()){
            OkHttpUtils.Param paramCell = new OkHttpUtils.Param(key,reqBean.get(key));
            params.add(paramCell);
        }
        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<BaseObjectBean<CreateAddressBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<CreateAddressBean> response) {
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
