package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.LogisticTrackBean;
import com.project.app.bean.OrderDetailBean;

public interface OrderDetailContract {

    interface Model{
        void fetchDetailData(String orderNo,String orderType ,BaseModelResponeListener listener);
        void checkLogisticTrack(String trackUuid,BaseModelResponeListener listener);
        void receiveGoodsRequest(String orderId,BaseModelResponeListener listener);
    }

    interface View extends BaseView{
        void fetchDetailSuccess(OrderDetailBean result);
        void fetchDetailFail(String msg);
        void fetchLogisticTSuccess(LogisticTrackBean tracks);
        void fetchLogisticTFail(String msg);
        void refundSuccess();
        void refundFail(String result);
    }

    interface Presenter{
      void fetchDetailData(String orderNo,String orderType);
      void checkLogisticTrack(String trackUuid);
      void receiveGoodsRequest(String orderId);
    }


}
