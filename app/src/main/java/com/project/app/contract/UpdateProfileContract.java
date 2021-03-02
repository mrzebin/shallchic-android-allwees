package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.base.BasePresenter;
import com.project.app.bean.AwsAccessTokenBean;
import com.project.app.bean.UserRegistInfoBean;

public interface UpdateProfileContract {

    interface Model{
        void fetchRegistInfo(BaseModelResponeListener listener);   //获取注册信息
        void updateRegistInfo(String birthdayDay, String birthdayMonth, String firstName, String lastName, String gender, BaseModelResponeListener listener);  //更新注册信息
        void updateRegistFile(String upImgUrl, String timeStamp, BaseModelResponeListener listener);  //上传注册文件
        void updateRegistAvatar(String upImgUrl, BaseModelResponeListener listener); //上传头像
        void fetchUploadToken(String code, BaseModelResponeListener listener);   //获取上传头像的token
    }

    interface View extends BaseView {
        void fetchRegistInfoSuccess(UserRegistInfoBean result);
        void updateRegistInfoSuccess(String result);
        void updateRegistAvatarSuccess();
        void fetchFail(int mode,String failMsg);  //mode 为失败的接口
        void fetchUAccessTokenSuccess(AwsAccessTokenBean result);
        void fetchUAccessTokenFail(String result);
    }

    interface Presenter extends BasePresenter.IBasePresenter {
        void fetchRegistInfo();   //获取注册信息
        void updateRegistInfo(String birthdayDay,String birthdayMonth,String firstName,String lastName,String gender);  //更新注册信息
        void updateRegistAvatar(String upImgUrl); //上传头像
        void fetchUploadToken(String code);  //获取上传头像的token
    }
}
