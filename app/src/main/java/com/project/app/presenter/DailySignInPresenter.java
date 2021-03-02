package com.project.app.presenter;


import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.DailySignInBean;
import com.project.app.contract.DailySignInContract;
import com.project.app.model.DailySignInModel;

public class DailySignInPresenter extends BasePresenter<DailySignInContract.View> implements DailySignInContract.Presenter {
    private DailySignInModel model;

    public DailySignInPresenter(){
        model = new DailySignInModel();
    }

    @Override
    public void fetchDailySignIn() {
        model.fetchDailySignIn(new BaseModelResponeListener<DailySignInBean>() {
            @Override
            public void onSuccess(DailySignInBean data) {
                if(mView == null){
                    return;
                }
                mView.fetchDailySignInResultSuccess(data);
            }
            @Override
            public void onFail(String msg) {

            }
        });
    }



}
