package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.WishDataBean;
import com.project.app.contract.WishlistContract;

import java.util.HashMap;

public class WishListModel implements WishlistContract.Model {

    public WishListModel() {

    }

    @Override
    public void fetchWishList(int page,int pageSize,BaseModelResponeListener listener) {
        String url = BaseUrlConfig.getRootHost() + UrlConfig.WISH_LIST_FAVORITE_URL;
        String requestType = "0";
        HashMap hMap = new HashMap();
        hMap.put("current",page);
        hMap.put("size",pageSize);

        OkHttpUtils.get(url, requestType,hMap,new OkHttpUtils.ResultCallback<BaseObjectBean<WishDataBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<WishDataBean> response) {
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
