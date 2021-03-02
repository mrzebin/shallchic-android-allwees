package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.HistoryPointsBean;

public interface HistoryPointsContract {

    interface Model{
        void fetchHistoryPoints(int currentPage, int pageSize, BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchHistoryPointsSucess(HistoryPointsBean result);
        void fetchHistoryPointsFail(String msg);
    }

    interface Presenter{
        void fetchHistoryPoints(int currentPage,int pageSize);
    }


}
