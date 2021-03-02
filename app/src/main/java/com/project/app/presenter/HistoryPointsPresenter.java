package com.project.app.presenter;


import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.HistoryPointsBean;
import com.project.app.contract.HistoryPointsContract;
import com.project.app.model.HistoryPointsModel;

public class HistoryPointsPresenter extends BasePresenter<HistoryPointsContract.View> implements HistoryPointsContract.Presenter {

    private final HistoryPointsModel model;

    public HistoryPointsPresenter(){
        model = new HistoryPointsModel();
    }

    @Override
    public void fetchHistoryPoints(int currentPage, int pageSize) {
        model.fetchHistoryPoints(currentPage, pageSize, new BaseModelResponeListener<HistoryPointsBean>() {
            @Override
            public void onSuccess(HistoryPointsBean data) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchHistoryPointsSucess(data);
            }

            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.stopLoading();
                mView.fetchHistoryPointsFail(msg);
            }
        });
    }
}
