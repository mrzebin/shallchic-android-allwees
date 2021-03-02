package com.project.app.presenter;


import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.CartBuyDataBean;
import com.project.app.bean.GoodsDetailInfoBean;
import com.project.app.bean.HomeFLashSaleBean;
import com.project.app.contract.FlashSaleContract;
import com.project.app.model.FlashSaleModel;

public class FlashSalePresenter extends BasePresenter<FlashSaleContract.View> implements FlashSaleContract.Presenter {

    private final FlashSaleContract.Model model;

    public FlashSalePresenter(){
        model = new FlashSaleModel();
    }

    @Override
    public void fetchCartData() {
        model.fetchCartData(new BaseModelResponeListener<CartBuyDataBean>() {
            @Override
            public void onSuccess(CartBuyDataBean data) {
                if(mView == null){
                    return;
                }
                mView.fetchBuyDataSuccess(data);
            }
            @Override
            public void onFail(String msg) {

            }
        });
    }

    @Override
    public void fetchFSGoodsDetail(String uuid) {
        if(mView == null){
            return;
        }
        mView.startLoading();
        model.fetchFSGoodsDetail(uuid, new BaseModelResponeListener<GoodsDetailInfoBean>() {
            @Override
            public void onSuccess(GoodsDetailInfoBean response) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchGoodsInfoSuccess(response);
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

    @Override
    public void fetchFlashSale(int type,int page, int size) {
        if(mView == null){
            return;
        }
        mView.startLoading();
        model.fetchFlashSale(type,page, size, new BaseModelResponeListener<HomeFLashSaleBean>() {
            @Override
            public void onSuccess(HomeFLashSaleBean data) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchFlashSaleSuccess(data);
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

    @Override
    public void operationAddGoods(int count, boolean incr, String skuUuid) {
        model.operationAddGoods(count, incr, skuUuid, new BaseModelResponeListener() {
            @Override
            public void onSuccess(Object data) {
                if(mView == null){
                    return;
                }
                mView.addCartSuccess(data.toString());
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.fetchFail(msg);
            }
        });
    }
}
