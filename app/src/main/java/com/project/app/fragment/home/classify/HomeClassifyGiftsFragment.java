package com.project.app.fragment.home.classify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.other.UserUtil;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.RecycerLoadMoreScrollView;
import com.project.app.config.AppEnvironmentResConfig;
import com.project.app.utils.StringUtils;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.project.app.R;
import com.project.app.activity.LoginActivity;
import com.project.app.adapter.BaseFragmentAdapter;
import com.project.app.adapter.NewFreeGiftAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.FreeGiftCouponBean;
import com.project.app.bean.HomeFreeGiftBean;
import com.project.app.bean.MarketGiftInfoBean;
import com.project.app.contract.NewFreeGiftContract;
import com.project.app.presenter.NewFreeGiftPresenter;
import com.project.app.ui.CustomViewPager;
import com.project.app.ui.TranslatePageTransformer;
import com.project.app.ui.dialog.GDBuyPopupDialogUtil;
import com.project.app.utils.ItemSpaceDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

public class HomeClassifyGiftsFragment extends BaseMvpQmuiFragment<NewFreeGiftPresenter> implements NewFreeGiftContract.View,SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.srl_addMore)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.sv_more_gifts)
    RecycerLoadMoreScrollView sv_more_gifts;
    @BindView(R.id.qmTab_freeGift)
    QMUITabSegment mTabSegment;
    @BindView(R.id.vp_freeGift)
    CustomViewPager vp_container;
    @BindView(R.id.tv_getFreeGift)
    TextView tv_getFreeGift;
    @BindView(R.id.rlv_gifts)
    RecyclerView rlv_gifts;
    @BindView(R.id.tv_applyGiftC)
    TextView tv_applyGiftC;
    @BindView(R.id.ll_ctGift)
    LinearLayout ll_ctGift;
    @BindView(R.id.tv_gift_ct_h)
    TextView tv_gift_ct_h;
    @BindView(R.id.tv_gift_ct_m)
    TextView tv_gift_ct_m;
    @BindView(R.id.tv_gift_ct_s)
    TextView tv_gift_ct_s;
    @BindView(R.id.ll_CexpireS)
    LinearLayout ll_CexpireS;
    @BindView(R.id.tv_cUseStatus)
    TextView tv_cUseStatus;
    @BindView(R.id.tv_cUseStatusT)
    TextView tv_cUseStatusT;
    @BindView(R.id.tv_topTitle)
    TextView tv_topTitle;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    private List<QMUIFragment> mFragments;
    private Context mContext;
    private CountDownTimer timer;
    private FreeGiftCouponBean mFreeGiftCouponBean;
    private BaseFragmentAdapter mAdapter;
    private NewFreeGiftAdapter mNewFreeAdapter;
    private FreeGiftChildFragment mBelowFreeGiftFragment;
    private FreeGiftChildFragment mInfluencerGiftFragment;
    private GDBuyPopupDialogUtil mGdBuyPopDialogUtil;
    private String[] titles = {};
    private List<HomeFreeGiftBean.GiftItem> mGiftDatas = new ArrayList<>();
    private HomeFreeGiftBean.GiftItem mChoiceGift;
    private HashMap<String,String> mResMap = new HashMap<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_free_gifts;
    }

    @Override
    public void initView() {
        mPresenter = new NewFreeGiftPresenter();
        mPresenter.attachView(this);
        initTopbar();
        initWidget();
        initRes();
    }

    private void initRes() {
        AppEnvironmentResConfig config = AppEnvironmentResConfig.getInstance(getActivity());
        mResMap = config.initFreeGift();
    }

    private void initWidget() {
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.color_ff45,android.R.color.holo_blue_dark,android.R.color.holo_orange_dark);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        rlv_gifts.setLayoutManager(manager);
        mNewFreeAdapter = new NewFreeGiftAdapter(mGiftDatas);
        rlv_gifts.setAdapter(mNewFreeAdapter);
        ItemSpaceDecoration spaceDecoration = new ItemSpaceDecoration(getContext());
        spaceDecoration.setLeft(5);
        spaceDecoration.setHideLeft(true);
        rlv_gifts.addItemDecoration(spaceDecoration);
        mGdBuyPopDialogUtil = new GDBuyPopupDialogUtil(getContext(),true,true);

        mNewFreeAdapter.setListener(item -> {
            mChoiceGift = item;
            mGdBuyPopDialogUtil.lazzyImageUrl(item.getMainPhoto());
        });

        sv_more_gifts.setScrollViewListener(new RecycerLoadMoreScrollView.ScrollViewListener() {
            @Override
            public void onLoadMore() {
                if(mBelowFreeGiftFragment != null){
                    mBelowFreeGiftFragment.addMoreLevel();
                }
                if(mInfluencerGiftFragment != null){
                    mInfluencerGiftFragment.addMoreLevel();
                }
            }

            @Override
            public void onScrollChanged(RecycerLoadMoreScrollView scrollView, int x, int y, int oldx, int oldy) {

            }
        });

        initViewSprite();
        initTabSegment();
    }

    private void initTopbar() {
        mContext = getContext();
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
    }

    private void initViewSprite() {
        mFragments = new ArrayList<>();
        mBelowFreeGiftFragment = FreeGiftChildFragment.newInstance("0");
        mInfluencerGiftFragment = FreeGiftChildFragment.newInstance("1");
        mFragments.add(mBelowFreeGiftFragment);
        mFragments.add(mInfluencerGiftFragment);
        mAdapter = new BaseFragmentAdapter(getChildFragmentManager(),mFragments);
        vp_container.setAdapter(mAdapter);
        vp_container.setPageTransformer(true, new TranslatePageTransformer());
    }

    private void initTabSegment() {
        titles = getContext().getResources().getStringArray(R.array.free_bottom_segment);
        QMUITabBuilder builder =  mTabSegment.tabBuilder();
        builder.setTypeface(null, Typeface.DEFAULT);
        builder.setNormalColor(mContext.getResources().getColor(R.color.color_ffa752))
                .setSelectColor(mContext.getResources().getColor(R.color.color_ffa752))
                .setTextSize(QMUIDisplayHelper.dp2px(mContext,14),QMUIDisplayHelper.dp2px(mContext,16))
                .setDynamicChangeIconColor(false)
                .skinChangeWithTintColor(false);

        for(String item:titles){
            QMUITab tab = builder.build(mContext);
            tab.setText(item);
            mTabSegment.addTab(tab);
        }

        int space = QMUIDisplayHelper.dp2px(mContext,16);
        mTabSegment.setIndicator(new QMUITabIndicator(ContextCompat.getDrawable(mContext,R.drawable.bg_rg_indicator),false,true));
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

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchFreeGiftsList();
        if(UserUtil.getInstance().isLogin()){
            mPresenter.fetchFreeGiftStatus();
        }
    }

    @OnClick({R.id.tv_getFreeGift,R.id.tv_applyGiftC,R.id.iv_back})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.tv_getFreeGift:
                if(!UserUtil.getInstance().isLogin()){
                    getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                    return;
                }else{
                    mPresenter.fetchFreeGiftsCouponStatus();
                }
                break;
            case R.id.tv_applyGiftC:
                if(!UserUtil.getInstance().isLogin()){
                    getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                    return;
                }
                if(mChoiceGift == null){
                    ToastUtil.showToast(mResMap.get("free_gift_hint_choose"));
                }else{
                    mPresenter.operationAddGoods(1,true,mChoiceGift.getUuid());
                }
                break;
            case R.id.iv_back:
                popBackStack();
                break;
        }
    }

    /**
     * 设置时间
     */
    private void setTime(long expire) {
        if (expire > 0) {
            timer = new CountDownTimer(expire, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    GetMinutes(millisUntilFinished);
                }
                @Override
                public void onFinish() {
                    tv_gift_ct_h.setText("00");
                    tv_gift_ct_m.setText("00");
                    tv_gift_ct_s.setText("00");
                }
            };
            timer.start();
        }
    }

    public String GetMinutes(long s) {
        s = s/1000;
        String timeStr;
        long hour = 0;
        long minute;
        long second;

        minute = s / 60;
        if (minute < 60) {
            second = s % 60;
            timeStr = unitFormat(minute) + ":" + unitFormat(second);
        } else {
            hour = minute / 60;
            if (hour > 1000)
                return "";
            minute = minute % 60;
            second = s - hour * 3600 - minute * 60;
            timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
        }

        tv_gift_ct_h.setText("" + hour);
        tv_gift_ct_m.setText("" + minute);
        tv_gift_ct_s.setText("" + second);
        return timeStr;
    }

    private static String unitFormat(long i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + i;
        else
            retStr = "" + i;
        return retStr;
    }

    @Override
    public void fetchFreeGiftsCouponSuccess(FreeGiftCouponBean result) {
        if(result != null){
            mFreeGiftCouponBean = result;
            mPresenter.fetchFreeGiftStatus();
        }
    }

    @Override
    public void fetchFreeGiftsStatusSuccess(MarketGiftInfoBean result) {
        if(result != null){
            if(result.getCoupon() == null){
                return;
            }
            MarketGiftInfoBean.CouponItem coupon = result.getCoupon();
            int cStatus = coupon.getStatus();

            if(cStatus == 0) {
                tv_getFreeGift.setVisibility(View.GONE);
                ll_ctGift.setVisibility(View.VISIBLE);
                ll_CexpireS.setVisibility(View.GONE);
                long currentTime = System.currentTimeMillis();
                long overdueTime = result.getCoupon().getOverdueTime();

                if(overdueTime <= currentTime){       //代表过期
                    tv_gift_ct_h.setText("00");
                    tv_gift_ct_m.setText("00");
                    tv_gift_ct_s.setText("00");
                }else{
                    tv_getFreeGift.setVisibility(View.GONE);
                    ll_ctGift.setVisibility(View.VISIBLE);
                    setTime(overdueTime-currentTime);
                }
            }else if(cStatus == 1|| cStatus == 18){
                tv_getFreeGift.setVisibility(View.GONE);
                ll_ctGift.setVisibility(View.GONE);
                ll_CexpireS.setVisibility(View.VISIBLE);
                tv_cUseStatus.setText(mResMap.get("str_use"));
                tv_cUseStatusT.setText(mResMap.get("str_useIn") +  " " + StringUtils.getEnMDFormat(result.getCoupon().getUpdatedAt()));
            }else if(cStatus == 24){
                tv_getFreeGift.setVisibility(View.GONE);
                ll_ctGift.setVisibility(View.GONE);
                ll_CexpireS.setVisibility(View.VISIBLE);
                tv_cUseStatus.setText(mResMap.get("str_expire"));
                tv_cUseStatusT.setText(mResMap.get("str_useIn") + " " + StringUtils.getEnMDFormat(result.getCoupon().getOverdueTime()));
            }
        }
    }

    @Override
    public void fetchGiftsList(HomeFreeGiftBean result) {
        if(result != null){
            mGiftDatas = result.getResults();
            if(DataUtil.idNotNull(result.getResults())){
                mNewFreeAdapter.setNewInstance(result.getResults());
            }
        }
    }

    @Override
    public void addCartSuccess(String msg) {
        mGdBuyPopDialogUtil.show();
        EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_REFRESH_CAR));
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
    public void onRefresh() {
        mSwipeRefresh.setRefreshing(true);
        mSwipeRefresh.postDelayed(() -> {
            if(mSwipeRefresh != null){
                mSwipeRefresh.setRefreshing(false);
            }
        }, Constant.DELAY_LOADING_TIME_OUT);

        if(mBelowFreeGiftFragment != null){
            mBelowFreeGiftFragment.resetLoadData();
        }
        if(mInfluencerGiftFragment != null){
            mInfluencerGiftFragment.resetLoadData();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }
}
