package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.CartCouponsBaean;

/**
 *
 */
public interface CartCouponsDialogContract {

    interface Model{
        void fetchCouponList(BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchCouponListSuccess(CartCouponsBaean result);
        void fetchCouponListFail(String msg);
        void fetchNetError();
    }

    interface Presenter{
        void fetchCouponList();
    }


}
