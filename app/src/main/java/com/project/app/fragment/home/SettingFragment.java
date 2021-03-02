package com.project.app.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.other.UserUtil;
import com.hb.basemodel.utils.AppUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.activity.LoginActivity;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.config.AppsFlyConfig;
import com.project.app.fragment.account.AccountSetFragment;
import com.project.app.fragment.address.AddressManagerFragment;
import com.project.app.fragment.help.AboutUsFragment;
import com.project.app.fragment.legal.LegalAndTermsFragment;
import com.project.app.utils.AppsFlyEventUtils;
import com.project.app.utils.ImageCropUtils;
import com.project.app.utils.LocaleUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingFragment extends BaseMvpQmuiFragment {
    @BindView(R.id.tv_topTitle)
    TextView tv_topTitle;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.ll_logout)
    LinearLayout ll_logout;
    @BindView(R.id.tv_appVersion)
    TextView tv_appVersion;
    @BindView(R.id.iv_locateFlag)
    ImageView iv_locateFlag;

    private final List<String> settingItes = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    public void initView() {
        initTopbar();
        initWidget();
    }

    @OnClick({R.id.iv_back,R.id.ll_accountSet,R.id.ll_manageAddress,R.id.ll_legalTerm,R.id.ll_aboutUs,R.id.ll_logout})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.iv_back:
                popBackStack();
                break;
            case R.id.ll_accountSet:
                HolderActivity.startFragment(getContext(),AccountSetFragment.class);
                AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_SETTINGS_ACCOUNT);
                break;
            case R.id.ll_manageAddress:
                if(UserUtil.getInstance().isLogin()){
                    Bundle bundle = new Bundle();
                    bundle.putString("type","1");
                    HolderActivity.startFragment(getContext(),AddressManagerFragment.class,bundle);
                    AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_SETTINGS_ADDRESS_MANAGER);
                }else{
                    getContext().startActivity(new Intent(getContext(),LoginActivity.class));
                }
                break;
            case R.id.ll_legalTerm:
                HolderActivity.startFragment(getContext(),LegalAndTermsFragment.class);
                AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_SETTINGS_ADDRESS_MANAGER);
                break;
            case R.id.ll_aboutUs:
                AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_SETTINGS_ABOUTUS);
                HolderActivity.startFragment(getContext(),AboutUsFragment.class);
                break;
            case R.id.ll_logout:
                exitApp();
                AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_SETTINGS_LOGOUT);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshDataEvent event){
        if (event.getmMsg().equals(Constant.EVENT_INTENT_COUNTRY_SELECT_INFO)){
            switchNationFalg();
        }
    }

    private void switchNationFalg(){
        iv_locateFlag.setImageBitmap(ImageCropUtils.getCropCountryFlag());
    }

    private void initTopbar() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        EventBus.getDefault().register(this);
    }

    private void initWidget() {
        String[] settings     = getContext().getResources().getStringArray(R.array.setItem);
        settingItes.addAll(Arrays.asList(settings));
        tv_appVersion.setText("v " + AppUtils.getVersionName(getContext()) + "." + AppUtils.getVersionCode(getContext()));
        switchNationFalg();
        hideShowExitButton();
    }


    public void hideShowExitButton(){
        if(!UserUtil.getInstance().isLogin()){
            ll_logout.setVisibility(View.GONE);
        }else{
            ll_logout.setVisibility(View.VISIBLE);
        }
    }


    private void exitApp() {
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle(getContext().getString(R.string.dialog_title_exit))
                .setMessage(getContext().getString(R.string.dialog_message_exit))
                .addAction(getContext().getString(R.string.str_no), (dialog, index) -> dialog.dismiss())
                .addAction(0,getContext().getString(R.string.str_yes), QMUIDialogAction.ACTION_PROP_NEGATIVE, (dialog, index) -> {
                    dialog.dismiss();
                    UserUtil.getInstance().outLogin(getContext());
                    LocaleUtil.getInstance().clearLocaleInfo();
                    hideShowExitButton();
                    popBackStack();
                    EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_EXIT_APP_SURE));   //刷新我的页面的数据
                })
                .create().show();
    }

    @Override
    protected void lazyFetchData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
