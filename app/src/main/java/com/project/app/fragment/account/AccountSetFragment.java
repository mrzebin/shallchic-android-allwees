package com.project.app.fragment.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.fragment.address.SelectCountryFragment;
import com.project.app.utils.ImageCropUtils;
import com.project.app.utils.LocaleUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AccountSetFragment extends BaseMvpQmuiFragment {
    @BindView(R.id.iv_locateFlag)
    ImageView iv_locateFlag;
    @BindView(R.id.ll_changeLocation)
    LinearLayout ll_changeLocation;
    @BindView(R.id.tv_countryName)
    TextView tv_countryName;

    private Unbinder mUnbinder;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_account_setting;
    }

    @Override
    public void initView() {
        initTopbar();
        initWidget();
    }

    private void initWidget() {
        EventBus.getDefault().register(this);
        switchNationFalg();
        inflateRegion();
    }

    private void inflateRegion(){
        String name = "";
        if(LocaleUtil.getInstance().getLocaleCustom()){
            name = LocaleUtil.getInstance().getLocalCustomNameEn();
        }else{
            name = LocaleUtil.getInstance().getCoungryName();
        }
        tv_countryName.setText(name);
    }

    @OnClick({R.id.iv_back,R.id.ll_changeLocation})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.iv_back:
                popBackStack();
                break;
            case R.id.ll_changeLocation:
                Intent selectCountry = HolderActivity.of(getContext(), SelectCountryFragment.class);
                getContext().startActivity(selectCountry);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshDataEvent event){
        if (event.getmMsg().equals(Constant.EVENT_INTENT_COUNTRY_SELECT_INFO)){
            switchNationFalg();
            inflateRegion();
        }
    }

    private void switchNationFalg(){
       Bitmap flagBitmap = ImageCropUtils.getCropCountryFlag();
       iv_locateFlag.setImageBitmap(flagBitmap);
    }

    private void initTopbar() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
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
