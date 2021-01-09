package com.project.app.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.basemodel.base.BaseUserInfo;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.other.UserUtil;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.activity.LoginActivity;
import com.project.app.adapter.CenterHelperAdapter;
import com.project.app.adapter.EarnFunctionsAdapter;
import com.project.app.adapter.MeOrderFunAdapter;
import com.project.app.base.BaseController;
import com.project.app.bean.EarnItemBean;
import com.project.app.bean.MeBindCPBean;
import com.project.app.bean.OrderFunctionBean;
import com.project.app.contract.MeContract;
import com.project.app.fragment.WebExplorerFragment;
import com.project.app.fragment.earn.RewardFragment;
import com.project.app.fragment.earn.SchallCashFragment;
import com.project.app.fragment.earn.SchallCashTestFragment;
import com.project.app.fragment.notify.NotifyCenterFragment;
import com.project.app.presenter.MePresenter;
import com.project.app.utils.LocaleUtil;
import com.project.app.utils.MathUtil;

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
    @BindView(R.id.tv_browseAllOrder)
    TextView tv_browseAllOrder;
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
            if(i==0){
                bean.setResId(R.mipmap.allwees_ic_mine_awaitingpayment);
            }else if(i==1){
                bean.setResId(R.mipmap.allwees_ic_mine_awaitingshiped);
            }else if(i==2){
                bean.setResId(R.mipmap.allwees_ic_mine_awaitingforreview);
            }else if(i==3){
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
        onRefreshView();
    }

    private void initView() {
        mFunAdapter = new MeOrderFunAdapter(mOrderFuns);
        mGoPrivacyp = getContext().getResources().getString(R.string.login_pp);
        GridLayoutManager manager = new GridLayoutManager(getContext(),4);
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
                Intent goNotify = HolderActivity.of(getContext(), NotifyCenterFragment.class);
                getContext().startActivity(goNotify);
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
                Intent intent = HolderActivity.of(getContext(), WebExplorerFragment.class,bundle);
                getContext().startActivity(intent);
            }else if(position == 2){
                Intent goSetting = HolderActivity.of(getContext(),SettingFragment.class);
                getContext().startActivity(goSetting);
            }
        });

        mEarnAdapter.setOnItemClickListener((adapter, view, position) -> {
            if(position == 0){
                Intent scash = HolderActivity.of(getContext(), SchallCashTestFragment.class);
                getContext().startActivity(scash);
            }else if(position == 1){
                Intent scash = HolderActivity.of(getContext(), RewardFragment.class);
                getContext().startActivity(scash);
            }
        });

        mFunAdapter.setOnItemClickListener((adapter, view, position) -> {
            boolean isLogin = UserUtil.getInstance().isLogin();
            if(!isLogin){
                Intent goLogin = new Intent(getContext(), LoginActivity.class);
                getContext().startActivity(goLogin);
            }else{
                String orderName = "All";
                if(position == 0){
                    orderName = "WAIT_SHIP";
                }else if(position == 1){
                    orderName = "SHIPPED";
                }else if(position == 2){
                    orderName = "REVIEW";
                }else if(position == 3){
                    orderName = "REFUNDED";
                }
                Bundle bundle = new Bundle();
                bundle.putString("typeName",orderName);
                Intent intent = HolderActivity.of(getContext(),OrderFragment.class,bundle);
                getContext().startActivity(intent);
            }
        });
    }

    public void onRefreshView() {
        if(UserUtil.getInstance().isLogin()){
            mPresenter.fetchUserInfo();
            bindCacheUserInfo();
        }else{
            tv_scCash.setVisibility(GONE);
            tv_goLogin.setVisibility(VISIBLE);
            tv_clientnName.setVisibility(GONE);
        }
    }

    @OnClick({R.id.cl_userInfo,R.id.tv_browseAllOrder,R.id.rl_cashDetail,R.id.rl_reward,R.id.qmiv_meHeader})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.cl_userInfo:
                if(!UserUtil.getInstance().isLogin()){
                    Intent skipIntent = new Intent(getContext(), LoginActivity.class);
                    getContext().startActivity(skipIntent);
                }
                break;
            case R.id.tv_browseAllOrder:
                if(!UserUtil.getInstance().isLogin()){
                    Intent skipIntent = new Intent(getContext(), LoginActivity.class);
                    getContext().startActivity(skipIntent);
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putString("typeName","All");
                    Intent intent = HolderActivity.of(getContext(),OrderFragment.class,bundle);
                    getContext().startActivity(intent);
                }
                break;
            case R.id.rl_cashDetail:
                if(!UserUtil.getInstance().isLogin()){
                    Intent skipIntent = new Intent(getContext(), LoginActivity.class);
                    getContext().startActivity(skipIntent);
                }else{
                    Intent goCash = HolderActivity.of(getContext(), SchallCashFragment.class);
                    getContext().startActivity(goCash);
                }
                break;
            case R.id.rl_reward:
                if(!UserUtil.getInstance().isLogin()){
                    Intent skipIntent = new Intent(getContext(), LoginActivity.class);
                    getContext().startActivity(skipIntent);
                }else{
                    Intent scash = HolderActivity.of(getContext(), RewardFragment.class);
                    getContext().startActivity(scash);
                }
                break;
            case R.id.qmiv_meHeader:
                
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
        }
    }

    public void bindCacheUserInfo() {
        BaseUserInfo meInfo = UserUtil.getInstance().getBaseUserInfo();
        if(meInfo != null){
            inflateDataToView(meInfo);
        }
    }

    private void inflateDataToView(BaseUserInfo result){
        String cash = result.getCashs();
        String point = result.getPoints();
        if(TextUtils.isEmpty(cash) || TextUtils.isEmpty(point)){
            return;
        }
        tv_goLogin.setVisibility(GONE);
        tv_clientnName.setVisibility(VISIBLE);
        tv_clientnName.setText(result.getFirstName() + result.getLastName());
        tv_scCash.setVisibility(VISIBLE);
        tv_scCash.setText(MathUtil.Companion.formatPrice(Double.parseDouble(result.getCashs())));
        tv_meBindPoints.setVisibility(VISIBLE);
        tv_pointsSuffix.setVisibility(VISIBLE);
        tv_meBindPoints.setText(doubleTrans(Double.parseDouble(result.getPoints())));
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
