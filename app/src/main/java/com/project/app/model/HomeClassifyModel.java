package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.ClassifyListBean;
import com.project.app.contract.HomeClassifyContract;

import java.util.HashMap;

public class HomeClassifyModel implements HomeClassifyContract.Model {

    public HomeClassifyModel() {

    }

    @Override
    public void fetchGoodsInfo(int page, int size,String no, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.HOME_CLASSIFY_GOODS + no;
        HashMap hMap = new HashMap();
        hMap.put("current",page);
        hMap.put("size",size);

        OkHttpUtils.get(url, requestType,hMap,new OkHttpUtils.ResultCallback<BaseObjectBean<ClassifyListBean>>() {
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
                listener.onFail(e.getMessage());
            }
        });
    }


}
