package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.AddressBean;

public interface AddressManagerContract {

    interface Model{
        void fetchAddressList(BaseModelResponeListener listener);
        void deleteSpecifyAddress(String addressId, BaseModelResponeListener listener);
        void selectAddress(String addressId, BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchFail(String failReason);
        void fetchAddressSuccess(AddressBean result);
        void deleteAddressSuccess();
        void selectAddressSuccess();
    }

    interface Presenter{
        void fetchAddressList();
        void deleteSpecifyAddress(String addressId);
        void selectAddress(String addressId);
    }


}
