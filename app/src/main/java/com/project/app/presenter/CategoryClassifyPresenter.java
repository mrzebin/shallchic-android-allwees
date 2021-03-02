package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.ClassifyListBean;
import com.project.app.contract.CategoryClassifyContract;
import com.project.app.model.CategoryClassifyModel;
import com.project.app.net.NetStateUtils;

public class CategoryClassifyPresenter extends BasePresenter<CategoryClassifyContract.View> implements CategoryClassifyContract.Presenter {
    private final CategoryClassifyModel model;

    public CategoryClassifyPresenter(){
        model = new CategoryClassifyModel();
    }

    @Override
    public void fetchGoodsInfo(boolean showLoading,int page,int size,String no) {
        if(!NetStateUtils.isHasNet()){
            mView.fetchNetWorkState(false);
            return;
        }
        if(mView == null){
            return;
        }
        if(showLoading){
            mView.startLoading();
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
            }
        });
    }
}
