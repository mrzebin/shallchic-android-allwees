package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.MeBindCPBean;
import com.project.app.bean.ScCashBean;
import com.project.app.contract.ScContract;
import com.project.app.model.ScModel;

public class ShareCashPresenter extends BasePresenter<ScContract.View> implements ScContract.Presenter {
    private final ScContract.Model model;

    public ShareCashPresenter(){
        model = new ScModel();
    }

    @Override
    public void fetchScHistory(int current, int size) {
        model.fetchScHistory(current,size,new BaseModelResponeListener<ScCashBean>() {
            @Override
            public void onSuccess(ScCashBean data) {
                if(mView == null){
                    return;
                }
                mView.fetchSuccess(data);
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
    public void fetchBindCashInfo() {
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
}
