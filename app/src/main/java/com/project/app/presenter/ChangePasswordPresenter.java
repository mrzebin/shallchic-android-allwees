package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.contract.ChangePasswordContract;
import com.project.app.model.ChangePasswordModel;

public class ChangePasswordPresenter extends BasePresenter<ChangePasswordContract.View> implements ChangePasswordContract.Presenter {
    private final ChangePasswordModel model;

    public ChangePasswordPresenter(){
        model = new ChangePasswordModel();
    }

    @Override
    public void requestChangePassowrd(String originPs, String newPs) {
        model.requestChangePassowrd(originPs, newPs, new BaseModelResponeListener<String>() {
            @Override
            public void onSuccess(String data) {
                if(mView == null){
                    return;
                }
                mView.fetchChangePassowrdSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.fetchChangePassowrdFail(msg);
            }
        });
    }
}
