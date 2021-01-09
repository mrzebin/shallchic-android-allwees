package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.ProvinceBean;

import java.util.List;

public interface ProvinceCityContract {

    interface Model {
        void fetchCityList(String regionId, String provinceId, BaseModelResponeListener listener);

        void fetchProvinceList(String region, BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchProvinceListSuccess(List<ProvinceBean> result);

        void fetchCityListSuccess(List<ProvinceBean> result);

        void fetchFail(String msg);
    }

    interface Presenter {
        void fetchCityList(String regionId, String provinceId);

        void fetchProvinceList(String region);
    }


}
