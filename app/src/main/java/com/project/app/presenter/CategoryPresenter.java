package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.CategoryBean;
import com.project.app.contract.CategoryContract;
import com.project.app.model.CategoryModel;
import com.project.app.net.NetStateUtils;

public class CategoryPresenter extends BasePresenter<CategoryContract.View> implements CategoryContract.Presenter {
    private final CategoryContract.Model model;

    public CategoryPresenter(){
        model = new CategoryModel();
    }

    @Override
    public void fetchGoodsInfo(String platform,String device,String version) {
        if(!NetStateUtils.isHasNet()){
            mView.fetchNetWorkState(false);
            return;
        }

        model.fetchGoodsInfo(platform, device, version, new BaseModelResponeListener<CategoryBean>() {
            @Override
            public void onSuccess(CategoryBean data) {
                mView.fetchSuccess(data);
            }

            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
            }
        });
    }
}
