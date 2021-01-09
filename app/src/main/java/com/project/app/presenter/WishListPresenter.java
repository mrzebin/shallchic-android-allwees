package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.WishDataBean;
import com.project.app.contract.WishlistContract;
import com.project.app.model.WishListModel;
import com.project.app.net.NetStateUtils;

public class WishListPresenter extends BasePresenter<WishlistContract.View> implements WishlistContract.Presenter {
    private final WishListModel model;

    public WishListPresenter(){
        model = new WishListModel();
    }

    @Override
    public void fetchWishList(int page, int pageSize) {
        if(!NetStateUtils.isHasNet()){
            mView.fetchNetWorkState(false);
            return;
        }
        model.fetchWishList(page,pageSize,new BaseModelResponeListener<WishDataBean>() {
            @Override
            public void onSuccess(WishDataBean data) {
                mView.fetchSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
            }
        });
    }
}
