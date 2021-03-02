package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.contract.GoodsSizeChartContract;
import com.project.app.model.GoodsSizeChartModel;

public class GoodsSizeChartPresenter extends BasePresenter<GoodsSizeChartContract.View> implements GoodsSizeChartContract.Presenter {
    private GoodsSizeChartModel model;

    public GoodsSizeChartPresenter(){
        model = new GoodsSizeChartModel();
    }

    @Override
    public void fetchGoodsSizeChart(String categoryNo, String productNo) {
        model.fetchGoodsSizeChart(categoryNo, productNo, new BaseModelResponeListener<String>() {
            @Override
            public void onSuccess(String data) {
                if(mView == null){
                    return;
                }
                mView.fetchGoodsSizeChartSuccess(data);
            }

            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.fetchGoodsSizeChartFail(msg);
            }
        });
    }
}
