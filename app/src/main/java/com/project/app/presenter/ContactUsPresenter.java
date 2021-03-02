package com.project.app.presenter;


import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.contract.ContactUsContract;
import com.project.app.model.ContactUsModel;

public class ContactUsPresenter extends BasePresenter<ContactUsContract.View> implements ContactUsContract.Presenter {

    private final ContactUsModel model;

    public ContactUsPresenter(){
        model = new ContactUsModel();
    }


    @Override
    public void fetctContactUsMethods() {
        if(mView == null){
            return;
        }
        mView.startLoading();
        model.fetctContactUsMethods(new BaseModelResponeListener<String>() {
            @Override
            public void onSuccess(String data) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchContactMethodsSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchContactMethodsFail(msg);
            }
        });
    }
}
