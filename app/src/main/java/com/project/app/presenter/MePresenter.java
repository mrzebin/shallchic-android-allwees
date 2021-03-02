package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseUserInfo;
import com.hb.basemodel.utils.SPManager;
import com.project.app.base.BasePresenter;
import com.project.app.bean.MeBindCPBean;
import com.project.app.contract.MeContract;
import com.project.app.model.MeModel;

public class MePresenter extends BasePresenter<MeContract.View> implements MeContract.Presenter {
    private final MeModel model;
    public MePresenter(){
        model = new MeModel();
    }

    @Override
    public void fetchBindCashInfo(){
        model.fetchBindCashInfo(new BaseModelResponeListener<MeBindCPBean>() {
            @Override
            public void onSuccess(MeBindCPBean data) {
                if(mView == null){
                    return;
                }
                mView.fetchCashSuccess(data);
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

    @Override
    public void fetchUserInfo() {
        String accessToken = SPManager.sGetString("access_token");
        model.fetchUserInfo(accessToken, new BaseModelResponeListener<BaseUserInfo>() {
            @Override
            public void onSuccess(BaseUserInfo data) {
                if(mView == null){
                    return;
                }
                mView.fetchUserInfo(data);
            }
            @Override
            public void onFail(String msg) {
//                mView.fetchFail(msg);
            }
        });
    }
}
