package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.contract.PushMiuiContract;
import com.project.app.model.PushMiuiModel;

public class PushMiuiPresenter extends BasePresenter<PushMiuiContract.View> implements PushMiuiContract.Presenter {
    private final PushMiuiModel model;

    public PushMiuiPresenter(){
        model = new PushMiuiModel();
    }

    @Override
    public void bindMiuiPushId(String registId, boolean isBind) {
        model.bindMiuiRegistId(registId, isBind, new BaseModelResponeListener() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onFail(String msg) {

            }
        });
    }
}
