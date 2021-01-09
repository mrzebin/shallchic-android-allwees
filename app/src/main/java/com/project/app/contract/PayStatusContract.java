package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.ClassifyListBean;

public interface PayStatusContract {

    interface Model{
        void fetchCLikeList(int currentPage, int pagetSize, BaseModelResponeListener listener);
    }

    interface View extends BaseView{
        void fetchMightLikeData(ClassifyListBean result);
        void fetchFail(String msg);
    }

    interface Presenter{
        void fetchCLikeList(int currentPage,int pageSize);
    }


}
