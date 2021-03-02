package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.CartItemReqBean;
import com.project.app.bean.ChionWrapperBean;
import com.project.app.bean.PayOrderBean;
import com.project.app.bean.PaymentCheckBean;

public interface PaymentContract {

    interface Model{
        void checkCod(String orderUuid, BaseModelResponeListener listener);
        void smsVerify(String orderUuid, String phoneNum, BaseModelResponeListener listener);
        void codPay(String orderUuid, String phone, String code, BaseModelResponeListener listener);
        void creditCardPay(String orderUuid, BaseModelResponeListener listener);
        void goPayPal(String orderUuid, BaseModelResponeListener listener);
        void refreshPayOrder(CartItemReqBean reqBean, BaseModelResponeListener listener);
        void createPayOrder(CartItemReqBean reqBean, BaseModelResponeListener listener);       //创建订单
    }

    interface View extends BaseView {
        void fetchFail(String failReason);
        void fetchCheckCodFail(String result);
        void fetchCheckCodSuccess(PaymentCheckBean result);
        void fetchSmsVerfyFail(String result);
        void fetchSmsVerfySuccess(String result);
        void fetchCodPayFail(String result);
        void fetchCodPaySuccess(PayOrderBean result);
        void fetchPapalH5(ChionWrapperBean result);
        void fetchCreditPaySuccess(String result);
        void fetchCreditPayFail(String result);
        void fetchRefreshOrderSuccess(PayOrderBean result);
        void fetchCreateOrderSuccess(PayOrderBean result);
    }

    interface Presenter{
        void checkCod(String orderUuid);
        void smsVerify(String orderUuid,String phoneNum);
        void codPay(String orderUuid,String phone,String code);
        void creditCardPay(String orderUuid);  //信用卡支付
        void goPayPal(String orderUuid);  //papal支付
        void refreshPayOrder(boolean isShowLoading,CartItemReqBean reqBean);    //刷新订单数据
        void createPayOrder(CartItemReqBean reqBean);       //创建订单
    }


}
