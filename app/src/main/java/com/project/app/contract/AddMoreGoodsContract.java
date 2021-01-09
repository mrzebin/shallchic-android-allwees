package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.AddMoreListBean;

public interface AddMoreGoodsContract {

    interface Model{
        void fetchGoodsList(String categoryNo, int currentPage, int pagetSize, BaseModelResponeListener listener);
    }

    interface View extends BaseView{
        void fetchSuccess(AddMoreListBean result);
        void fetchFail(String failReason);
    }

    interface Presenter{
        void fetchGoodsList(String categoryNo, int currentPage, int pagetSize);
    }

}
