package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.ProvinceBean;
import com.project.app.contract.ProvinceCityContract;
import com.project.app.model.ProvinceCityModel;

import java.util.List;

public class ProvinceCityPresenter extends BasePresenter<ProvinceCityContract.View> implements ProvinceCityContract.Presenter {
    private final ProvinceCityModel model;

    public ProvinceCityPresenter(){
        model = new ProvinceCityModel();
    }

    @Override
    public void fetchCityList(String regionId, String provinceId) {
        mView.startLoading();
        model.fetchCityList(regionId,provinceId,new BaseModelResponeListener<List<ProvinceBean>>() {
            @Override
            public void onSuccess(List<ProvinceBean> response) {
                mView.stopLoading();
                mView.fetchCityListSuccess(response);
            }

            @Override
            public void onFail(String msg) {
                mView.stopLoading();
                mView.fetchFail(msg);
            }
        });
    }

    @Override
    public void fetchProvinceList(String region) {
        mView.startLoading();
        model.fetchProvinceList(region,new BaseModelResponeListener<List<ProvinceBean>>() {
            @Override
            public void onSuccess(List<ProvinceBean> response) {
                mView.stopLoading();
                mView.fetchProvinceListSuccess(response);
            }
            @Override
            public void onFail(String msg) {
                mView.stopLoading();
                mView.fetchFail(msg);
            }
        });
    }
}
