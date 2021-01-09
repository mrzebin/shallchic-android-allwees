package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.AddMoreListBean;
import com.project.app.contract.AddMoreGoodsContract;
import com.project.app.model.AddMoreGoodsModel;

public class AddMoreGoodsPresenter extends BasePresenter<AddMoreGoodsContract.View> implements AddMoreGoodsContract.Presenter {
    private final AddMoreGoodsContract.Model model;

    public AddMoreGoodsPresenter(){
        model = new AddMoreGoodsModel();
    }

    @Override
    public void fetchGoodsList(String categoryNo, int currentPage, int pagetSize) {
        model.fetchGoodsList(categoryNo, currentPage, pagetSize, new BaseModelResponeListener<AddMoreListBean>() {
            @Override
            public void onSuccess(AddMoreListBean data) {
               mView.stopLoading();
               mView.fetchSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.stopLoading();
            }
        });
    }
}
