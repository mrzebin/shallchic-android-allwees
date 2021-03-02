package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.base.BasePresenter;
import com.project.app.bean.ActionFreeOneBean;

public interface ActionDeliverDoubleContract {

    interface Model{
        void fetchActionFreeOne(String marketType, int page, int size, BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchActionFreeOneSuccess(ActionFreeOneBean result);
        void fetchActionFreeOneFail(String msg);
        void loadMatchViewStatus(int loadStatus);
    }

    interface Presenter extends BasePresenter.IBasePresenter {
        void fetchActionFreeOne(String marketType,int page,int size);    //买一送一活动
    }

}
