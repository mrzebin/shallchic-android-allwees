package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.MeBindCPBean;
import com.project.app.bean.RedeemBean;
import com.project.app.contract.RedeemContract;
import com.project.app.model.RedeemModel;

public class RedeemPresenter extends BasePresenter<RedeemContract.View> implements RedeemContract.Presenter {
    private final RedeemContract.Model model;

    public RedeemPresenter(){
        model = new RedeemModel();
    }

    @Override
    public void upUsePointChangeCoupon(String cpUuid) {
        model.upUsePointChangeCoupon(cpUuid, new BaseModelResponeListener() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    @Override
    public void fetchRedeemInfo(int page,int pageSize) {
        model.fetchRedeemInfo(page,pageSize,new BaseModelResponeListener<RedeemBean>() {
            @Override
            public void onSuccess(RedeemBean data) {
                if(mView == null){
                    return;
                }
                mView.fetchRedeemInfoSuccess(data);
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
    public void fetchBindPointInfo() {
        model.fetchBindPointInfo(new BaseModelResponeListener<MeBindCPBean>() {
            @Override
            public void onSuccess(MeBindCPBean data) {
                if(mView == null){
                    return;
                }
                mView.fetchPointSuccess(data);
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
}
