package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseUserInfo;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.MeBindCPBean;

public interface MeContract {

    interface Model{
        void fetchUserInfo(String accessToken, BaseModelResponeListener listener);
        void fetchBindCashInfo(BaseModelResponeListener listener);
        void fetchBindPointInfo(BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchUserInfo(BaseUserInfo result);
        void fetchCashSuccess(MeBindCPBean result);
        void fetchPointSuccess(MeBindCPBean result);
        void fetchFail(String failReason);
    }

    interface Presenter{
        void fetchBindCashInfo();
        void fetchBindPointInfo();
        void fetchUserInfo();
    }


}
