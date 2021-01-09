package com.project.app.fragment.earn;

import android.content.Context;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.View;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
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
import com.project.app.contract.RewardContract;
import com.project.app.fragment.earn.reward.Re_InfomationFragment;
import com.project.app.fragment.earn.reward.Re_RedeemFragment;
import com.project.app.fragment.earn.reward.Re_dashboardFragment;
import com.project.app.presenter.RewardPresenter;
import com.project.app.ui.TranslatePageTransformer;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

public class RewardFragment extends BaseMvpQmuiFragment<RewardPresenter> implements RewardContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.tb_rewardFun)
    QMUITabSegment mTabSegment;
    @BindView(R.id.vp_rewardFun)
    ViewPager vp_container;
    private String[] titles = new String[]{};
    private List<QMUIFragment> mFragments;
    private BaseFragmentAdapter mAdapter;
    private Context mContext;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_reward;
    }

    @Override
    public void initView() {
        initTopbar();
        initWidget();
    }

    private void initWidget() {
        mContext = getContext();
        mPresenter = new RewardPresenter();
        mPresenter.attachView(this);
        initTabSegment();
        initViewSprite();
    }


    private void initTopbar() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
    }

    private void initTabSegment() {
        titles = getResources().getStringArray(R.array.reward_items);
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
        mTabSegment.setMode(QMUITabSegment.MODE_SCROLLABLE);
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
        mFragments.add(new Re_dashboardFragment());
        mFragments.add(new Re_RedeemFragment());
        mFragments.add(new Re_InfomationFragment());

        mAdapter = new BaseFragmentAdapter(getChildFragmentManager(),mFragments);
        vp_container.setAdapter(mAdapter);
        vp_container.setPageTransformer(true, new TranslatePageTransformer());
    }

    @OnClick({R.id.iv_back})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.iv_back:
                popBackStack();
                break;
        }
    }


    @Override
    protected void lazyFetchData() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void fetchPointSuccess(MeBindCPBean result) {

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
