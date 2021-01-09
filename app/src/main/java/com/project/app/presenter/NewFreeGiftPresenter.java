package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.FreeGiftCouponBean;
import com.project.app.bean.HomeFreeGiftBean;
import com.project.app.bean.MarketGiftInfoBean;
import com.project.app.contract.NewFreeGiftContract;
import com.project.app.model.NewFreeGiftModel;

public class NewFreeGiftPresenter extends BasePresenter<NewFreeGiftContract.View> implements NewFreeGiftContract.Presenter {
    private final NewFreeGiftModel model;

    public NewFreeGiftPresenter(){
        model = new NewFreeGiftModel();
    }

    @Override
    public void fetchFreeGiftStatus() {
        model.fetchFreeGiftStatus(new BaseModelResponeListener<MarketGiftInfoBean>() {
            @Override
            public void onSuccess(MarketGiftInfoBean data) {
                mView.fetchFreeGiftsStatusSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void fetchFreeGiftsCouponStatus() {
        model.fetchFreeGiftsCouponStatus(new BaseModelResponeListener<FreeGiftCouponBean>() {
            @Override
            public void onSuccess(FreeGiftCouponBean data) {
                mView.fetchFreeGiftsCouponSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void fetchFreeGiftsList() {
        model.fetchFreeGiftsList(new BaseModelResponeListener<HomeFreeGiftBean>() {
            @Override
            public void onSuccess(HomeFreeGiftBean data) {
                mView.fetchGiftsList(data);
            }

            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void operationAddGoods(int count, boolean incr, String skuUuid) {
        model.operationAddGoods(count, incr, skuUuid, new BaseModelResponeListener() {
            @Override
            public void onSuccess(Object data) {
                mView.addCartSuccess(data.toString());
            }

            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
            }
        });
    }
}
