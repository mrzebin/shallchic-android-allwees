package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.CartCouponsBaean;
import com.project.app.contract.CartCouponsDialogContract;
import com.project.app.model.CartCouponDialogModel;
import com.project.app.net.NetStateUtils;

public class CartCouponDialogPresenter extends BasePresenter<CartCouponsDialogContract.View> implements CartCouponsDialogContract.Presenter {
    private final CartCouponDialogModel model;

    public CartCouponDialogPresenter(){
        model = new CartCouponDialogModel();
    }

    @Override
    public void fetchCouponList() {
        if(!NetStateUtils.isHasNet()){
            mView.fetchNetError();
            return;
        }
        if(mView == null){
            return;
        }
        model.fetchCouponList(new BaseModelResponeListener<CartCouponsBaean>() {
            @Override
            public void onSuccess(CartCouponsBaean data) {
                if(mView == null){
                    return;
                }
                mView.fetchCouponListSuccess(data);
            }

            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.fetchCouponListFail(msg);
            }
        });
    }
}
