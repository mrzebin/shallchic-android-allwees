package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.LogisticTrackBean;
import com.project.app.contract.LogisticsInfomationContract;

import java.util.HashMap;

public class LogisticsInformationModel implements LogisticsInfomationContract.Model {

    public LogisticsInformationModel() {

    }

    @Override
    public void checkLogisticTrack(String trackUuid, BaseModelResponeListener listener) {
        String url = BaseUrlConfig.getRootHost() + UrlConfig.ORDER_LOGISTIC_TRACK_URL + trackUuid;
        String requestType = "0";
        HashMap<String,String> params = new HashMap<>();

        OkHttpUtils.get(url, requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<LogisticTrackBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<LogisticTrackBean> response){
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
