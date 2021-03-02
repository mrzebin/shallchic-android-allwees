package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.MeBindCPBean;
import com.project.app.bean.RedeemBean;

public interface RedeemContract {

    interface Model{
        void upUsePointChangeCoupon(String cpUuid, BaseModelResponeListener listener);
        void fetchRedeemInfo(int page, int pageSize, BaseModelResponeListener listener);
        void fetchBindPointInfo(BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void upChagePointSuccess();
        void fetchPointSuccess(MeBindCPBean result);
        void fetchRedeemInfoSuccess(RedeemBean result);
        void fetchFail(String failReason);
    }

    interface Presenter{
       void upUsePointChangeCoupon(String cpUuid);
       void fetchRedeemInfo(int page,int pageSize);
       void fetchBindPointInfo();
    }


}
