package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.LogisticTrackBean;
import com.project.app.bean.OrderDetailBean;
import com.project.app.contract.OrderDetailContract;
import com.project.app.model.OrderDetailMoel;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderDetailPresenter extends BasePresenter<OrderDetailContract.View> implements OrderDetailContract.Presenter {
    private final OrderDetailContract.Model model;

    public OrderDetailPresenter(){
        model = new OrderDetailMoel();
    }

    @Override
    public void fetchDetailData(String orderNo) {
        if(mView == null){
            return;
        }
        mView.startLoading();
        model.fetchDetailData(orderNo,new BaseModelResponeListener<OrderDetailBean>() {
            @Override
            public void onSuccess(OrderDetailBean data) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchDetailSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchDetailFail(msg);
            }
        });
    }

    @Override
    public void checkLogisticTrack(String trackUuid) {
        model.checkLogisticTrack(trackUuid, new BaseModelResponeListener<LogisticTrackBean>() {
            @Override
            public void onSuccess(LogisticTrackBean data) {
                if(mView == null){
                    return;
                }
                mView.fetchLogisticTSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.fetchLogisticTFail(msg);
            }
        });
    }

    @Override
    public void receiveGoodsRequest(String orderId) {
        model.receiveGoodsRequest(orderId, new BaseModelResponeListener<String>() {
            @Override
            public void onSuccess(String data) {
                if(mView == null){
                    return;
                }
                try {
                    JSONObject object = new JSONObject(data);
                    String msg = object.getString("msg");
                    int code = object.getInt("code");
                    if(code == 1){
                        mView.refundSuccess();
                    }else{
                        mView.refundFail(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.refundFail(msg);
            }
        });
    }
}
