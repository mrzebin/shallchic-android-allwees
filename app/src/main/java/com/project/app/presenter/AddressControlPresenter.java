package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.CreateAddressBean;
import com.project.app.contract.AddressControlContract;
import com.project.app.model.AddressControlModel;

import java.util.HashMap;

public class AddressControlPresenter extends BasePresenter<AddressControlContract.View> implements AddressControlContract.Presenter {
    private final AddressControlModel model;

    public AddressControlPresenter(){
        model = new AddressControlModel();
    }

    @Override
    public void editAddress(HashMap<String, String> params) {
        model.editAddress(params, new BaseModelResponeListener<CreateAddressBean>() {
            @Override
            public void onSuccess(CreateAddressBean data) {
                if(mView == null){
                    return;
                }
                mView.editAddressResult(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.editAddreddFail(msg);
            }
        });
    }

    @Override
    public void createAddress(HashMap<String, String> params) {
        model.createAddress(params, new BaseModelResponeListener<CreateAddressBean>() {
            @Override
            public void onSuccess(CreateAddressBean data) {
                if(mView == null){
                    return;
                }
                mView.addAddressResult(data);
            }

            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.createAddressFail(msg);
            }
        });
    }

    @Override
    public void onDestoryView() {
        mView = null;
        System.gc();
    }
}
