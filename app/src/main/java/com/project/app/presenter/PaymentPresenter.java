package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.ChionWrapperBean;
import com.project.app.bean.PayOrderBean;
import com.project.app.bean.PaymentCheckBean;
import com.project.app.contract.PaymentContract;
import com.project.app.model.PaymentModel;

public class PaymentPresenter extends BasePresenter<PaymentContract.View> implements PaymentContract.Presenter {
    private final PaymentModel model;

    public PaymentPresenter(){
        model = new PaymentModel();
    }

    @Override
    public void checkCod(String orderUuid) {
        model.checkCod(orderUuid,new BaseModelResponeListener<PaymentCheckBean>() {
            @Override
            public void onSuccess(PaymentCheckBean data) {
                mView.fetchCheckCodSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.fetchCodPayFail(msg);
            }
        });
    }

    @Override
    public void smsVerify(String orderUuid, String phoneNum) {
        model.smsVerify(orderUuid, phoneNum, new BaseModelResponeListener<String>() {
            @Override
            public void onSuccess(String data) {
                mView.fetchSmsVerfySuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.fetchSmsVerfyFail(msg);
            }
        });
    }

    @Override
    public void codPay(String orderUuid, String phone, String code) {
        mView.startLoading();
        model.codPay(orderUuid, phone, code, new BaseModelResponeListener<PayOrderBean>() {
            @Override
            public void onSuccess(PayOrderBean data) {
                mView.stopLoading();
                mView.fetchCodPaySuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.stopLoading();
                mView.fetchCodPayFail(msg);
            }
        });
    }

    @Override
    public void creditCardPay(String orderUuid) {
        mView.startLoading();
        model.creditCardPay(orderUuid, new BaseModelResponeListener<String>() {
            @Override
            public void onSuccess(String data) {
                mView.stopLoading();
                mView.fetchCreditPaySuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.stopLoading();
                mView.fetchCreditPayFail(msg);
            }
        });
    }

    @Override
    public void goPayPal(String orderUuid) {
        mView.startLoading();
        model.goPayPal(orderUuid, new BaseModelResponeListener<ChionWrapperBean>() {
            @Override
            public void onSuccess(ChionWrapperBean data) {
                mView.stopLoading();
                mView.fetchPapalH5(data);
            }
            @Override
            public void onFail(String msg) {
                mView.stopLoading();
                mView.fetchFail(msg);
            }
        });
    }
}
