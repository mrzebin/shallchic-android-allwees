package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.DailySignInBean;
import com.project.app.contract.DailySignInContract;

import java.util.ArrayList;
import java.util.List;

public class DailySignInModel implements DailySignInContract.Model {

    @Override
    public void fetchDailySignIn(BaseModelResponeListener listener) {
        String requestType = "1";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.DAILY_SIGN_IN_URL;

        List<OkHttpUtils.Param> params = new ArrayList<>();
        OkHttpUtils.post(url,requestType,new OkHttpUtils.ResultCallback<BaseObjectBean<DailySignInBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<DailySignInBean> response) {
                if(response.getCode() == 1){
                    listener.onSuccess(response.getData());
                }else{
                    listener.onFail(response.getMsg());
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        },params);
    }
}
