package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;

public interface PushMiuiContract {

    interface Model{
        void bindMiuiRegistId(String registId, boolean isBind, BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void bindMiuiPushIdSuccess();
        void bindMuiPushIdFail(String msg);
    }

    interface Presenter{
        void bindMiuiPushId(String registId,boolean isBind);
    }


}
