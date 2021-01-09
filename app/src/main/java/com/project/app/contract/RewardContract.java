package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.MeBindCPBean;
import com.project.app.bean.RedeemBean;

public interface RewardContract {

    interface Model{
        void fetchRedeemInfo(int page,int pageSize,BaseModelResponeListener listener);
        void fetchBindPointInfo(BaseModelResponeListener listener);
    }

    interface View extends BaseView{
        void fetchPointSuccess(MeBindCPBean result);
        void fetchRedeemInfoSuccess(RedeemBean result);
        void fetchFail(String failReason);
    }

    interface Presenter{
       void fetchRedeemInfo(int page,int pageSize);
        void fetchBindPointInfo();
    }


}
