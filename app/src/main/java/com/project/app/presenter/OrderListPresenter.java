package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.HomeTopTipsBean;
import com.project.app.contract.OrderListContract;
import com.project.app.model.OrderListModel;

public class OrderListPresenter extends BasePresenter<OrderListContract.View> implements OrderListContract.Presenter {
    private final OrderListModel model;

    public OrderListPresenter(){
        model = new OrderListModel();
    }


    @Override
    public void fetchActionTopTips() {
        model.fetchActionTopTips(new BaseModelResponeListener<HomeTopTipsBean>() {
            @Override
            public void onSuccess(HomeTopTipsBean data) {
                if(mView == null){
                    return;
                }
                mView.fetchActionTopTipsSuccess(data);
            }

            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.fethhActionTopTipsFail(msg);
            }
        });
    }
}
