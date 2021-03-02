package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.ClassifyListBean;
import com.project.app.contract.HomeClassifyContract;
import com.project.app.model.HomeClassifyModel;
import com.project.app.net.NetStateUtils;

public class HomeClassifyPresenter extends BasePresenter<HomeClassifyContract.View> implements HomeClassifyContract.Presenter {
    private final HomeClassifyModel model;

    public HomeClassifyPresenter(){
        model = new HomeClassifyModel();
    }

    @Override
    public void fetchGoodsInfo(int page,int size,String no) {
        if(mView == null){
            return;
        }
        if(!NetStateUtils.isHasNet()){
            mView.stopLoading();
            mView.fetchNetWorkState(false);
            return;
        }

        model.fetchGoodsInfo(page,size,no,new BaseModelResponeListener<ClassifyListBean>() {
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
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void onDestoryView() {
        mView = null;
        System.gc();
    }
}
