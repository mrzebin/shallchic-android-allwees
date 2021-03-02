package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.ClassifyListBean;
import com.project.app.bean.OrderDetailBean;

public interface PayStatusContract {

    interface Model{
        void fetchCLikeList(int currentPage, int pagetSize, BaseModelResponeListener listener);
        void fetchOrderCashBackAmt(String orderNo, BaseModelResponeListener listener);   //获取订单返利
    }

    interface View extends BaseView {
        void fetchMightLikeData(ClassifyListBean result);
        void fetchOrderCashBackAmtSuccess(OrderDetailBean result);
        void fetchFail(String msg);
    }

    interface Presenter{
        void fetchCLikeList(int currentPage,int pageSize);
        void fetchOrderCashBackAmt(String orderNo);   //获取订单返利
    }


}
