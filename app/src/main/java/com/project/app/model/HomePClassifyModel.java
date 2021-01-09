package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.ActionFreeOneBean;
import com.project.app.bean.ClassifyListBean;
import com.project.app.bean.HomeBannerBean;
import com.project.app.bean.HomeFLashSaleBean;
import com.project.app.bean.HomeFreeGiftBean;
import com.project.app.contract.HomePClassifyContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class HomePClassifyModel implements HomePClassifyContract.Model {

    public HomePClassifyModel() {

    }

    @Override
    public void fetchFreeGift(BaseModelResponeListener listener) {
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

    @Override
    public void fetchBannerInfo(String url, BaseModelResponeListener listener) {
        String requestType = "0";
        HashMap hMap = new HashMap();

        OkHttpUtils.get(url, requestType,hMap,new OkHttpUtils.ResultCallback<HomeBannerBean>() {
            @Override
            public void onSuccess(HomeBannerBean response) {
//                if(response.getCode() == 1){
//                    listener.onSuccess(response);
//                }else{
//                    listener.onFail(response.getMsg());
//                }
            }
            @Override
            public void onFailure(Exception e) {
                listener.onFail(e.getMessage());
            }
        });
    }

    @Override
    public void fetchFlashSale(int page, int size, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.FETCH_HOME_FLASH_SALE;
        HashMap hMap = new HashMap();
        hMap.put("current",page);
        hMap.put("size",size);

        OkHttpUtils.get(url, requestType,hMap,new OkHttpUtils.ResultCallback<BaseObjectBean<HomeFLashSaleBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<HomeFLashSaleBean> response) {
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
    public void fetchActionFreeOne(BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.HOME_ACTION_BUY_FREE_ONE;
        HashMap hMap = new HashMap();

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
                listener.onFail(e.getMessage());
            }
        });
    }

    @Override
    public void fetchHomePopularMax(BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.HOME_CLASSIFY_POPULAR_MAX;
        HashMap hMap = new HashMap();

        OkHttpUtils.get(url, requestType,hMap,new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject objJson = new JSONObject(response);
                    int code = objJson.getInt("code");
                    String msg = objJson.getString("msg");
                    if(code == 1){
                        listener.onSuccess(objJson.getString("data"));
                    }else{
                        listener.onFail(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Exception e) {
                listener.onFail(e.getMessage());
            }
        });
    }
}
