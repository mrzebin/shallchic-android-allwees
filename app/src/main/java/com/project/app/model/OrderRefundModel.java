package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.hb.basemodel.utils.JsonUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.AwsAccessTokenBean;
import com.project.app.bean.OrderDetailBean;
import com.project.app.bean.RefundReasonBean;
import com.project.app.contract.OrderRefundContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderRefundModel implements OrderRefundContract.Model {

    public OrderRefundModel() {

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
//                listener.onFail(e.getMessage());
            }
        });
    }

    @Override
    public void submitRefundReasonToService(String orderItemUuid, String orderUuid, List<String> photos, String reason, String remarks, int type, BaseModelResponeListener listener) {
        String requestType = "4";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.ORDER_DETAIL_URL + orderUuid + "/refund";
        List<OkHttpUtils.Param> params = new ArrayList<>();
        RefundReasonBean bean = new RefundReasonBean();
        bean.setOrderUuid(orderUuid);
        bean.setOrderItemUuid(orderItemUuid);
        bean.setPhotos(photos);
        bean.setReason(reason);
        bean.setRemarks(remarks);
        bean.setType(0);
        params.add(new OkHttpUtils.Param("params", JsonUtils.serialize(bean)));

        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<BaseObjectBean<OrderDetailBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<OrderDetailBean> response) {
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
        },params);
    }
}
