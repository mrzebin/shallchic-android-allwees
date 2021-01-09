package com.project.app.fragment.earn.reward;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.project.app.R;
import com.project.app.adapter.BaseFragmentAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.MeBindCPBean;
import com.project.app.bean.RedeemBean;
import com.project.app.config.AppEnvironmentResConfig;
import com.project.app.contract.RewardContract;
import com.project.app.fragment.earn.crecode.CouponRecodePFragment;
import com.project.app.presenter.RewardPresenter;
import com.project.app.ui.CustomViewPager;
import com.project.app.ui.TranslatePageTransformer;
import com.project.app.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.core.content.ContextCompat;
import butterknife.BindView;

public class Re_dashboardFragment extends BaseMvpQmuiFragment<RewardPresenter> implements RewardContract.View {
    @BindView(R.id.tv_db_point)
    TextView tv_db_point;
    @BindView(R.id.tb_rewardFun)
    QMUITabSegment mTabSegment;
    @BindView(R.id.vp_rewardFun)
    CustomViewPager vp_container;
    @BindView(R.id.tv_updateEarnT)
    TextView tv_updateEarnT;

    private Context mContext;
    private List<QMUIFragment> mFragments;
    private BaseFragmentAdapter mAdapter;
    private String[] titles = {"Available","Used","History"};
    private HashMap<String,String> mResMap = new HashMap<>();

    public static Re_dashboardFragment newInstance() {
        return new Re_dashboardFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_reward_db;
    }

    @Override
    public void initView() {
        mContext = getContext();
        mPresenter = new RewardPresenter();
        mPresenter.attachView(this);
        initWidget();
        initRes();
    }

    private void initWidget() {
        initTabSegment();
        initViewSprite();
    }

    private void initRes() {
        AppEnvironmentResConfig config = AppEnvironmentResConfig.getInstance(getActivity());
        mResMap = config.initReward();
    }

    private void initTabSegment() {
        titles = getResources().getStringArray(R.array.reward_d_items);
        QMUITabBuilder builder =  mTabSegment.tabBuilder();
        builder.setTypeface(null, Typeface.DEFAULT);
        builder.setNormalColor(mContext.getResources().getColor(R.color.color_bfbf))
                .setSelectColor(mContext.getResources().getColor(R.color.allwees_theme_color))
                .setTextSize(QMUIDisplayHelper.dp2px(mContext,14),QMUIDisplayHelper.dp2px(mContext,16))
                .setDynamicChangeIconColor(false)
                .skinChangeWithTintColor(false);

        for(String item:titles){
            QMUITab tab = builder.build(mContext);
            tab.setText(item);
            mTabSegment.addTab(tab);
        }

        int space = QMUIDisplayHelper.dp2px(mContext,16);
        mTabSegment.setIndicator(new QMUITabIndicator(ContextCompat.getDrawable(mContext,R.drawable.bg_indicator),false,true));
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);
        mTabSegment.setItemSpaceInScrollMode(space);
        mTabSegment.setPadding(space,0,space,0);

        mTabSegment.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {

            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {

            }

            @Override
            public void onDoubleTap(int index) {

            }
        });
        mTabSegment.setupWithViewPager(vp_container,false);
    }

    private void initViewSprite() {
        mFragments = new ArrayList<>();
        for(int i=0;i<titles.length;i++){
            Bundle bundle = new Bundle();
            bundle.putInt("type",i);
            mFragments.add(CouponRecodePFragment.newInstance(bundle));
        }
        mAdapter = new BaseFragmentAdapter(getChildFragmentManager(),mFragments);
        vp_container.setAdapter(mAdapter);
        vp_container.setPageTransformer(true, new TranslatePageTransformer());
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchBindPointInfo();
    }

    @Override
    public void fetchPointSuccess(MeBindCPBean result) {
        if(result != null){
            tv_db_point.setText(doubleTrans(result.getValue())+ " " + mResMap.get("str_points"));
            tv_updateEarnT.setText(StringUtils.getEnMDFormat(result.getUpdatedAt()));
        }
    }

    public static String doubleTrans(double d){
        if(Math.round(d)-d==0){
            return String.valueOf((long)d);
        }
        return String.valueOf(d);
    }

    @Override
    public void fetchRedeemInfoSuccess(RedeemBean result) {

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