package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.FreeGiftBean;
import com.project.app.contract.HomeFreeGiftContract;

import java.util.HashMap;

public class HomeFreeGiftKindsModel implements HomeFreeGiftContract.Model {

    public HomeFreeGiftKindsModel() {

    }

    @Override
    public void fetchFreeGift(int page, int size, String url, BaseModelResponeListener listener) {
        String requestType = "0";
        HashMap hMap = new HashMap();
        hMap.put("current",page);
        hMap.put("size",size);

        OkHttpUtils.get(url, requestType,hMap,new OkHttpUtils.ResultCallback<BaseObjectBean<FreeGiftBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<FreeGiftBean> response) {
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
