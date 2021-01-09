package com.project.app.presenter;


import com.project.app.base.BasePresenter;
import com.project.app.contract.BaiduContract;
import com.project.app.model.BaiduModel;

public class BaiduPresenter extends BasePresenter<BaiduContract.View> implements BaiduContract.Presenter {

    private final BaiduContract.Model model;

    public BaiduPresenter(){
        model = new BaiduModel();
    }

    @Override
    public void login(String name, String passowrd) {
        if(mView == null){
            return;
        }
        mView.startLoading();
        model.login(name, passowrd, new BaiduContract.Model.ModelListener() {
            @Override
            public void complete(String result) {
                mView.stopLoading();
                mView.loginSuccess(result);
            }

            @Override
            public void fail(String result) {
                mView.stopLoading();
                mView.loginFail(result);
            }
        });
    }
}
