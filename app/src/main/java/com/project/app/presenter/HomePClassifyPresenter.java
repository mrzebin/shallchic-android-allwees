package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.ClassifyListBean;
import com.project.app.bean.HomePopularLastOrderBean;
import com.project.app.contract.HomePClassifyContract;
import com.project.app.model.HomePClassifyModel;
import com.project.app.net.NetStateUtils;

public class HomePClassifyPresenter extends BasePresenter<HomePClassifyContract.View> implements HomePClassifyContract.Presenter {
    private final HomePClassifyModel model;

    public HomePClassifyPresenter(){
        model = new HomePClassifyModel();
    }

    @Override
    public void fetchLastOrderInfo() {
        if(!NetStateUtils.isHasNet()){
            if(mView == null){
                return;
            }
            mView.stopLoading();
            mView.fetchNetWorkState(false);
            return;
        }
        model.fetchLastOrderInfo(new BaseModelResponeListener<HomePopularLastOrderBean>() {
            @Override
            public void onSuccess(HomePopularLastOrderBean data) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchLastOrdersSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.stopLoading();
            }
        });
    }

    @Override
    public void fetchGoodsInfo(int page,int size,String no) {
        if(!NetStateUtils.isHasNet()){
            if(mView == null){
                return;
            }
            mView.stopLoading();
            mView.fetchNetWorkState(false);
            return;
        }
        model.fetchGoodsInfo(page,size,no, new BaseModelResponeListener<ClassifyListBean>() {
            @Override
            public void onSuccess(ClassifyListBean data) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchCategoryList(data);
            }
            @Override
            public void onFail(String msg) {
                mView.stopLoading();
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void fetchHomePopularMax() {
        if(!NetStateUtils.isHasNet()){
            if(mView == null){
                return;
            }
            mView.stopLoading();
            mView.fetchNetWorkState(false);
            return;
        }
        model.fetchHomePopularMax(new BaseModelResponeListener<String>() {
            @Override
            public void onSuccess(String data) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.checkEmptyView();
                mView.fetchHomePopularMaxSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.checkEmptyView();
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void fetchHomeAction() {
        model.fetchHomeAction(new BaseModelResponeListener<String>() {
            @Override
            public void onSuccess(String data) {
                if(mView == null){
                    return;
                }
                mView.checkEmptyView();
                mView.fetchHomeActionMaxSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.checkEmptyView();
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void onDestoryView() {
        if(mView != null){
            mView = null;
            System.gc();    //通知系统有空余功夫就回收资源
        }
    }
}
