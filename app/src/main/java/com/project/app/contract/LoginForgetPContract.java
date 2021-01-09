package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;

public interface LoginForgetPContract {

    interface Model{
        void  sendTargetEmailCode(String email,BaseModelResponeListener listener);
    }

    interface View extends BaseView{
        void sendSuccess(String msg);
        void fetchFail(String failReason);
    }

    interface Presenter{
      void  sendTargetEmailCode(String email);
    }

}
