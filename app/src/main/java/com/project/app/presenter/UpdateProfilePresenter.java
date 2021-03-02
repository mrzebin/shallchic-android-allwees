package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.AwsAccessTokenBean;
import com.project.app.bean.UserRegistInfoBean;
import com.project.app.contract.UpdateProfileContract;
import com.project.app.fragment.account.UpdateProfileFragment;
import com.project.app.model.UpdateProfileModel;

public class UpdateProfilePresenter extends BasePresenter<UpdateProfileContract.View> implements UpdateProfileContract.Presenter {
    private final UpdateProfileModel model;

    public UpdateProfilePresenter(){
        model = new UpdateProfileModel();
    }

    @Override
    public void fetchRegistInfo() {
        model.fetchRegistInfo(new BaseModelResponeListener<UserRegistInfoBean>() {
            @Override
            public void onSuccess(UserRegistInfoBean data) {
                if(mView == null){
                    return;
                }
                mView.fetchRegistInfoSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.fetchFail(UpdateProfileFragment.RESPONSE_FETCH_REGIST_INFO,msg);
            }
        });
    }

    @Override
    public void updateRegistInfo(String birthdayDay, String birthdayMonth, String firstName, String lastName, String gender) {
        model.updateRegistInfo(birthdayDay, birthdayMonth, firstName, lastName, gender, new BaseModelResponeListener<String>() {
            @Override
            public void onSuccess(String data) {
                if(mView == null){
                    return;
                }
                mView.updateRegistInfoSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.fetchFail(UpdateProfileFragment.RESPONSE_UPDATE_REGIST_INFO,msg);
            }
        });
    }

    @Override
    public void updateRegistAvatar(String upImgUrl) {
        model.updateRegistAvatar(upImgUrl, new BaseModelResponeListener() {
            @Override
            public void onSuccess(Object data) {
                if(mView == null){
                    return;
                }
                mView.updateRegistAvatarSuccess();
            }
            @Override
            public void onFail(String msg) {

            }
        });
    }

    @Override
    public void fetchUploadToken(String code) {
        model.fetchUploadToken(code, new BaseModelResponeListener<AwsAccessTokenBean>() {
            @Override
            public void onSuccess(AwsAccessTokenBean data) {
                if (data == null){
                    return;
                }
                if(mView != null){
                    mView.fetchUAccessTokenSuccess(data);
                }
            }
            @Override
            public void onFail(String msg) {
                if(mView != null){
                    mView.fetchUAccessTokenFail(msg);
                }
            }
        });
    }

    @Override
    public void onDestoryView() {
        mView = null;
        System.gc();
    }
}
