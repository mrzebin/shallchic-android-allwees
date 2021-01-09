package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.MeBindCPBean;
import com.project.app.bean.ScCashBean;

public interface ScContract {

    interface Model{
        void fetchScHistory(BaseModelResponeListener listener);
        void fetchBindCashInfo(BaseModelResponeListener listener);
    }

    interface View extends BaseView{
        void fetchCashSuccess(MeBindCPBean result);
        void fetchSuccess(ScCashBean result);
        void fetchFail(String failReason);
    }

    interface Presenter{
        void fetchScHistory();
        void fetchBindCashInfo();
    }


}
