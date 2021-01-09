package com.project.app.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.hb.basemodel.utils.RxRegTool;
import com.hb.basemodel.utils.ToastUtil;
import com.hb.basemodel.view.SuperEditView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.project.app.R;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.contract.LoginForgetPContract;
import com.project.app.presenter.LoginForgePresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgetPasswordFragment extends BaseMvpQmuiFragment<LoginForgePresenter> implements LoginForgetPContract.View {
    @BindView(R.id.et_fetchPassword)
    SuperEditView et_fetchPassword;
    @BindView(R.id.tv_retriveP)
    TextView tv_retriveP;

    private String targetP = "";

    @Override
    public int getLayoutId() {
        return R.layout.fragment_forget_password;
    }

    @Override
    public void initView() {
        initTopbar();
        initWidget();
    }

    private void initWidget() {
        Bundle bundle = getArguments();
        targetP = bundle.getString("email");
        et_fetchPassword.editText.setText(targetP);
        mPresenter = new LoginForgePresenter();
        mPresenter.attachView(this);

        tv_retriveP.setSelected(!TextUtils.isEmpty(targetP));

        et_fetchPassword.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                String inputInfo = editable.toString();
                tv_retriveP.setSelected(!TextUtils.isEmpty(inputInfo));
            }
        });
    }

    private void initTopbar() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
    }

    @OnClick({R.id.tv_retriveP,R.id.iv_back})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.iv_back:
                popBackStack();
                break;
            case R.id.tv_retriveP:
                postLoveYou();
                break;
        }
    }

    private void postLoveYou() {
        String loveName = et_fetchPassword.editText.getText().toString().trim();
        String hint_email_empty = getContext().getResources().getString(R.string.forget_p_hint_email_nil);
        String hint_email_invaild = getContext().getResources().getString(R.string.hint_email_valid);
        if(TextUtils.isEmpty(loveName)){
            ToastUtil.showToast(hint_email_empty);
            return;
        }

        if(!RxRegTool.isEmail(loveName)){
            ToastUtil.showToast(hint_email_invaild);
            return;
        }
        mPresenter.sendTargetEmailCode(loveName);
    }

    @Override
    protected void lazyFetchData() {

    }

    @Override
    public void sendSuccess(String msg) {
        String sendSuc = getResources().getString(R.string.forget_p_send_email_success);
        ToastUtil.showToast(sendSuc);
        popBackStack();
    }

    @Override
    public void fetchFail(String failReason) {
        ToastUtil.showToast(failReason);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
