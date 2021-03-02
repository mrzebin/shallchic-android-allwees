package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;

public interface GoodsSizeChartContract {

    interface Model{
        void fetchGoodsSizeChart(String categoryNo, String productNo, BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchGoodsSizeChartSuccess(String result);
        void fetchGoodsSizeChartFail(String msg);
    }

    interface Presenter{
       void fetchGoodsSizeChart(String categoryNo,String productNo);
    }


}
