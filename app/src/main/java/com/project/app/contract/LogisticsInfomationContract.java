package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.LogisticTrackBean;

public interface LogisticsInfomationContract {

    interface Model{
        void checkLogisticTrack(String trackUuid, BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchLogisticTSuccess(LogisticTrackBean tracks);
        void fetchLogisticTFail(String msg);
    }

    interface Presenter{
        void checkLogisticTrack(String trackUuid);
    }


}
