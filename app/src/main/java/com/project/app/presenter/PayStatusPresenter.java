package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.ClassifyListBean;
import com.project.app.bean.OrderDetailBean;
import com.project.app.contract.PayStatusContract;
import com.project.app.model.PayStatusModel;

public class PayStatusPresenter extends BasePresenter<PayStatusContract.View> implements PayStatusContract.Presenter {
    private final PayStatusContract.Model model;

    public PayStatusPresenter(){
        model = new PayStatusModel();
    }

    @Override
    public void fetchCLikeList(int currentPage, int pageSize) {
        model.fetchCLikeList(currentPage, pageSize, new BaseModelResponeListener<ClassifyListBean>() {
            @Override
            public void onSuccess(ClassifyListBean data) {
                if(mView == null){
                    return;
                }
                mView.fetchMightLikeData(data);
                mView.stopLoading();
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void fetchOrderCashBackAmt(String orderNo) {
        if(mView == null){
            return;
        }
        mView.startLoading();
        model.fetchOrderCashBackAmt(orderNo,new BaseModelResponeListener<OrderDetailBean>() {
            @Override
            public void onSuccess(OrderDetailBean data) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchOrderCashBackAmtSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchFail(msg);
            }
        });
    }


}
