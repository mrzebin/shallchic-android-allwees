package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.SPManager;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.AppUpdateBean;
import com.project.app.bean.CartBuyDataBean;
import com.project.app.bean.CountryCropBean;
import com.project.app.contract.HomeContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class HomeModel implements HomeContract.Model {

    @Override
    public void fetchUploadToken(String code, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.ACCESS_UPLOAD_TOKEN + code;
        HashMap<String,String> params = new HashMap<>();

        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<String>>() {
            @Override
            public void onSuccess(BaseObjectBean<String> response) {
                if(response.getCode() == 1){

                }else{

                }
            }
            @Override
            public void onFailure(Exception e) {
                listener.onFail(e.getMessage());
            }
        });
    }

    @Override
    public void checkAppersion(BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.APP_CHECK_VERSION;
        HashMap<String,String> params = new HashMap<>();

        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<AppUpdateBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<AppUpdateBean> response) {
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
    public void fetchCountryList(BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.FETCH_COUNTRY_LIST;
        HashMap<String,String> params = new HashMap<>();
        params.put("isDefault","1");

        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<CountryCropBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<CountryCropBean> response) {
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
                        listener.onFail("购物车为空");
                    }
                }else{
                    listener.onFail(msg);
                }
            }
            @Override
            public void onFailure(Exception e) {
                listener.onFail(e.getMessage());
            }
        });
    }

}
