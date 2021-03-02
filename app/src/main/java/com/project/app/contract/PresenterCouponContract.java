package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.RewardDashbBean;

public interface PresenterCouponContract {

    interface Model{
        void fetchUseCouponHistory(int page, int pageSize,String url, BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchSuccess(RewardDashbBean result);
        void fetchFail(String failReason);
    }

    interface Presenter{
        void fetchUseCouponHistory(int page,int pageSize,String url);
    }


}
