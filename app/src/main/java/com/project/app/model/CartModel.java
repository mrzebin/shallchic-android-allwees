package com.project.app.model;

import android.text.TextUtils;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.SPManager;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.AddressBean;
import com.project.app.bean.CartBuyDataBean;
import com.project.app.bean.CartCouponsBaean;
import com.project.app.bean.CartItemReqBean;
import com.project.app.bean.ChionWrapperBean;
import com.project.app.bean.ClassifyListBean;
import com.project.app.bean.PayOrderBean;
import com.project.app.contract.CartContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartModel implements CartContract.Model {

    public CartModel() {

    }

    @Override
    public void fetchCouponList(BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.CART_USER_VALID_COUPON_URL;
        HashMap<String,String> params = new HashMap<>();

        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response){
                try {
                    int code;
                    String msg;
                    JSONObject jsonObject = new JSONObject(response);
                    code = jsonObject.getInt("code");
                    msg  = jsonObject.getString("msg");
                    if(code == 1){
                        CartCouponsBaean bean = JsonUtils.deserialize(response,CartCouponsBaean.class);
                        if(bean != null){
                            listener.onSuccess(bean);
                        }
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
    public void applyCouponCode(String promoCode, BaseModelResponeListener listener) {
        String requestType = "1";
        String uuid = SPManager.sGetString("cartUuid");
        String url = BaseUrlConfig.getRootHost() + UrlConfig.CART_APPLY_PROMO_CODE_URL + uuid + "/use-coupon";
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(new OkHttpUtils.Param("cartUuid",uuid));
        params.add(new OkHttpUtils.Param("promoCode",promoCode+""));

        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<BaseObjectBean<CartBuyDataBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<CartBuyDataBean> response) {
                if(response.getCode() == 1){
                    listener.onSuccess(response.getData());
                }else{
                    listener.onFail(response.getMsg());
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        },params);
    }

    @Override
    public void deleteItemBuyGoods(String itemUuid, String skuUuid, String type, BaseModelResponeListener listener) {
        String requestType = "1";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.CART_DELETE_BUY_GOODS_URL;
        String cartUuid = SPManager.sGetString("cartUuid");
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(new OkHttpUtils.Param("cartUuid",cartUuid));
        params.add(new OkHttpUtils.Param("itemUuid",itemUuid));
        params.add(new OkHttpUtils.Param("skuUuid",skuUuid));
        params.add(new OkHttpUtils.Param("type",type));

        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<BaseObjectBean<CartBuyDataBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<CartBuyDataBean> response) {
                if(response.getCode() == 1){
                    listener.onSuccess(response.getData());
                }else{
                    listener.onFail(response.getMsg());
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        },params);
    }

    @Override
    public void requestOrderPay(CartItemReqBean reqBean, BaseModelResponeListener listener) {
        String requestType = "4";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.REQUEST_ORDER_PAY_URL;
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(new OkHttpUtils.Param("params", JsonUtils.serialize(reqBean)));

        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<BaseObjectBean<PayOrderBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<PayOrderBean> response) {
                if(response.getCode() == 1){
                    listener.onSuccess(response.getData());
                }else{
                    listener.onFail(response.getMsg());
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        },params);
    }

    @Override
    public void fetchCLikeList(int currentPage, int pagetSize, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.CART_MIGHT_LIKE_URL;
        HashMap<String,String> params = new HashMap<>();
        params.put("categoryNo","P01");
        params.put("current",currentPage+"");
        params.put("size",pagetSize+"");

        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<ClassifyListBean>>() {
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
    public void fetchAddressList(BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.CART_ADDRESS_LIST_MANAGER;
        HashMap<String,String> params = new HashMap<>();
        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<AddressBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<AddressBean> response) {
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
    public void balanceAmtByCash(Double cash, BaseModelResponeListener listener) {
        String requestType = "1";
        String cartUuid = SPManager.sGetString("cartUuid");
        String url = BaseUrlConfig.getRootHost() + UrlConfig.CART_USER_COUPON_URL+cartUuid + "/use-cash";
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(new OkHttpUtils.Param("cartUuid",cartUuid));
        if(cash >0){   //小于0则取消
            params.add(new OkHttpUtils.Param("amtCash",cash));
        }
        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<BaseObjectBean<CartBuyDataBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<CartBuyDataBean> response) {
                if(response.getCode() == 1){
                    listener.onSuccess(response.getData());
                }else{
                    listener.onFail(response.getMsg());
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        },params);
    }

    //跳转到paypal
    @Override
    public void goPayPal(String orderUuid, BaseModelResponeListener listener) {
        String requestType = "1";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.PAY_GET_PAYPAL_WEB_URL;
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(new OkHttpUtils.Param("orderUuid",orderUuid));
        params.add(new OkHttpUtils.Param("platform", Constant.APP_SOURCE_TYPE));

        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<BaseObjectBean<ChionWrapperBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<ChionWrapperBean> response) {
                if(response.getCode() == 1){
                    listener.onSuccess(response.getData());
                }else{
                    listener.onFail(response.getMsg());
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        },params);
    }

    @Override
    public void modifyBuyCount(int count, boolean incr, String skuUuid, String itemUuid, BaseModelResponeListener listener) {
        String requestType = "1";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.CART_MODIFY_BUY_COUNT_URL;
        String cartUuid = SPManager.sGetString("cartUuid");
        if(TextUtils.isEmpty(cartUuid)){
            return;
        }
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(new OkHttpUtils.Param("cartUuid",cartUuid));
        params.add(new OkHttpUtils.Param("count",count));
        params.add(new OkHttpUtils.Param("incr",incr));
        params.add(new OkHttpUtils.Param("itemUuid",itemUuid));
        params.add(new OkHttpUtils.Param("skuUuid",skuUuid));
        params.add(new OkHttpUtils.Param("type","updatearemu"));
        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<BaseObjectBean<CartBuyDataBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<CartBuyDataBean> response) {
                if(response.getCode() == 1){
                    listener.onSuccess(response.getData());
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
