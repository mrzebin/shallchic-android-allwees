package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.contract.LoginForgetPContract;
import com.project.app.model.LoginForgetModel;

public class LoginForgePresenter extends BasePresenter<LoginForgetPContract.View> implements LoginForgetPContract.Presenter{
    private final LoginForgetModel model;
    public LoginForgePresenter(){
        model = new LoginForgetModel();
    }

    @Override
    public void sendTargetEmailCode(String email) {
        model.sendTargetEmailCode(email,new BaseModelResponeListener<String>() {
            @Override
            public void onSuccess(String data) {
                if(mView == null){
                    return;
                }
                mView.sendSuccess(data);
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
