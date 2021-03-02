package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;

public interface ContactUsContract {

    interface Model{
        void fetctContactUsMethods(BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchContactMethodsSuccess(String result);
        void fetchContactMethodsFail(String msg);
    }

    interface Presenter{
        void fetctContactUsMethods();
    }
}
