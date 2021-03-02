package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.AppLocaleBean;
import com.project.app.bean.CountryCropBean;
import com.project.app.contract.SwitchLanguateContract;

import java.util.HashMap;

public class SwitchLanguateModel implements SwitchLanguateContract.Model {
    public SwitchLanguateModel() {

    }

    @Override
    public void fetchCountryList(BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.FETCH_COUNTRY_LIST;
        HashMap<String,String> params = new HashMap<>();
        params.put("isDefault","1");

        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<CountryCropBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<CountryCropBean> response) {
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
    public void fetchLocaleInfo(String countryId, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.FETCH_LANGUAGE_ENVIRONMENT_URL;
        HashMap hMap = new HashMap();
        hMap.put("country",countryId);

        OkHttpUtils.get(url, requestType,hMap,new OkHttpUtils.ResultCallback<BaseObjectBean<AppLocaleBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<AppLocaleBean> response) {
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
