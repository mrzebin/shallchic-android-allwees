package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.AppLocaleBean;
import com.project.app.bean.CategoryBean;
import com.project.app.bean.HomeBannerBean;
import com.project.app.contract.LauncherContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LauncherModel implements LauncherContract.Model {

    public LauncherModel() {

    }

    @Override
    public void fetchNormTitles(String platform, String device, String version, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.CATEGORY_GOODS_URL;
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(new OkHttpUtils.Param("platform",platform));
        params.add(new OkHttpUtils.Param("device",device));
        params.add(new OkHttpUtils.Param("version",version));

        OkHttpUtils.post(url, requestType,new OkHttpUtils.ResultCallback<BaseObjectBean<CategoryBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<CategoryBean> response) {
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
        },params);
    }

    @Override
    public void fetchLocaleInfo(BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.FETCH_LANGUAGE_ENVIRONMENT_URL;
        HashMap hMap = new HashMap();

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
                listener.onFail(e.getMessage());
            }
        });
    }

    @Override
    public void fetchAdvInfo(int advType, BaseModelResponeListener listener) {
        String requestType = "0";
        HashMap hMap = new HashMap();
        String url = BaseUrlConfig.getRootHost() + UrlConfig.FETCH_HOME_BANNER_URL + "/" + advType + "/" + Constant.APP_SOURCE_TYPE;

        OkHttpUtils.get(url, requestType,hMap,new OkHttpUtils.ResultCallback<HomeBannerBean>() {
            @Override
            public void onSuccess(HomeBannerBean response) {
//                if(response.getCode() == 1){
//                    listener.onSuccess(response);
//                }else{
//                    listener.onFail(response.getMsg());
//                }
            }
            @Override
            public void onFailure(Exception e) {
                listener.onFail(e.getMessage());
            }
        });
    }
}
