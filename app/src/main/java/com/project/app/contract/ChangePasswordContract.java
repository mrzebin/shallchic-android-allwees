package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;

public interface ChangePasswordContract {

    interface Model{
        void requestChangePassowrd(String originPs, String newPs, BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchChangePassowrdSuccess(String msg);
        void fetchChangePassowrdFail(String msg);
    }

    interface Presenter{
        void requestChangePassowrd(String originPs,String newPs);
    }


}
