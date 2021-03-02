package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.RewardDashbBean;
import com.project.app.contract.PresenterCouponContract;
import com.project.app.model.RecodeCouponModel;

public class RecodeCPresenter extends BasePresenter<PresenterCouponContract.View> implements PresenterCouponContract.Presenter {
    private final PresenterCouponContract.Model model;

    public RecodeCPresenter(){
        model = new RecodeCouponModel();
    }

    @Override
    public void fetchUseCouponHistory(int page, int pageSize, String url) {
       model.fetchUseCouponHistory(page, pageSize, url, new BaseModelResponeListener<RewardDashbBean>() {
           @Override
           public void onSuccess(RewardDashbBean data) {
               if(mView == null){
                   return;
               }
               mView.fetchSuccess(data);
           }

           @Override
           public void onFail(String msg) {
               if(mView == null){
                   return;
               }
                mView.fetchFail(msg);
           }
       });
    }
}
