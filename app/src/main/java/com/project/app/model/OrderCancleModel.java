package com.project.app.model;


import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.hb.basemodel.utils.JsonUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.OrderDetailBean;
import com.project.app.bean.RefundReasonBean;
import com.project.app.contract.OrderCancelContract;

import java.util.ArrayList;
import java.util.List;

public class OrderCancleModel implements OrderCancelContract.Model {

    @Override
    public void submitCancelReasonToService(String orderItemUuid, String orderUuid, String reason, String remarks, int type, BaseModelResponeListener listener) {
        String requestType = "4";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.ORDER_DETAIL_URL + orderItemUuid + "/cancel";
        List<OkHttpUtils.Param> params = new ArrayList<>();
        RefundReasonBean bean = new RefundReasonBean();
        bean.setOrderUuid(orderUuid);
        bean.setOrderItemUuid(orderItemUuid);
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
