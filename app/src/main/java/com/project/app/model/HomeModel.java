package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.SPManager;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.AppUpdateBean;
import com.project.app.bean.CartBuyDataBean;
import com.project.app.bean.CategoryBean;
import com.project.app.bean.CountryCropBean;
import com.project.app.bean.MarketGiftInfoBean;
import com.project.app.contract.HomeContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeModel implements HomeContract.Model {

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
//                listener.onFail(e.getMessage());
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
//                listener.onFail(e.getMessage());
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
                        listener.onFail("nil");
                    }
                }else{
                    listener.onFail(msg);
                }
            }
            @Override
            public void onFailure(Exception e) {
//                listener.onFail(e.getMessage());
            }
        });
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
//                listener.onFail(e.getMessage());
            }
        });
    }

    @Override
    public void fetchContactInfo(BaseModelResponeListener listener) {
        String url = BaseUrlConfig.getRootHost() + UrlConfig.CONTACT_INFO_URL;
        String requestType = "0";
        HashMap hMap = new HashMap();

        OkHttpUtils.get(url, requestType,hMap,new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response){
                try {
                    int code = 0;
                    String msg = "";
                    JSONObject contactJson = new JSONObject(response);
                    code = contactJson.getInt("code");
                    msg  = contactJson.getString("msg");
                    if(code == 1){
                        listener.onSuccess(contactJson.getString("data"));
                    }else{
                        listener.onFail(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    @Override
    public void bindMiuiRegistId(String registId, boolean isBind, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.MIUI_PUSH_BIND_REGISTID;
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(new OkHttpUtils.Param("regId",registId));
        params.add(new OkHttpUtils.Param("isBind", isBind));

        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {

            }
            @Override
            public void onFailure(Exception e) {

            }
        },params);
    }

    @Override
    public void fetchHomePopularTitles(BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.CATEGORY_GOODS_URL;
        String platform = Constant.APP_PLATFROM;
        String device   = SPManager.sGetString(Constant.SP_DEVICE_ID_FLAG);
        String version  = SPManager.sGetString(Constant.SP_APP_VERSION);

        HashMap<String,String> params = new HashMap<>();
        params.put("platform",platform);
        params.put("device",device);
        params.put("version",version);

        OkHttpUtils.get(url, requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<CategoryBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<CategoryBean> response){
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
