package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.base.BasePresenter;
import com.project.app.bean.ClassifyListBean;
import com.project.app.bean.HomePopularLastOrderBean;

public interface HomePClassifyContract {

    interface Model{
        void fetchLastOrderInfo(BaseModelResponeListener listener);
        void fetchGoodsInfo(int page, int size, String no, BaseModelResponeListener listener);
        void fetchHomePopularMax(BaseModelResponeListener listener);
        void fetchHomeAction(BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchCategoryList(ClassifyListBean result);
        void fetchHomePopularMaxSuccess(String result);
        void fetchHomeActionMaxSuccess(String result);
       void fetchLastOrdersSuccess(HomePopularLastOrderBean result);
        void fetchFail(String failReason);
        void fetchNetWorkState(boolean unknowNet);
        void checkEmptyView();
    }

    interface Presenter extends BasePresenter.IBasePresenter{
        void fetchLastOrderInfo();
        void fetchGoodsInfo(int page,int size,String no);
        void fetchHomePopularMax();
        void fetchHomeAction();
    }

}
