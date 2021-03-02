package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.WishDataBean;

public interface WishlistContract {

    interface Model{
        void fetchWishList(int page,int pageSize,BaseModelResponeListener listener);
    }

    interface View extends BaseView{
        void fetchSuccess(WishDataBean result);
        void fetchFail(String failReason);
        void fetchNetWorkState(boolean unknowNet);
    }

    interface Presenter{
        void fetchWishList(int page,int pageSize);
    }

}

