package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.RewardDashbBean;
import com.project.app.contract.PresenterCouponContract;

import java.util.HashMap;

public class RecodeCouponModel implements PresenterCouponContract.Model {

    public RecodeCouponModel() {

    }

    @Override
    public void fetchUseCouponHistory(int page, int pageSize, String url, BaseModelResponeListener listener) {
        String requestType = "0";
        HashMap<String,String> params = new HashMap<>();
        params.put("current",page+"");
        params.put("size ",pageSize+"");

        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<RewardDashbBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<RewardDashbBean> response) {
                if(response.getCode() == 1){
                    listener.onSuccess(response.getData());
                }else{
                    listener.onFail(response.getMsg());
                }
            }
            @Override
            public void onFailure(Exception e) {
                listener.onFail(e.getMessage());
            }
        });
    }
}
