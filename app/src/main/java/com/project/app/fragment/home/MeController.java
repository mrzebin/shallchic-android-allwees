package com.project.app.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.basemodel.base.BaseUserInfo;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.image.ImageLoader;
import com.hb.basemodel.other.UserUtil;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.activity.LoginActivity;
import com.project.app.activity.SettingHolderActivity;
import com.project.app.adapter.CenterHelperAdapter;
import com.project.app.adapter.EarnFunctionsAdapter;
import com.project.app.adapter.MeOrderFunAdapter;
import com.project.app.base.BaseController;
import com.project.app.bean.EarnItemBean;
import com.project.app.bean.MeBindCPBean;
import com.project.app.bean.OrderFunctionBean;
import com.project.app.config.AppsFlyConfig;
import com.project.app.contract.MeContract;
import com.project.app.fragment.WebExplorerFragment;
import com.project.app.fragment.account.UpdateProfileFragment;
import com.project.app.fragment.cashback.CashBackRecommendFragment;
import com.project.app.fragment.contact.ContactUsFragment;
import com.project.app.fragment.earn.RewardFragment;
import com.project.app.fragment.earn.SchallCashFragment;
import com.project.app.fragment.earn.SchallCashTestFragment;
import com.project.app.fragment.notify.NotifyCenterFragment;
import com.project.app.presenter.MePresenter;
import com.project.app.utils.AppsFlyEventUtils;
import com.project.app.utils.LocaleUtil;
import com.project.app.utils.MathUtil;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeController extends BaseController<MePresenter> implements MeContract.View {
    @BindView(R.id.rlv_orderFuns)
    RecyclerView rlv_orderFuns;
    @BindView(R.id.rlv_earn)
    RecyclerView rlv_earn;
    @BindView(R.id.rlv_me_helper)
    RecyclerView rlv_me_helper;
    @BindView(R.id.cl_userInfo)
    ConstraintLayout cl_userInfo;
    @BindView(R.id.tv_clientnName)
    TextView tv_clientnName;
    @BindView(R.id.tv_goLogin)
    TextView tv_goLogin;
    @BindView(R.id.tv_scCash)
    TextView tv_scCash;
    @BindView(R.id.tv_meBindPoints)
    TextView tv_meBindPoints;
    @BindView(R.id.rl_cashDetail)
    RelativeLayout rl_cashDetail;
    @BindView(R.id.qmiv_meHeader)
    QMUIRadiusImageView qmiv_meHeader;
    @BindView(R.id.tv_mePointsSuffix)
    TextView tv_pointsSuffix;
    @BindView(R.id.ll_me_cashBackDetect)
    LinearLayout ll_me_cashBackDetect;

    private String mGoPrivacyp;
    private MeOrderFunAdapter mFunAdapter;
    private EarnFunctionsAdapter mEarnAdapter;
    private CenterHelperAdapter mHelperAdatper;
    private final List<OrderFunctionBean> mOrderFuns = new ArrayList<>();
    private final List<EarnItemBean> mEarnBeans = new ArrayList<>();
    private final List<String> mSettingHelper = new ArrayList<>();

    public MeController(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.me_layout,this);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        String[] funName = getContext().getResources().getStringArray(R.array.me_orderFuns);
        for(int i=0;i<funName.length;i++){
            OrderFunctionBean bean = new OrderFunctionBean();
            bean.setIndex(i);
            bean.setFunName(funName[i]);
            if(i ==0){
                bean.setResId(R.mipmap.allwees_ic_mine_penging);
            }if(i==1){
                bean.setResId(R.mipmap.allwees_ic_mine_awaitingpayment);
            }else if(i==2){
                bean.setResId(R.mipmap.allwees_ic_mine_awaitingshiped);
            }else if(i==3){
                bean.setResId(R.mipmap.allwees_ic_mine_awaitingforreview);
            }else if(i==4){
                bean.setResId(R.mipmap.allwees_ic_mine_awaitingshipment);
            }
            mOrderFuns.add(bean);
        }
        String[] earns = getContext().getResources().getStringArray(R.array.me_earn);
        for(String item:earns){
            mEarnBeans.add(new EarnItemBean(item,""));
        }
        String[] settingFun = getContext().getResources().getStringArray(R.array.me_helpers);
        mSettingHelper.addAll(Arrays.asList(settingFun));
        mPresenter = new MePresenter();
        mPresenter.attachView(this);

        //显示初始化数据
        BaseUserInfo meInfo = UserUtil.getInstance().getBaseUserInfo();
        inflateDataToView(meInfo);
        onRefreshView();
    }

    private void initView() {
        mFunAdapter = new MeOrderFunAdapter(mOrderFuns);
        mGoPrivacyp = getContext().getResources().getString(R.string.login_pp);
        GridLayoutManager manager = new GridLayoutManager(getContext(),5);
        rlv_orderFuns.setLayoutManager(manager);
        rlv_orderFuns.setAdapter(mFunAdapter);
        LinearLayoutManager llManager = new LinearLayoutManager(getContext());
        rlv_earn.setLayoutManager(llManager);
        mEarnAdapter = new EarnFunctionsAdapter(mEarnBeans);
        rlv_earn.setAdapter(mEarnAdapter);
        rlv_me_helper.setLayoutManager(new LinearLayoutManager(getContext()));
        mHelperAdatper = new CenterHelperAdapter(mSettingHelper);
        rlv_me_helper.setAdapter(mHelperAdatper);

        mHelperAdatper.setOnItemClickListener((adapter, view, position) -> {
            if(position == 0){
                HolderActivity.startFragment(getContext(),NotifyCenterFragment.class);
                AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_ME_NOTIFICATION);
            }else if(position == 1){
                Bundle bundle = new Bundle();
                String siteUrl = "";
                if(LocaleUtil.getInstance().getLanguage().equals("en")){
                    siteUrl = UrlConfig.HELPER_CENTER_URL_EN;
                }else{
                    siteUrl = UrlConfig.HELPER_CENTER_URL;
                }
                bundle.putString("webUrl", siteUrl);
                bundle.putString("type","1");
                bundle.putString("title",mGoPrivacyp);
                HolderActivity.startFragment(getContext(),WebExplorerFragment.class,bundle);
            }else if(position == 2){
                HolderActivity.startFragment(getContext(), ContactUsFragment.class);
            }else if(position == 3){
                HolderActivity.startFragment(getContext(), SettingFragment.class);
                AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_ME_SETTING);
            }
        });

        mEarnAdapter.setOnItemClickListener((adapter, view, position) -> {
            if(position == 0){
                HolderActivity.startFragment(getContext(),SchallCashTestFragment.class);
            }else if(position == 1){
                HolderActivity.startFragment(getContext(),RewardFragment.class);
            }
        });

        mFunAdapter.setOnItemClickListener((adapter, view, position) -> {
            boolean isLogin = UserUtil.getInstance().isLogin();
            if(!isLogin){
                Intent goLogin = new Intent(getContext(), LoginActivity.class);
                getContext().startActivity(goLogin);
            }else{
                String orderName = "ALL";
                if(position == 0){
                    orderName = "PENDING";
                    AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_ME_ORDER_PENDING);
                }else if(position == 1){
                    orderName = "WAIT_SHIP";
                    AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_ME_ORDER_PAID);
                }else if(position == 2){
                    orderName = "SHIPPED";
                    AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_ME_ORDER_SHIPPED);
                }else if(position == 3){
                    orderName = "REVIEW";
                    AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_ME_ORDER_REVIEW);
                }else if(position == 4){
                    orderName = "REFUNDED";
                    AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_ME_ORDER_REFUND);
                }
                Bundle bundle = new Bundle();
                bundle.putString("typeName",orderName);
                HolderActivity.startFragment(getContext(),OrderFragment.class,bundle);
            }
        });
    }

    public void onRefreshView() {
        if(UserUtil.getInstance().isLogin()){
            mPresenter.fetchUserInfo();       //刷新用户信息
        }else{
            tv_goLogin.setVisibility(VISIBLE);
            tv_scCash.setVisibility(GONE);
            ll_me_cashBackDetect.setVisibility(GONE);
            tv_clientnName.setVisibility(GONE);
            qmiv_meHeader.setImageResource(R.mipmap.allwees_ic_default_header);
        }
    }

    @OnClick({R.id.cl_userInfo,R.id.rl_allOrder,R.id.rl_cashDetail,R.id.rl_reward,R.id.qmiv_meHeader,R.id.tv_clientnName,R.id.ll_me_cashBackDetect})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.cl_userInfo:
                if(!UserUtil.getInstance().isLogin()){
                    Intent skipIntent = new Intent(getContext(), LoginActivity.class);
                    getContext().startActivity(skipIntent);
                }
                break;
            case R.id.rl_allOrder:
                if(!UserUtil.getInstance().isLogin()){
                    Intent skipIntent = new Intent(getContext(), LoginActivity.class);
                    getContext().startActivity(skipIntent);
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putString("typeName","ALL");
                    HolderActivity.startFragment(getContext(),OrderFragment.class,bundle);
                    AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_ME_ORDER_ALL);
                }
                break;
            case R.id.rl_cashDetail:
                if(!UserUtil.getInstance().isLogin()){
                    Intent skipIntent = new Intent(getContext(), LoginActivity.class);
                    getContext().startActivity(skipIntent);
                }else{
                    HolderActivity.startFragment(getContext(),SchallCashFragment.class);
                }
                break;
            case R.id.rl_reward:
                if(!UserUtil.getInstance().isLogin()){
                    Intent skipIntent = new Intent(getContext(), LoginActivity.class);
                    getContext().startActivity(skipIntent);
                }else{
                    HolderActivity.startFragment(getContext(),RewardFragment.class);
                    AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_ME_REWARDS);
                }
                break;
            case R.id.ll_me_cashBackDetect:    //返现的入口
                HolderActivity.startFragment(getContext(),CashBackRecommendFragment.class);
                break;
            case R.id.qmiv_meHeader:
                if(UserUtil.getInstance().isLogin()){
                    SettingHolderActivity.startFragment(getContext(),UpdateProfileFragment.class);
                }else{
                    Intent skipIntent = new Intent(getContext(), LoginActivity.class);
                    getContext().startActivity(skipIntent);
                }
                AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_ME_HEADER_ICON);
                break;
            case R.id.tv_clientnName:
                if(UserUtil.getInstance().isLogin()){
                    SettingHolderActivity.startFragment(getContext(),UpdateProfileFragment.class);
                }
                AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_ME_HEADER_ICON);
                break;
        }
    }

    /**
     * 退出app时刷新数据
     */
    public void syncExitApp(){
        if(!UserUtil.getInstance().isLogin()){
            tv_scCash.setVisibility(GONE);
            tv_goLogin.setVisibility(VISIBLE);
            tv_clientnName.setVisibility(GONE);
            tv_meBindPoints.setVisibility(GONE);
            tv_pointsSuffix.setVisibility(GONE);
            ll_me_cashBackDetect.setVisibility(GONE);
            qmiv_meHeader.setImageResource(R.mipmap.allwees_ic_default_header);
        }
    }

    private void inflateDataToView(BaseUserInfo result){
        if(result != null){
            String cash = result.getCashs();
            String point = result.getPoints();
            if(TextUtils.isEmpty(cash) || TextUtils.isEmpty(point)){
                return;
            }
            String avatarUrl    = result.getAvatar();
            if(!TextUtils.isEmpty(avatarUrl)){  //刷新头像
                ImageLoader.getInstance().displayImage(qmiv_meHeader,avatarUrl,R.mipmap.allwees_ic_default_header);
            }
            tv_goLogin.setVisibility(GONE);
            tv_clientnName.setVisibility(VISIBLE);
            tv_clientnName.setText(result.getFirstName() + result.getLastName());
            tv_scCash.setVisibility(VISIBLE);
            tv_scCash.setText(MathUtil.Companion.formatPrice(Double.parseDouble(result.getCashs())));
            tv_meBindPoints.setVisibility(VISIBLE);
            tv_pointsSuffix.setVisibility(VISIBLE);
            ll_me_cashBackDetect.setVisibility(VISIBLE);
            tv_meBindPoints.setText(doubleTrans(Double.parseDouble(result.getPoints())));
        }
    }

    public void fetchLocaleLanguage() {
        mHelperAdatper.notifyDataSetChanged();
    }

    @Override
    public void fetchUserInfo(BaseUserInfo result) {
        if(result != null){
            if(UserUtil.getInstance().isLogin()){
                UserUtil.getInstance().setUserLoginInfo(result);
                inflateDataToView(result);
            }else{
                tv_goLogin.setVisibility(VISIBLE);
                tv_clientnName.setVisibility(GONE);
                ll_me_cashBackDetect.setVisibility(GONE);
            }
        }
    }

    @Override
    public void fetchCashSuccess(MeBindCPBean result) {
        if(result != null){
            tv_scCash.setVisibility(VISIBLE);
            tv_scCash.setText(MathUtil.Companion.formatPrice(result.getValue()));
        }
    }

    @Override
    public void fetchPointSuccess(MeBindCPBean result) {
        if(result != null){
            tv_meBindPoints.setVisibility(VISIBLE);
            tv_pointsSuffix.setVisibility(VISIBLE);
            tv_meBindPoints.setText(doubleTrans(result.getValue()));
        }
    }

    public static String doubleTrans(double d){
        if(Math.round(d)-d==0){
            return String.valueOf((long)d);
        }
        return String.valueOf(d);
    }

    @Override
    public void fetchFail(String failReason) {

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
}
