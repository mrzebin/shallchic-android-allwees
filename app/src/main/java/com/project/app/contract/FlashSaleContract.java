package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.CartBuyDataBean;
import com.project.app.bean.GoodsDetailInfoBean;
import com.project.app.bean.HomeFLashSaleBean;

public interface FlashSaleContract {

    interface Model{
        void fetchCartData(BaseModelResponeListener listener);
        void fetchFSGoodsDetail(String uuid,BaseModelResponeListener listener);
        void fetchFlashSale(int type,int page, int size, BaseModelResponeListener listener);
        void operationAddGoods(int count,boolean incr,String skuUuid,BaseModelResponeListener listener);
    }

    interface View extends BaseView{
        void fetchBuyDataSuccess(CartBuyDataBean result);
        void fetchFlashSaleSuccess(HomeFLashSaleBean result);
        void fetchGoodsInfoSuccess(GoodsDetailInfoBean result);
        void addCartSuccess(String msg);
        void fetchFail(String failReason);
    }

    interface Presenter{
        void fetchCartData();
        void fetchFSGoodsDetail(String uuid);
        void fetchFlashSale(int type,int page,int size);
        void operationAddGoods(int count,boolean incr,String skuUuid);
    }


}
