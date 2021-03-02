package com.project.app.fragment.account;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.hb.basemodel.utils.ToastUtil;
import com.hb.basemodel.view.DelEditView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.project.app.R;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.contract.ChangePasswordContract;
import com.project.app.presenter.ChangePasswordPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePasswordFragment extends BaseMvpQmuiFragment<ChangePasswordPresenter> implements ChangePasswordContract.View {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout srl_refresh;
    @BindView(R.id.et_changePassword_oldPassword)
    DelEditView et_changePassword_oldPassword;
    @BindView(R.id.et_changePassword_newPassword)
    DelEditView et_changePassword_newPassword;
    @BindView(R.id.et_changePassword_confirmPassword)
    DelEditView et_changePassword_confirmPassword;
    @BindView(R.id.qrb_changePassword_save)
    QMUIRoundButton qrb_changePassword_save;

    private Context mContext;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_change_password;
    }

    @Override
    public void initView() {
        initTopbar();
        initWidget();
    }

    private void initWidget() {
        mContext = getContext();
        mPresenter = new ChangePasswordPresenter();
        mPresenter.attachView(this);
        srl_refresh.setEnablePureScrollMode(true);
        srl_refresh.setEnableRefresh(true);
        srl_refresh.setEnableLoadMore(true);

        qrb_changePassword_save.setChangeAlphaWhenPress(true);
    }

    private void initTopbar() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
    }

    @Override
    protected void lazyFetchData() {

    }

    @OnClick({R.id.qrb_changePassword_save,R.id.iv_back})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.iv_back:
                popBackStack();
                break;
            case R.id.qrb_changePassword_save:
                postPassword();
                break;
        }
    }

    private void postPassword() {
        String oldPs     = et_changePassword_oldPassword.editText.getText().toString().trim();
        String newPs     = et_changePassword_newPassword.editText.getText().toString().trim();
        String confirmPs = et_changePassword_confirmPassword.editText.getText().toString().trim();

        if(TextUtils.isEmpty(oldPs)){
            String warnContent = mContext.getResources().getString(R.string.change_password_empty_password_old);
            ToastUtil.showToast(warnContent);
            return;
        }

        if(TextUtils.isEmpty(newPs)){
            String warnContent = mContext.getResources().getString(R.string.change_password_empty_password_new);
            ToastUtil.showToast(warnContent);
            return;
        }

        if(TextUtils.isEmpty(confirmPs)){
            String warnContent = mContext.getResources().getString(R.string.change_password_empty_password_confirm);
            ToastUtil.showToast(warnContent);
            return;
        }

        if(!newPs.equals(confirmPs)){
            String warnContent = mContext.getResources().getString(R.string.change_password_empty_password_differ);
            ToastUtil.showToast(warnContent);
            return;
        }
        mPresenter.requestChangePassowrd(oldPs,newPs);
    }

    @Override
    public void fetchChangePassowrdSuccess(String result) {
        ToastUtil.showToast(result);
        popBackStack();
    }

    @Override
    public void fetchChangePassowrdFail(String msg) {
        ToastUtil.showToast(msg);
    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }

}
