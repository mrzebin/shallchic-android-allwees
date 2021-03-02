package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.LogisticTrackBean;
import com.project.app.contract.LogisticsInfomationContract;
import com.project.app.model.LogisticsInformationModel;

public class LogisticsInfomationPresenter extends BasePresenter<LogisticsInfomationContract.View> implements LogisticsInfomationContract.Presenter {
    private final LogisticsInformationModel model;

    public LogisticsInfomationPresenter(){
        model = new LogisticsInformationModel();
    }


    @Override
    public void checkLogisticTrack(String trackUuid) {
        mView.startLoading();
        model.checkLogisticTrack(trackUuid, new BaseModelResponeListener<LogisticTrackBean>() {
            @Override
            public void onSuccess(LogisticTrackBean data) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchLogisticTSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchLogisticTFail(msg);
            }
        });
    }
}
