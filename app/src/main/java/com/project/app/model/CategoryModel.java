package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.CategoryAllBean;
import com.project.app.contract.CategoryContract;

import java.util.HashMap;

public class CategoryModel implements CategoryContract.Model {
    public CategoryModel() {

    }

    @Override
    public void fetchCateofyAll(String platform, String device, String version, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.CATEGORY_ALL_URL;
        HashMap hMap = new HashMap();
        hMap.put("platform",platform);
        hMap.put("device",device);
        hMap.put("version",version);

        OkHttpUtils.get(url, requestType,hMap,new OkHttpUtils.ResultCallback<BaseObjectBean<CategoryAllBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<CategoryAllBean> response) {
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
