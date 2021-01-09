package com.project.app.presenter;


import com.project.app.base.BasePresenter;
import com.project.app.contract.BaiduContract;
import com.project.app.contract.LocationContract;
import com.project.app.model.BaiduModel;

public class LocationPresenter extends BasePresenter<LocationContract.View> implements LocationContract.Presenter {
    private final BaiduContract.Model model;

    public LocationPresenter(){
        model = new BaiduModel();
    }

    @Override
    public void login(String name, String passowrd) {
        if(mView == null){
            return;
        }
        mView.startLoading();

    }
}
