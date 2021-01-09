package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.GoodsDetailInfoBean;
import com.project.app.bean.GoodsRelationBean;

import java.util.List;

/**
 * 商品详情
 */
public interface GoodsDetailContract {
    interface Model{
        void fetchCoverList(String uuid, BaseModelResponeListener listener);
        void fetchRelativeBox(String uuid, BaseModelResponeListener listener);
        void operationFavoriteAdd(String uuid,BaseModelResponeListener listener);
        void operationFavoriteCancel(String uuid,BaseModelResponeListener listener);
        void operationAddGoods(int count,boolean incr,String skuUuid,BaseModelResponeListener listener);
    }

    interface View extends BaseView{
        void fetchCoverSuccess(GoodsDetailInfoBean result);
        void fetchCoverRSuccess(List<GoodsRelationBean> result);
        void fetchSuccess(String msg);
        void fetchFile(String msg);
        void addCartSuccess(String msg);
        void refreshWishFavorite();
        void refreshRemoveWishFavorite();
    }

    interface Presenter{
        void fetchCoverList(String uuid);
        void fetchRelativeBox(String uuid);
        void operationFavoriteAdd(String uuid);
        void operationFavoriteCancel(String uuid);
        void operationAddGoods(int count,boolean incr,String skuUuid);
    }

}
