package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.SPManager;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.CartBuyDataBean;
import com.project.app.bean.GoodsDetailInfoBean;
import com.project.app.bean.HomeFLashSaleBean;
import com.project.app.contract.FlashSaleContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FlashSaleModel implements FlashSaleContract.Model {

    public FlashSaleModel() {

    }

    @Override
    public void fetchCartData(BaseModelResponeListener listener) {
        String requestType = "0";
        String uuid = SPManager.sGetString("cartUuid");
        String url = BaseUrlConfig.getRootHost() + UrlConfig.CART_DATA_URL + uuid;
        HashMap<String,String> params = new HashMap<>();
        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException { //CartBuyDataBean
                int code;
                String msg;
                JSONObject carObj = new JSONObject(response);
                code = carObj.getInt("code");
                msg  = carObj.getString("msg");
                String data = carObj.getString("data");
                if(code == 1){
                    CartBuyDataBean bean = JsonUtils.deserialize(data,CartBuyDataBean.class);
                    if(bean != null){
                        listener.onSuccess(bean);
                    }else{
                        listener.onFail("fail");
                    }
                }else{
                    listener.onFail(msg);
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    @Override
    public void fetchFSGoodsDetail(String uuid, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.GOODS_DETAILS_URL + uuid;
        HashMap<String,String> params = new HashMap<>();
        OkHttpUtils.get(url, requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<GoodsDetailInfoBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<GoodsDetailInfoBean> response) {
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

    @Override
    public void fetchFlashSale(int type,int page, int size, BaseModelResponeListener listener) {
        String url =  BaseUrlConfig.getRootHost() + UrlConfig.FETCH_HOME_FLASH_SALE;
        String requestType = "0";
        HashMap hMap = new HashMap();
        hMap.put("current",page);
        hMap.put("size",size);
        if(type == 1){
            hMap.put("ask","tomorrow");
        }
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

            }
        },params);
    }
}
