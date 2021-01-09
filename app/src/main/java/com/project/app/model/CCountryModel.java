package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.CountryCropBean;
import com.project.app.contract.CCountryContract;

import java.util.HashMap;

public class CCountryModel implements CCountryContract.Model {

    public CCountryModel() {

    }

    @Override
    public void fetchCountryRList(BaseModelResponeListener listener) {
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
                listener.onFail(e.getMessage());
            }
        });
    }
}
