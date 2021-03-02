package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.OrderBean;
import com.project.app.contract.OrderContract;
import com.project.app.model.OrderModel;

import org.json.JSONException;
import org.json.JSONObject;

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
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchOrderListSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchOrderListFail(msg);
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
                mView.stopLoading();
                try {
                    JSONObject object = new JSONObject(data);
                    String msg = object.getString("msg");
                    int code = object.getInt("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
            }
        });
    }

    @Override
    public void requestRepurchase(String orderId) {
        if(mView == null){
            return;
        }
        mView.startLoading();
        model.requestRepurchase(orderId, new BaseModelResponeListener<String>() {
            @Override
            public void onSuccess(String data) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.requestRepurchaseSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.requestRepurchaseFail(msg);
            }
        });
    }

}
