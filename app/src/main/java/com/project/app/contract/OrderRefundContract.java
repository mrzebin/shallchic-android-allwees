package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.OrderDetailBean;
import com.project.app.bean.RefundAccessTokenBean;

import java.util.List;

public interface OrderRefundContract {

    interface Model {
        void fetchUploadToken(String code,BaseModelResponeListener listener);
        void submitRefundReasonToService(String orderItemUuid, String orderUuid, List<String> photos, String reason, String remarks, int type, BaseModelResponeListener listener);
    }

    interface View extends BaseView{
        void postRefundSuccess(OrderDetailBean result);
        void postRefundFail(String result);
        void fetchUAccessTokenSuccess(RefundAccessTokenBean result);
        void fetchUAccessTokenFail(String result);
    }

    interface Presenter{
        void fetchUploadToken(String code);
        void submitRefundReasonToService(String orderItemUuid, String orderUuid, List<String> photos,String reason,String remarks,int type);
    }

}
