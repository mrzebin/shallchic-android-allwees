package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.AddMoreListBean;
import com.project.app.contract.AddMoreGoodsContract;

import java.util.HashMap;

public class AddMoreGoodsModel implements AddMoreGoodsContract.Model {

    @Override
    public void fetchGoodsList(String categoryNo, int currentPage, int pagetSize, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.ADD_MORE_GOODS_URL;
        HashMap<String,String> params = new HashMap<>();
        params.put("categoryNo",categoryNo);
        params.put("current",currentPage+"");
        params.put("size",pagetSize+"");
        
        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<AddMoreListBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<AddMoreListBean> response) {
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
