package com.project.app.fragment.earn;

import android.content.Context;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.project.app.bean.ScCashBean;
import com.project.app.contract.ScContract;
import com.project.app.fragment.earn.sc_c.ScHistoryFragment;
import com.project.app.fragment.earn.sc_c.ScInfoFragment;
import com.project.app.presenter.ShareCashPresenter;
import com.project.app.ui.CustomViewPager;
import com.project.app.ui.TranslatePageTransformer;
import com.project.app.utils.MathUtil;
import com.project.app.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

public class SchallCashFragment extends BaseMvpQmuiFragment<ShareCashPresenter> implements ScContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.tv_tb_title)
    TextView tv_tb_title;
    @BindView(R.id.iv_tb_back)
    ImageView iv_tb_back;
    @BindView(R.id.ts_sc_fun)
    QMUITabSegment mTabSegment;
    @BindView(R.id.vp_sc)
    CustomViewPager vp_container;
    @BindView(R.id.tv_cashTotal)
    TextView tv_cashTotal;
    @BindView(R.id.ll_inflate)
    LinearLayout ll_inflate;

    private Context mContext;
    private ScCashBean mScContainer;
    private final int mPageSize     = 20;
    private final int mCurrentIndex = 1;
    private int mCurrentPage  = 0;
    private BaseFragmentAdapter mAdapter;
    private List<QMUIFragment> mChildFragments;
    private ScInfoFragment mScInfoFragment;
    private ScHistoryFragment mScHistoryFragment;
    private String[] titles = {"Info","History"};

    @Override
    public int getLayoutId() {
        return R.layout.fragment_shall_cast;
    }

    @Override
    public void initView() {
        initTopbar();
        initWidget();
    }

    private void initWidget() {
        mContext = getContext();
        mChildFragments = new ArrayList<>();
        mScInfoFragment = ScInfoFragment.newInstance();
        mScHistoryFragment = ScHistoryFragment.newInstance();
        mChildFragments.add(mScInfoFragment);
        mChildFragments.add(mScHistoryFragment);
        mPresenter = new ShareCashPresenter();
        mPresenter.attachView(this);
        initTabSegment();
        initViewSprite();
    }

    private void initTopbar() {
        QMUIStatusBarHelper.setStatusBarDarkMode(getActivity());
        StatusBarUtils.setStatusBarView(getContext(),ll_inflate);
        titles = getResources().getStringArray(R.array.sc_cash_segment);
    }

    private void initViewSprite() {
        mAdapter = new BaseFragmentAdapter(getChildFragmentManager(),mChildFragments);
        vp_container.setAdapter(mAdapter);
        vp_container.setPageTransformer(true, new TranslatePageTransformer());
    }

    private void initTabSegment() {
        QMUITabBuilder builder =  mTabSegment.tabBuilder();
        builder.setTypeface(null, Typeface.DEFAULT);
        builder.setNormalColor(mContext.getResources().getColor(R.color.white))
                .setSelectColor(mContext.getResources().getColor(R.color.white))
                .setTextSize(QMUIDisplayHelper.dp2px(mContext,14),QMUIDisplayHelper.dp2px(mContext,14))
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
                mCurrentPage = index;
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

    @OnClick({R.id.iv_tb_back})
    public void onClickViewed(View view){
        if (view.getId() == R.id.iv_tb_back) {
            popBackStack();
        }
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchScHistory();
        mPresenter.fetchBindCashInfo();
    }

    @Override
    public void fetchCashSuccess(MeBindCPBean result) {
        if(result != null){
            tv_cashTotal.setText(MathUtil.Companion.formatPrice(result.getValue()));
        }
    }

    @Override
    public void fetchSuccess(ScCashBean result) {
        if(result != null){
            mScContainer = result;
            inflateDataToView();
        }
    }

    private void inflateDataToView() {
        if(mScHistoryFragment != null){
            mScHistoryFragment.syncData(mScContainer.getResults(),mCurrentIndex);
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.fetchScHistory();
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
