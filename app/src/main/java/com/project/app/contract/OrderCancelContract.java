package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.OrderDetailBean;

public interface OrderCancelContract {

    interface Model {
        void submitCancelReasonToService(String orderItemUuid, String orderUuid,String reason,String remarks, int type, BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void submitCancalSuccess(OrderDetailBean result);
        void submitCancalFail(String result);
    }

    interface Presenter{
        void submitCancelReasonToService(String orderItemUuid, String orderUuid,String reason,String remarks,int type);
    }
}
