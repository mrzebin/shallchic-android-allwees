package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.hb.basemodel.utils.JsonUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.AwsAccessTokenBean;
import com.project.app.bean.RefundReasonBean;
import com.project.app.contract.OrderReviewContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderReviewModel implements OrderReviewContract.Model {

    public OrderReviewModel() {

    }

    @Override
    public void fetchUploadToken(String code, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.ACCESS_UPLOAD_TOKEN + code;
        HashMap<String,String> params = new HashMap<>();
        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<AwsAccessTokenBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<AwsAccessTokenBean> response){
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
    public void submitReviewReasonToService(String orderUuid, String orderItemUuid, int rating, String reason, List<String> photos, BaseModelResponeListener listener) {
        String requestType = "4";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.ORDER_REVIEW_URL;
        List<OkHttpUtils.Param> params = new ArrayList<>();
        RefundReasonBean bean = new RefundReasonBean();
        bean.setOrderUuid(orderUuid);
        bean.setOrderItemUuid(orderItemUuid);
        bean.setPhotos(photos);
        bean.setRating(rating);
        bean.setText(reason);
        bean.setType(0);
        params.add(new OkHttpUtils.Param("params", JsonUtils.serialize(bean)));

        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    int code = 0;
                    String msg = "";
                    JSONObject resultObj = new JSONObject(response);
                    code = resultObj.getInt("code");
                    msg  = resultObj.getString("msg");
                    if(code == 1){
                        listener.onSuccess(msg);
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
        },params);
    }

}
