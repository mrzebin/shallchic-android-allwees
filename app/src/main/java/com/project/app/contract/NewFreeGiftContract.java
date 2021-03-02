package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.FreeGiftCouponBean;
import com.project.app.bean.HomeFreeGiftBean;
import com.project.app.bean.MarketGiftInfoBean;

public interface NewFreeGiftContract {

    interface Model{
        void fetchFreeGiftStatus(BaseModelResponeListener listener);
        void fetchFreeGiftsCouponStatus(BaseModelResponeListener listener);
        void fetchFreeGiftsList(BaseModelResponeListener listener);
        void operationAddGoods(int count, boolean incr, String skuUuid, BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchFreeGiftsCouponSuccess(FreeGiftCouponBean result);
        void fetchFreeGiftsStatusSuccess(MarketGiftInfoBean result);
        void fetchGiftsList(HomeFreeGiftBean result);
        void addCartSuccess(String msg);
        void fetchFail(String msg);
    }

    interface Presenter{
        void fetchFreeGiftStatus();
        void fetchFreeGiftsCouponStatus();
        void fetchFreeGiftsList();
        void operationAddGoods(int count,boolean incr,String skuUuid);
    }

}
