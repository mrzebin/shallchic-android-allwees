package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.DailySignInBean;

public interface DailySignInContract {

    interface Model{
        void fetchDailySignIn(BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchDailySignInResultSuccess(DailySignInBean result);
        void fetchDailySignInResultFail(String msg);
    }

    interface Presenter{
       void fetchDailySignIn();
    }


}
