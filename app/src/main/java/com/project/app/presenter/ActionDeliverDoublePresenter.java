package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.ActionFreeOneBean;
import com.project.app.contract.ActionDeliverDoubleContract;
import com.project.app.model.ActionDeliverDoubleModel;

public class ActionDeliverDoublePresenter extends BasePresenter<ActionDeliverDoubleContract.View> implements ActionDeliverDoubleContract.Presenter {
    private final ActionDeliverDoubleModel model;

    public ActionDeliverDoublePresenter(){
        model = new ActionDeliverDoubleModel();
    }

    @Override
    public void fetchActionFreeOne(int page,int size) {
        model.fetchActionFreeOne(page,size,new BaseModelResponeListener<ActionFreeOneBean>() {
            @Override
            public void onSuccess(ActionFreeOneBean data) {
                mView.fetchActionFreeOneSuccess(data);
            }

            @Override
            public void onFail(String msg) {
                mView.fetchActionFreeOneFail(msg);
            }
        });
    }

}
