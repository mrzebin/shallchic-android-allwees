package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.CreateAddressBean;

import java.util.HashMap;

public interface AddressControlContract {

    interface Model{
        void editAddress(HashMap<String,String> params, BaseModelResponeListener listener);
        void createAddress(HashMap<String,String> params,BaseModelResponeListener listener);
    }

    interface View extends BaseView{
        void editAddressResult(CreateAddressBean response);
        void addAddressResult(CreateAddressBean response);
        void createAddressFail(String result);
        void editAddreddFail(String result);
    }

    interface Presenter{
        void editAddress(HashMap<String,String> params);
        void createAddress(HashMap<String,String> params);
    }

}
