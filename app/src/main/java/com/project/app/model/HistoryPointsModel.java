package com.project.app.model;


import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.HistoryPointsBean;
import com.project.app.contract.HistoryPointsContract;

import java.util.HashMap;

public class HistoryPointsModel implements HistoryPointsContract.Model {

    @Override
    public void fetchHistoryPoints(int currentPage, int pageSize, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.FETCH_RECODE_DASHBOARD_H_URL;
        HashMap hMap = new HashMap();
        hMap.put("current",currentPage);
        hMap.put("size",pageSize);

        OkHttpUtils.get(url, requestType,hMap,new OkHttpUtils.ResultCallback<BaseObjectBean<HistoryPointsBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<HistoryPointsBean> response) {
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
