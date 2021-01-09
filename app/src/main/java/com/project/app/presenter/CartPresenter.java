package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.AddressBean;
import com.project.app.bean.CartBuyDataBean;
import com.project.app.bean.CartCouponsBaean;
import com.project.app.bean.CartItemReqBean;
import com.project.app.bean.ChionWrapperBean;
import com.project.app.bean.ClassifyListBean;
import com.project.app.bean.PayOrderBean;
import com.project.app.contract.CartContract;
import com.project.app.model.CartModel;

public class CartPresenter extends BasePresenter<CartContract.View> implements CartContract.Presenter {
    private final CartContract.Model model;

    public CartPresenter(){
        model = new CartModel();
    }

    @Override
    public void fetchCouponList() {
        model.fetchCouponList(new BaseModelResponeListener<CartCouponsBaean>() {
            @Override
            public void onSuccess(CartCouponsBaean data) {
                mView.fetchCouponListSuccess(data);
            }

            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void applyCouponCode(String promoCode) {
        mView.startLoading();
        model.applyCouponCode(promoCode, new BaseModelResponeListener<CartBuyDataBean>() {
            @Override
            public void onSuccess(CartBuyDataBean data) {
                mView.stopLoading();
                mView.fetchApplyCouponSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.stopLoading();
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void fetchCLikeList(int currentPage, int pageSize) {
        model.fetchCLikeList(currentPage, pageSize, new BaseModelResponeListener<ClassifyListBean>() {
            @Override
            public void onSuccess(ClassifyListBean data) {
                mView.fetchMightLikeData(data);
                mView.stopLoading();
            }
            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
                mView.stopLoading();
            }
        });
    }

    @Override
    public void deleteItemBuyGoods(String itemUuid, String skuUuid, String type) {
        model.deleteItemBuyGoods(itemUuid, skuUuid, type, new BaseModelResponeListener<CartBuyDataBean>() {
            @Override
            public void onSuccess(CartBuyDataBean data) {
                mView.deleteSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.deleteFail(msg);
            }
        });
    }

    @Override
    public void requestOrderPay(CartItemReqBean reqBean) {
        mView.startLoading();
        model.requestOrderPay(reqBean,new BaseModelResponeListener<PayOrderBean>() {
            @Override
            public void onSuccess(PayOrderBean data) {
                mView.stopLoading();
                mView.fetchCreateOrderSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.stopLoading();
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void fetchCartData() {
        model.fetchCartData(new BaseModelResponeListener<CartBuyDataBean>() {
            @Override
            public void onSuccess(CartBuyDataBean data) {
                mView.fetchBuyData(data);
                mView.stopLoading();
            }
            @Override
            public void onFail(String msg) {
                mView.fetchBuyDataFail(msg);
                mView.stopLoading();
            }
        });
    }

    @Override
    public void fetchAddressList() {
        model.fetchAddressList(new BaseModelResponeListener<AddressBean>() {
            @Override
            public void onSuccess(AddressBean data) {
                mView.fetchAddressSuccess(data);
            }

            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void balanceAmtByCash(Double cash) {
        model.balanceAmtByCash(cash,new BaseModelResponeListener<CartBuyDataBean>() {
            @Override
            public void onSuccess(CartBuyDataBean data) {
                mView.stopLoading();
                mView.fetchBuyData(data);
            }
            @Override
            public void onFail(String msg) {
                mView.stopLoading();
            }
        });
    }

    @Override
    public void goPayPal(String orderUuid) {
        model.goPayPal(orderUuid, new BaseModelResponeListener<ChionWrapperBean>() {
            @Override
            public void onSuccess(ChionWrapperBean data) {
                mView.fetchWebPayH5(data);
            }
            @Override
            public void onFail(String msg) {
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void modifyBuyCount(int count, boolean incr, String skuUuid, String itemUuid) {
        mView.startLoading();
        model.modifyBuyCount(count, incr, skuUuid, itemUuid, new BaseModelResponeListener<CartBuyDataBean>() {
            @Override
            public void onSuccess(CartBuyDataBean data) {
                mView.stopLoading();
                mView.modifyBuyCountSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.stopLoading();
                mView.modifyBuyCountFail(msg);
            }
        });
    }

}
