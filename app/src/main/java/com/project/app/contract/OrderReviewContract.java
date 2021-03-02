package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.AwsAccessTokenBean;

import java.util.List;

public interface OrderReviewContract {

    interface Model {
        void fetchUploadToken(String code, BaseModelResponeListener listener);
        void submitReviewReasonToService(String orderUuid, String orderItemUuid, int rating, String reason, List<String> photos, BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void postReviewReasonSuccess(String result);
        void postReviewReasonFail(String result);
        void fetchUAccessTokenSuccess(AwsAccessTokenBean result);
        void fetchUAccessTokenFail(String result);
    }

    interface Presenter{
        void fetchUploadToken(String code);
        void submitReviewReasonToService(String orderUuid, String orderItemUuid, int rating,String reason,List<String> photos);
    }

}
