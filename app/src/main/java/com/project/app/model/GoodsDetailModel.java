package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.hb.basemodel.utils.SPManager;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.GoodsDetailInfoBean;
import com.project.app.bean.GoodsRelationBean;
import com.project.app.contract.GoodsDetailContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GoodsDetailModel implements GoodsDetailContract.Model {

    public GoodsDetailModel() {
        
    }

    @Override
    public void fetchCoverList(String url, BaseModelResponeListener listener) {
        String requestType = "0";
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
//                listener.onFail(e.getMessage());
            }
        });
    }

    @Override
    public void fetchRelativeBox(String url, BaseModelResponeListener listener) {
        String requestType = "0";
        HashMap<String,String> params = new HashMap<>();
        OkHttpUtils.get(url, requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<List<GoodsRelationBean>>>() {
            @Override
            public void onSuccess(BaseObjectBean<List<GoodsRelationBean>> response) {
                if(response.getCode() == 1){
                    listener.onSuccess(response.getData());
                }else{
                    listener.onFail(response.getMsg());
                }
            }
            @Override
            public void onFailure(Exception e) {
//                listener.onFail(e.getMessage());
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

        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject objJson = new JSONObject(response);
                    if(objJson.getInt("code") == 1){
                        listener.onSuccess(objJson.getString("data"));
                    }else{
                        listener.onFail(objJson.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Exception e) {
                listener.onFail(e.getMessage());
            }
        },params);
    }

    @Override
    public void operationFavoriteAdd(String uuid, BaseModelResponeListener listener) {
        String requestType = "1";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.GOODS_FAVORITE_ADD_URL;
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(new OkHttpUtils.Param("uuid",uuid));

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
//                listener.onSuccess(e.getMessage());
            }
        },params);
    }

    @Override
    public void operationFavoriteCancel(String uuid, BaseModelResponeListener listener) {
        String requestType = "1";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.GOODS_FAVORITE_CANCEL_URL;
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(new OkHttpUtils.Param("uuid",uuid));
        
        OkHttpUtils.delete(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<String>>() {
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
//                listener.onSuccess(e.getMessage());
            }
        });
    }


}
