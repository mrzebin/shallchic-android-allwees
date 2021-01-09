package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.CategoryBean;
import com.project.app.contract.CategoryContract;

import java.util.ArrayList;
import java.util.List;

public class CategoryModel implements CategoryContract.Model {

    public CategoryModel() {

    }

    @Override
    public void fetchGoodsInfo(String platform, String device, String version, BaseModelResponeListener listener) {
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
}
