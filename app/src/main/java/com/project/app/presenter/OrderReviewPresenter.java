package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.AwsAccessTokenBean;
import com.project.app.contract.OrderReviewContract;
import com.project.app.model.OrderReviewModel;

import java.util.List;

public class OrderReviewPresenter extends BasePresenter<OrderReviewContract.View> implements OrderReviewContract.Presenter {
    private final OrderReviewModel model;

    public OrderReviewPresenter() {
        model = new OrderReviewModel();
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
    public void submitReviewReasonToService(String orderUuid, String orderItemUuid, int rating, String reason, List<String> photos) {
        if(mView == null){
            return;
        }
        mView.startLoading();
        model.submitReviewReasonToService(orderUuid, orderItemUuid, rating, reason, photos, new BaseModelResponeListener<String>() {
            @Override
            public void onSuccess(String data) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.postReviewReasonSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.postReviewReasonFail(msg);
            }
        });
    }

}
