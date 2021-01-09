package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.OrderBean;
import com.project.app.contract.OrderContract;
import com.project.app.model.OrderModel;

import java.util.HashMap;

public class OrderPresenter extends BasePresenter<OrderContract.View> implements OrderContract.Presenter {
    private final OrderContract.Model model;

    public OrderPresenter(){
        model = new OrderModel();
    }

    @Override
    public void fetchOrderList(int current, int pageSize, String queryType) {
        HashMap<String,String> params = new HashMap<>();
        params.put("current",String.valueOf(current));
        params.put("pageSize",String.valueOf(pageSize));
        params.put("queryType",String.valueOf(queryType));

        model.fetchOrderList(params, new BaseModelResponeListener<OrderBean>() {
            @Override
            public void onSuccess(OrderBean data) {
                mView.fetchOrderListSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.fetchOrderListFail(msg);
            }
        });
    }
}
