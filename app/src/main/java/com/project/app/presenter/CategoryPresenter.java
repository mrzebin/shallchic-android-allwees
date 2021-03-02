package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.Constant;
import com.project.app.base.BasePresenter;
import com.project.app.bean.CategoryAllBean;
import com.project.app.contract.CategoryContract;
import com.project.app.model.CategoryModel;
import com.project.app.net.NetStateUtils;

public class CategoryPresenter extends BasePresenter<CategoryContract.View> implements CategoryContract.Presenter {
    private final CategoryContract.Model model;

    public CategoryPresenter(){
        model = new CategoryModel();
    }


    @Override
    public void fetchCateofyAll(String platform, String device, String version) {
        if(mView == null){
            return;
        }
        if(!NetStateUtils.isHasNet()){
            mView.stopLoading();
            mView.fetchExceptModel(Constant.EXCEPT_STATE_NETEXCEPT);
            return;
        }
        model.fetchCateofyAll(platform, device, version, new BaseModelResponeListener<CategoryAllBean>() {
            @Override
            public void onSuccess(CategoryAllBean data) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchCategoryAllSuccess(data);
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


}
