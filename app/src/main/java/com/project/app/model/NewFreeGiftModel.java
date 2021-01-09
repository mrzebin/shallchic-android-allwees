package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.hb.basemodel.utils.SPManager;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.FreeGiftCouponBean;
import com.project.app.bean.HomeFreeGiftBean;
import com.project.app.bean.MarketGiftInfoBean;
import com.project.app.contract.NewFreeGiftContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewFreeGiftModel implements NewFreeGiftContract.Model {

    public NewFreeGiftModel() {

    }

    @Override
    public void fetchFreeGiftStatus(BaseModelResponeListener listener) {
        String url = BaseUrlConfig.getRootHost() + UrlConfig.FREE_GIFTS_MARKETING_INFO;
        String requestType = "0";
        HashMap hMap = new HashMap();

        OkHttpUtils.get(url, requestType,hMap,new OkHttpUtils.ResultCallback<BaseObjectBean<MarketGiftInfoBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<MarketGiftInfoBean> response){
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
    public void fetchFreeGiftsCouponStatus(BaseModelResponeListener listener) {
        String url = BaseUrlConfig.getRootHost() + UrlConfig.FREE_GIFTS_MARKETING_COUPON;
        String requestType = "0";
        HashMap hMap = new HashMap();
        List<OkHttpUtils.Param> params = new ArrayList<>();

        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<BaseObjectBean<FreeGiftCouponBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<FreeGiftCouponBean> response){
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
    public void fetchFreeGiftsList(BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.FETCH_HOME_FREE_GIFT;
        HashMap hMap = new HashMap();

        OkHttpUtils.get(url, requestType,hMap,new OkHttpUtils.ResultCallback<BaseObjectBean<HomeFreeGiftBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<HomeFreeGiftBean> response) {
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
    public void operationAddGoods(int count, boolean incr, String skuUuid, BaseModelResponeListener listener) {
        String requestType = "1";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.GOODS_ADD_TO_CART_URL;
        String uudi = SPManager.sGetString("cartUuid");
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(new OkHttpUtils.Param("cartUuid",uudi));
        params.add(new OkHttpUtils.Param("count",count+""));
        params.add(new OkHttpUtils.Param("incr",incr+""));
        params.add(new OkHttpUtils.Param("skuUuid",skuUuid));

        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<BaseObjectBean<String>>() {
            @Override
            public void onSuccess(BaseObjectBean<String> response) {
                if(response.getCode() == 1){
                    listener.onSuccess(response.getMsg());
                }else{
                    listener.onFail(response.getMsg());
                }
            }
            @Override
            public void onFailure(Exception e) {
                listener.onSuccess(e.getMessage());
            }
        },params);
    }
}
