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
        if(!NetStateUtils.isHasNet()){
            mView.fetchNetWorkState(false);
            return;
        }

        if(mView != null && page == 1){
            mView.startLoading();
        }
        model.fetchGoodsInfo(page,size,no,new BaseModelResponeListener<ClassifyListBean>() {
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
}
