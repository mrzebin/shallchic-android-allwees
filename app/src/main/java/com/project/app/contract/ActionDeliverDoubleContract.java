package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.ActionFreeOneBean;

public interface ActionDeliverDoubleContract {

    interface Model{
        void fetchActionFreeOne(int page,int size,BaseModelResponeListener listener);
    }

    interface View extends BaseView{
        void fetchActionFreeOneSuccess(ActionFreeOneBean result);
        void fetchActionFreeOneFail(String msg);
    }

    interface Presenter{
        void fetchActionFreeOne(int page,int size);    //买一送一活动
    }

}
