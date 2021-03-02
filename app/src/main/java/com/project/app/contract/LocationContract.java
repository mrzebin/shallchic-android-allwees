package com.project.app.contract;

import com.hb.basemodel.base.BaseView;

public interface LocationContract {

    interface Model{
        void login(String name, String password, ModelListener listener);

        interface ModelListener{
            void complete(String result);
            void fail(String result);
        }
    }

    interface View extends BaseView {
        void loginSuccess(String result);
        void loginFail(String failReason);
    }

    interface Presenter{
        void login(String name, String passowrd);
    }


}
