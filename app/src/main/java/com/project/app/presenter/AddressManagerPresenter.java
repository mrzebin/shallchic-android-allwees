package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.AddressBean;
import com.project.app.contract.AddressManagerContract;
import com.project.app.model.AddressMaangerModel;

public class AddressManagerPresenter extends BasePresenter<AddressManagerContract.View> implements AddressManagerContract.Presenter {
    private final AddressManagerContract.Model model;
    public AddressManagerPresenter(){
        model = new AddressMaangerModel();
    }

    @Override
    public void fetchAddressList() {
        model.fetchAddressList(new BaseModelResponeListener<AddressBean>() {
            @Override
            public void onSuccess(AddressBean data) {
                if(mView == null){
                    return;
                }
                mView.fetchAddressSuccess(data);
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
    public void deleteSpecifyAddress(String addressId) {
        if(mView == null){
            return;
        }
        mView.startLoading();
        model.deleteSpecifyAddress(addressId, new BaseModelResponeListener<String>() {
            @Override
            public void onSuccess(String data) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.deleteAddressSuccess();
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
    public void selectAddress(String addressId) {
        if(mView == null){
            return;
        }
        mView.startLoading();
        model.selectAddress(addressId, new BaseModelResponeListener<String>() {
            @Override
            public void onSuccess(String data) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.selectAddressSuccess();
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
}
