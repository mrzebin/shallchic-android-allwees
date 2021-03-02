package com.project.app.presenter;


import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.AppUpdateBean;
import com.project.app.bean.CartBuyDataBean;
import com.project.app.bean.CategoryBean;
import com.project.app.bean.CountryCropBean;
import com.project.app.bean.MarketGiftInfoBean;
import com.project.app.contract.HomeContract;
import com.project.app.model.HomeModel;
import com.project.app.net.NetStateUtils;

public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter{
    private final HomeModel model;

    public HomePresenter(){
        model = new HomeModel();
    }

    @Override
    public void fetchFreeGiftStatus() {
        model.fetchFreeGiftStatus(new BaseModelResponeListener<MarketGiftInfoBean>() {
            @Override
            public void onSuccess(MarketGiftInfoBean data) {
                if(mView == null){
                    return;
                }
                mView.fetchFreeGiftsStatusSuccess(data);
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
    public void fetchCountryList() {
        model.fetchCountryList(new BaseModelResponeListener<CountryCropBean>() {
            @Override
            public void onSuccess(CountryCropBean data) {
                if(mView == null){
                    return;
                }
                mView.fetchCountrysSuccess(data);
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
    public void checkAppersion() {
        model.checkAppersion(new BaseModelResponeListener<AppUpdateBean>() {
            @Override
            public void onSuccess(AppUpdateBean data) {
                if(mView == null){
                    return;
                }
                mView.fetchAppVersionSuccess(data);
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
    public void fetchCartData() {
        model.fetchCartData(new BaseModelResponeListener<CartBuyDataBean>() {
            @Override
            public void onSuccess(CartBuyDataBean data) {
                if(mView == null){
                    return;
                }
                mView.fetchUserCartData(data);
            }
            @Override
            public void onFail(String msg) {
            }
        });
    }

    @Override
    public void fetchContactInfo() {
        model.fetchContactInfo(new BaseModelResponeListener<String>() {
            @Override
            public void onSuccess(String data) {
                if(mView == null){
                    return;
                }
                mView.fetchContackInfoSuccess(data);
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
    public void bindMiuiPushId(String registId, boolean isBind) {
        model.bindMiuiRegistId(registId, isBind, new BaseModelResponeListener() {
            @Override
            public void onSuccess(Object data) {

            }
            @Override
            public void onFail(String msg) {

            }
        });
    }

    @Override
    public void fetchHomePopularTitles() {
        if(!NetStateUtils.isHasNet()){
            return;
        }
        model.fetchHomePopularTitles(new BaseModelResponeListener<CategoryBean>() {
            @Override
            public void onSuccess(CategoryBean data) {
                if(mView == null){
                    return;
                }
                mView.fetchHomePopularTitlesSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                
            }
        });
    }


}
