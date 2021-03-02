package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.MeBindCPBean;
import com.project.app.bean.RedeemBean;
import com.project.app.contract.RewardContract;
import com.project.app.model.RewardModel;

public class RewardPresenter extends BasePresenter<RewardContract.View> implements RewardContract.Presenter {
    private final RewardContract.Model model;

    public RewardPresenter(){
        model = new RewardModel();
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
