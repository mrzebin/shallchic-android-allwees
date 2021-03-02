package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.AwsAccessTokenBean;
import com.project.app.bean.OrderDetailBean;
import com.project.app.contract.OrderRefundContract;
import com.project.app.model.OrderRefundModel;

import java.util.List;

public class OrderRefundPresenter extends BasePresenter<OrderRefundContract.View> implements OrderRefundContract.Presenter {
    private final OrderRefundContract.Model model;

    public OrderRefundPresenter() {
        model = new OrderRefundModel();
    }

    @Override
    public void fetchUploadToken(String code) {
        if(mView == null){
            return;
        }
        mView.startLoading();
        model.fetchUploadToken(code, new BaseModelResponeListener<AwsAccessTokenBean>() {
            @Override
            public void onSuccess(AwsAccessTokenBean data) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchUAccessTokenSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchUAccessTokenFail(msg);
            }
        });
    }

    @Override
    public void submitRefundReasonToService(String orderItemUuid, String orderUuid, List<String> photos, String reason, String remarks, int type) {
        model.submitRefundReasonToService(orderItemUuid, orderUuid, photos, reason, remarks, type, new BaseModelResponeListener<OrderDetailBean>() {
            @Override
            public void onSuccess(OrderDetailBean data) {
                if(mView == null){
                    return;
                }
                mView.postRefundSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.postRefundFail(msg);
            }
        });
    }
}
