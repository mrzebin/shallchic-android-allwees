package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.ProvinceBean;
import com.project.app.contract.ProvinceCityContract;

import java.util.HashMap;
import java.util.List;

public class ProvinceCityModel implements ProvinceCityContract.Model {

    public ProvinceCityModel() {

    }

    @Override
    public void fetchCityList(String regionId, String provinceId, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.STATE_PROVINCE_URL + regionId + "/child"+ "/" + provinceId;
        HashMap<String,String> params = new HashMap<>();
        params.put("isDefault","1");

        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<List<ProvinceBean>>>() {
            @Override
            public void onSuccess(BaseObjectBean<List<ProvinceBean>> response) {
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

    @Override
    public void fetchProvinceList(String region, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.STATE_PROVINCE_URL + region + "/child";
        HashMap<String,String> params = new HashMap<>();
        params.put("isDefault","1");

        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<List<ProvinceBean>>>() {
            @Override
            public void onSuccess(BaseObjectBean<List<ProvinceBean>> response) {
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
