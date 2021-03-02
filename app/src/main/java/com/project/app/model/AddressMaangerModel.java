package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.AddressBean;
import com.project.app.contract.AddressManagerContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddressMaangerModel implements AddressManagerContract.Model {

    @Override
    public void fetchAddressList(BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.CART_ADDRESS_LIST_MANAGER;
        HashMap<String,String> params = new HashMap<>();
        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<AddressBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<AddressBean> response) {
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
    public void deleteSpecifyAddress(String addressId, BaseModelResponeListener listener) {
        String requestType = "1";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.ADDRESS_DELETE_ITEM_URL + addressId;
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(new OkHttpUtils.Param("addressUuid",addressId));

        OkHttpUtils.delete(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<String>>() {
            @Override
            public void onSuccess(BaseObjectBean<String> response) {
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
    public void selectAddress(String addressId, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.ADDRESS_CHANGE_SELECT_URL + addressId+"/set-default";
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(new OkHttpUtils.Param("isDefault","1"));

        OkHttpUtils.put(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<String>>() {
            @Override
            public void onSuccess(BaseObjectBean<String> response) {
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
