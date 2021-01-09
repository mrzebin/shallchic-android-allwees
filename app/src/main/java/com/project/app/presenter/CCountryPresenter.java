package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.CountryCropBean;
import com.project.app.contract.CCountryContract;
import com.project.app.model.CCountryModel;

public class CCountryPresenter extends BasePresenter<CCountryContract.View> implements CCountryContract.Presenter {
    private final CCountryContract.Model model;

    public CCountryPresenter(){
        model = new CCountryModel();
    }

    @Override
    public void fetchCountryRList() {
        mView.startLoading();
        model.fetchCountryRList(new BaseModelResponeListener<CountryCropBean>() {
            @Override
            public void onSuccess(CountryCropBean data) {
                mView.stopLoading();
                mView.fetchSuccess(data);
            }

            @Override
            public void onFail(String msg) {
                mView.stopLoading();
                mView.fetchFail(msg);
            }
        });
    }
}
