package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.AppLocaleBean;
import com.project.app.bean.CountryCropBean;
import com.project.app.contract.SwitchLanguateContract;
import com.project.app.model.SwitchLanguateModel;

public class SwtichLanguatePresenter extends BasePresenter<SwitchLanguateContract.View> implements SwitchLanguateContract.Presenter {
    private final SwitchLanguateModel model;

    public SwtichLanguatePresenter(){
        model = new SwitchLanguateModel();
    }

    @Override
    public void fetchCountryList() {
        mView.startLoading();
        model.fetchCountryList(new BaseModelResponeListener<CountryCropBean>() {
            @Override
            public void onSuccess(CountryCropBean data) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchCountryListSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchCountryListFail(msg);
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
