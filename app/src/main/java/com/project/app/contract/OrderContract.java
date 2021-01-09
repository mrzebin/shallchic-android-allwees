package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.OrderBean;

import java.util.HashMap;

public interface OrderContract {

    interface Model{
        void fetchOrderList(HashMap<String,String> params, BaseModelResponeListener listener);
    }

    interface View extends BaseView{
        void fetchOrderListSuccess(OrderBean result);
        void fetchOrderListFail(String msg);
    }

    interface Presenter{
       void fetchOrderList(int current,int pageSize,String queryType);
    }


}
