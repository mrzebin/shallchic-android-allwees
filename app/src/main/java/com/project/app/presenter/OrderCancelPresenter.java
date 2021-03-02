package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.OrderDetailBean;
import com.project.app.contract.OrderCancelContract;
import com.project.app.model.OrderCancleModel;

public class OrderCancelPresenter extends BasePresenter<OrderCancelContract.View> implements OrderCancelContract.Presenter {
    private final OrderCancelContract.Model model;

    public OrderCancelPresenter() {
        model = new OrderCancleModel();
    }

    @Override
    public void submitCancelReasonToService(String orderItemUuid, String orderUuid, String reason, String remarks, int type) {
        model.submitCancelReasonToService(orderItemUuid, orderUuid, reason, remarks, type, new BaseModelResponeListener<OrderDetailBean>() {
            @Override
            public void onSuccess(OrderDetailBean data) {
                if(mView == null){
                    return;
                }
                mView.submitCancalSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.submitCancalFail(msg);
            }
        });
    }
}
