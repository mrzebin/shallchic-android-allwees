package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.AddressBean;
import com.project.app.bean.CartBuyDataBean;
import com.project.app.bean.CartCouponsBaean;
import com.project.app.bean.CartItemReqBean;
import com.project.app.bean.CategoryBean;
import com.project.app.bean.ChionWrapperBean;
import com.project.app.bean.ClassifyListBean;
import com.project.app.bean.PayOrderBean;

public interface CartContract {

    interface Model{
        void fetchCouponList(BaseModelResponeListener listener);
        void fetchCLikeList(int currentPage, int pagetSize, BaseModelResponeListener listener);
        void fetchCartData(BaseModelResponeListener listener);
        void applyCouponCode(String promoCode, BaseModelResponeListener listener);
        void deleteItemBuyGoods(String itemUuid, String skuUuid, String type, BaseModelResponeListener listener);
        void requestOrderPay(CartItemReqBean reqBean, BaseModelResponeListener listener);
        void fetchAddressList(BaseModelResponeListener listener);
        void balanceAmtByCash(Double cash, BaseModelResponeListener listener);
        void goPayPal(String orderUuid, BaseModelResponeListener listener);
        void modifyBuyCount(int count, boolean incr, String itemUuid, String skuUuid, BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchBuyDataFail(String msg);
        void fetchBuyDataSuccess(CartBuyDataBean result);
        void fetchMightLikeData(ClassifyListBean result);
        void fetchSuccess(CategoryBean result);
        void fetchFail(String failReason);
        void deleteSuccess(CartBuyDataBean result);
        void deleteFail(String result);
        void fetchAddressSuccess(AddressBean result);
        void doCashSuccess(String result);
        void fetchCreateOrderSuccess(PayOrderBean result);
        void fetchWebPayH5(ChionWrapperBean result);
        void modifyBuyCountSuccess(CartBuyDataBean result);
        void modifyBuyCountFail(String result);
        void fetchCouponListSuccess(CartCouponsBaean result);
        void fetchApplyCouponSuccess(CartBuyDataBean result);
    }

    interface Presenter{
        void fetchCartData();
        void applyCouponCode(String promoCode);
        void fetchCLikeList(int currentPage,int pageSize);
        void deleteItemBuyGoods(String itemUuid,String skuUuid,String type);
        void requestOrderPay(CartItemReqBean reqBean);
        void fetchAddressList();
        void balanceAmtByCash(Double cash);
        void modifyBuyCount(int count,boolean incr,String itemUuid,String skuUuid);
        void fetchCouponList();
    }


}
