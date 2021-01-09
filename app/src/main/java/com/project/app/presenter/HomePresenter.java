package com.project.app.presenter;


import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.AppUpdateBean;
import com.project.app.bean.CartBuyDataBean;
import com.project.app.bean.CountryCropBean;
import com.project.app.contract.HomeContract;
import com.project.app.model.HomeModel;

public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter{
    private final HomeModel model;

    public HomePresenter(){
        model = new HomeModel();
    }

    @Override
    public void fetchUploadToken(String code) {
        model.fetchUploadToken(code, new BaseModelResponeListener() {
            @Override
            public void onSuccess(Object data) {
                mView.fetchAccessUploadToken("");
            }
            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void fetchCountryList() {
        model.fetchCountryList(new BaseModelResponeListener<CountryCropBean>() {
            @Override
            public void onSuccess(CountryCropBean data) {
                mView.fetchCountrysSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void checkAppersion() {
        model.checkAppersion(new BaseModelResponeListener<AppUpdateBean>() {
            @Override
            public void onSuccess(AppUpdateBean data) {
                mView.fetchAppVersionSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void fetchCartData() {
        model.fetchCartData(new BaseModelResponeListener<CartBuyDataBean>() {
            @Override
            public void onSuccess(CartBuyDataBean data) {
                mView.fetchUserCartData(data);
            }
            @Override
            public void onFail(String msg) {
            }
        });
    }
}
