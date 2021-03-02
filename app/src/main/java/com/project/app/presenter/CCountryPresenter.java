package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.AppLocaleBean;
import com.project.app.bean.CountryCropBean;
import com.project.app.contract.CCountryContract;
import com.project.app.model.CCountryModel;

public class CCountryPresenter extends BasePresenter<CCountryContract.View> implements CCountryContract.Presenter {
    private final CCountryModel model;

    public CCountryPresenter(){
        model = new CCountryModel();
    }

    @Override
    public void fetchCountryRList() {
        if(mView == null){
            return;
        }
        mView.startLoading();
        model.fetchCountryRList(new BaseModelResponeListener<CountryCropBean>() {
            @Override
            public void onSuccess(CountryCropBean data) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchSuccess(data);
            }

            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void fetchLocaleInfo(String countryId) {
        model.fetchLocaleInfo(countryId,new BaseModelResponeListener<AppLocaleBean>() {
            @Override
            public void onSuccess(AppLocaleBean data) {
                if(mView == null){
                    return;
                }
                mView.fetchLocaleInofSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.fetchLocaleInfoFail(msg);
            }
        });
    }
}
