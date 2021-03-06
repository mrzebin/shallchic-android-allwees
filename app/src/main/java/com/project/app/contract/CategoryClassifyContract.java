package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.ClassifyListBean;

public interface CategoryClassifyContract {

    interface Model{
        void fetchGoodsInfo(int page, int size, String no, BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchCategoryList(ClassifyListBean result);
        void fetchFail(String failReason);
        void fetchNetWorkState(boolean unknowNet);
    }

    interface Presenter{
        void fetchGoodsInfo(boolean showLoading,int page,int size,String no);
    }


}
