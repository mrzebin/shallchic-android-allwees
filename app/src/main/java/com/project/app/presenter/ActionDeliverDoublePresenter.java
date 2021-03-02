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
    public void fetchActionFreeOne(String marketType,int page,int size) {
        model.fetchActionFreeOne(marketType,page,size,new BaseModelResponeListener<ActionFreeOneBean>() {
            @Override
            public void onSuccess(ActionFreeOneBean data) {
                if(mView == null){
                    return;
                }
                if(data == null){
                    return;
                }
                mView.loadMatchViewStatus(0);
                mView.fetchActionFreeOneSuccess(data);
            }

            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.loadMatchViewStatus(1);
                mView.fetchActionFreeOneFail(msg);
            }
        });
    }

    @Override
    public void onDestoryView() {
        mView = null;
        System.gc();
    }
}
