package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.project.app.base.BasePresenter;
import com.project.app.bean.GoodsDetailInfoBean;
import com.project.app.bean.GoodsRelationBean;
import com.project.app.contract.GoodsDetailContract;
import com.project.app.model.GoodsDetailModel;

import java.util.List;

public class GoodsDetailPresenter extends BasePresenter<GoodsDetailContract.View> implements GoodsDetailContract.Presenter{
    private final GoodsDetailModel model;
    public GoodsDetailPresenter(){
        model = new GoodsDetailModel();
    }

    @Override
    public void fetchCoverList(String uuid) {
        String url = BaseUrlConfig.getRootHost() + UrlConfig.GOODS_DETAILS_URL + uuid;
        mView.startLoading();
        model.fetchCoverList(url, new BaseModelResponeListener<GoodsDetailInfoBean>() {
            @Override
            public void onSuccess(GoodsDetailInfoBean response) {
                mView.stopLoading();
                mView.fetchCoverSuccess(response);
            }
            @Override
            public void onFail(String msg) {
                mView.stopLoading();
                mView.fetchFile(msg);
            }
        });
    }

    @Override
    public void fetchRelativeBox(String uuid) {
        String url = BaseUrlConfig.getRootHost() +   UrlConfig.GOODS_RELATION_URL + uuid;
        model.fetchRelativeBox(url, new BaseModelResponeListener<List<GoodsRelationBean>>() {
            @Override
            public void onSuccess(List<GoodsRelationBean> response) {
                mView.fetchCoverRSuccess(response);
            }
            @Override
            public void onFail(String msg) {
                mView.fetchFile(msg);
            }
        });
    }

    @Override
    public void operationFavoriteAdd(String uuid) {
        model.operationFavoriteAdd(uuid, new BaseModelResponeListener() {
            @Override
            public void onSuccess(Object data) {
                mView.refreshWishFavorite();
            }
            @Override
            public void onFail(String msg) {

            }
        });
    }

    @Override
    public void operationFavoriteCancel(String uuid) {
        model.operationFavoriteCancel(uuid, new BaseModelResponeListener() {
            @Override
            public void onSuccess(Object data) {
                mView.refreshRemoveWishFavorite();
            }
            @Override
            public void onFail(String msg) {

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
                mView.fetchFile(msg);
            }
        });
    }

}
