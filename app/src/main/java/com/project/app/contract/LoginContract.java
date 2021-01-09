package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseUserInfo;
import com.hb.basemodel.base.BaseView;
import com.hb.basemodel.base.LocalUserInfo;

import java.util.Map;

public interface LoginContract {

    interface Model{
        void regist(Map<String,String> params, BaseModelResponeListener listener);
        void login(Map<String,String> params,BaseModelResponeListener listener);
        void fbLogin(Map<String,String> params,BaseModelResponeListener listener);
        void fetchUserInfo(String accessToken,BaseModelResponeListener listener);
    }

    interface View extends BaseView{
        void fetchRegistSuccess(String msg);
        void fetchLoginSuccess(LocalUserInfo result);
        void fetchFbLoginSuccess(LocalUserInfo result);
        void fetchFail(String failReason);
        void fetchUserInfoSuccess(BaseUserInfo result);
    }

    interface Presenter{
        void regist(Map<String,String> params);
        void login(Map<String,String> params);
        void fbLogin(Map<String,String> params);
        void fetchUserInfo();
    }
}
