package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.project.app.base.BasePresenter;
import com.project.app.bean.ActionFreeOneBean;
import com.project.app.bean.ClassifyListBean;
import com.project.app.bean.HomeBannerBean;
import com.project.app.bean.HomeFLashSaleBean;
import com.project.app.bean.HomeFreeGiftBean;
import com.project.app.contract.HomePClassifyContract;
import com.project.app.model.HomePClassifyModel;
import com.project.app.net.NetStateUtils;

public class HomePClassifyPresenter extends BasePresenter<HomePClassifyContract.View> implements HomePClassifyContract.Presenter {
    private final HomePClassifyModel model;

    public HomePClassifyPresenter(){
        model = new HomePClassifyModel();
    }

    @Override
    public void fetchFreeGift() {
        model.fetchFreeGift(new BaseModelResponeListener<HomeFreeGiftBean>() {
            @Override
            public void onSuccess(HomeFreeGiftBean data) {
                mView.fetchFreeGiftSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void fetchGoodsInfo(int page,int size,String no) {
        if(!NetStateUtils.isHasNet()){
            mView.fetchNetWorkState(false);
            return;
        }
        if(mView != null && page == 1){
            mView.startLoading();
        }
        model.fetchGoodsInfo(page,size,no, new BaseModelResponeListener<ClassifyListBean>() {
            @Override
            public void onSuccess(ClassifyListBean data) {
                mView.fetchCategoryList(data);
            }
            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void fetchBannerInfo() {
        String url = BaseUrlConfig.getRootHost() + UrlConfig.FETCH_HOME_BANNER_URL + "/1/1";

        model.fetchBannerInfo(url, new BaseModelResponeListener<HomeBannerBean>() {
            @Override
            public void onSuccess(HomeBannerBean data) {
                mView.fetchBannerSuccess(data);
            }

            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void fetchFlashSale(int page, int size) {
        model.fetchFlashSale(page, size, new BaseModelResponeListener<HomeFLashSaleBean>() {
            @Override
            public void onSuccess(HomeFLashSaleBean data) {
                mView.fetchFlashSaleSuccess(data);
            }

            @Override
            public void onFail(String msg) {
                mView.fetchFlashSaleFail(msg);
            }
        });
    }

    @Override
    public void fetchActionFreeOne() {
        model.fetchActionFreeOne(new BaseModelResponeListener<ActionFreeOneBean>() {
            @Override
            public void onSuccess(ActionFreeOneBean data) {

            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    @Override
    public void fetchHomePopularMax() {
        model.fetchHomePopularMax(new BaseModelResponeListener<String>() {
            @Override
            public void onSuccess(String data) {
                mView.fetchHomePopularMaxSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
            }
        });
    }
}
