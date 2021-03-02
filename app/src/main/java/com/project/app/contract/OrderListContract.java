package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.HomeTopTipsBean;

public interface OrderListContract {

    interface Model{
        void fetchActionTopTips(BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchActionTopTipsSuccess(HomeTopTipsBean result);
        void fethhActionTopTipsFail(String result);
    }

    interface Presenter{
        void fetchActionTopTips();
    }


}
