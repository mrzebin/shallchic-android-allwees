package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.ActionFreeOneBean;
import com.project.app.contract.ActionDeliverDoubleContract;

import java.util.HashMap;

public class ActionDeliverDoubleModel implements ActionDeliverDoubleContract.Model {

    public ActionDeliverDoubleModel() {

    }

    @Override
    public void fetchActionFreeOne(String marketType, int page, int size, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.HOME_ACTION_BUY_FREE_ONE;
        HashMap hMap = new HashMap();
        hMap.put("page",page);
        hMap.put("size",size);
        hMap.put("marketingType",marketType);

        OkHttpUtils.get(url, requestType,hMap,new OkHttpUtils.ResultCallback<BaseObjectBean<ActionFreeOneBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<ActionFreeOneBean> response) {
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
