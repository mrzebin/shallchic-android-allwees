package com.project.app.fragment.home.classify;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.image.ImageLoader;
import com.hb.basemodel.other.UserUtil;
import com.hb.basemodel.uicomponent.roundview.RoundLinearLayout;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.SPManager;
import com.hb.basemodel.utils.ToastUtil;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.activity.LoginActivity;
import com.project.app.adapter.HomeActivityAreaAdapter;
import com.project.app.adapter.HomeBannerAdapter;
import com.project.app.adapter.HomeClassifyAdapter;
import com.project.app.adapter.HomeFlashSaleAdapter;
import com.project.app.adapter.HomeFreeGiftAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.ClassifyListBean;
import com.project.app.bean.HomeBannerBean;
import com.project.app.bean.HomeFLashSaleBean;
import com.project.app.bean.HomeFreeGiftBean;
import com.project.app.bean.HomePopularActionBean;
import com.project.app.bean.HomePopularLastOrderBean;
import com.project.app.config.AppsFlyConfig;
import com.project.app.contract.HomePClassifyContract;
import com.project.app.fragment.earn.RewardFragment;
import com.project.app.fragment.home.HomeController;
import com.project.app.fragment.order.OrderDetailFragment;
import com.project.app.fragment.task.LoginTaskCouponFragment;
import com.project.app.presenter.HomePClassifyPresenter;
import com.project.app.ui.widget.GlobalClassicsFooter;
import com.project.app.ui.widget.GlobalClassicsHeader;
import com.project.app.utils.AppsFlyEventUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundLinearLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * create by hb
 * Popular 分类
 */
public class HomePClassifyFragment extends BaseMvpQmuiFragment<HomePClassifyPresenter> implements HomePClassifyContract.View{
    @BindView(R.id.srf_classify)
    SmartRefreshLayout srl_home;
    @BindView(R.id.rlv_popular)
    NestedScrollView rlv_popular;
    @BindView(R.id.rv_classify)
    RecyclerView rv_classify;
    @BindView(R.id.bn_active)
    Banner bn_active;
    @BindView(R.id.ll_bannerContainer)
    LinearLayout ll_bannerContainer;
    @BindView(R.id.iv_home_banner)
    QMUIRadiusImageView iv_home_banner;
    @BindView(R.id.rl_activityAreaWrapper)    //活动专区包装类
    RelativeLayout rl_activityAreaWrapper;
    @BindView(R.id.rlv_activityArea)
    RecyclerView rlv_activityArea;
    @BindView(R.id.rlv_active_freeGift)
    RecyclerView rlv_active_freeGift;
    @BindView(R.id.ll_giftWrapper)
    LinearLayout ll_giftWrapper;
    @BindView(R.id.tv_browseMorefg)
    TextView tv_browseMorefg;
    @BindView(R.id.ll_flashSaleWrpper)
    LinearLayout ll_flashSaleWrpper;
    @BindView(R.id.rlv_fsWrpper)
    RecyclerView rlv_fsWrpper;
    @BindView(R.id.ll_goodsWrapper)
    LinearLayout ll_goodsWrapper;
    @BindView(R.id.tv_moreLoveTag)
    TextView tv_moreLoveTag;
    @BindView(R.id.tv_fsMore)
    TextView tv_fsMore;
    @BindView(R.id.qiv_buyDeliverOne)
    QMUIRadiusImageView qiv_buyDeliverOne;
    @BindView(R.id.tv_flashSaleHour)
    TextView tv_fs_h;
    @BindView(R.id.tv_flashSaleMinute)
    TextView tv_fs_m;
    @BindView(R.id.tv_flashSaleSecond)
    TextView tv_fs_s;
    @BindView(R.id.emptyView)
    QMUIEmptyView emptyView;
    @BindView(R.id.iv_floatGift)
    ImageView iv_floatGift;
    @BindView(R.id.iv_floatUp)
    ImageView iv_floatUp;
    @BindView(R.id.ll_lastOrderWrapper)
    QMUIRoundLinearLayout ll_lastOrderWrapper;
    @BindView(R.id.iv_lastOrderGoods)
    QMUIRadiusImageView iv_lastOrderGoods;
    @BindView(R.id.tv_lastOrderStatus)
    TextView tv_lastOrderStatus;
    @BindView(R.id.tv_lastOrderDescribute)
    TextView tv_lastOrderDescribute;
    @BindView(R.id.ll_floatParent)
    LinearLayout ll_floatParent;
    @BindView(R.id.riv_checkLoginTask)
    QMUIRadiusImageView riv_checkLoginTask;

    private View mNoDateView;
    private String mNo;
    private int mPageSize    = 20;
    private int mCurrentPage = 1;
    private int mFloatLimitHeight = 800;    //判断显示的高度,默认800
    private boolean isLoadMore = false;
    private HomeClassifyAdapter    mAdapter;
    private HomeFreeGiftAdapter    mGiftAdapter;
    private HomeFlashSaleAdapter   mFlashSaleAdapter;
    private HomeBannerAdapter      mBannerAdapter;
    private HomeActivityAreaAdapter mActivityAreaAdapter;
    private CountDownTimer mFsTimer;
    private HomePopularLastOrderBean mLastOrderBean;

    private final List<ClassifyListBean.ClassifyItem> mCategorys = new ArrayList<>();
    private final List<HomeFLashSaleBean.FlashSaleBean> mFlashSales = new ArrayList<>();
    private final List<HomeFreeGiftBean.GiftItem> mGiftDatas = new ArrayList<>();
    private final List<HomePopularActionBean> mActivityAreas = new ArrayList<>();
    private static HomeController.HostMasterListener mMoveListener;

    public static HomePClassifyFragment newInstance(String no, HomeController.HostMasterListener listener) {
        Bundle bundle = new Bundle();
        bundle.putString("type",no);
        HomePClassifyFragment fragment = new HomePClassifyFragment();
        fragment.setArguments(bundle);
        mMoveListener = listener;
        return fragment;
    }

    private View getNoNetView(){
        mAdapter.setNewInstance(new ArrayList<>());    //要把它先置为空
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_no_net,null);
        QMUIRoundButton qrb_refreshData = view.findViewById(R.id.qrb_refreshNet);
        qrb_refreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProgressDialog(getContext());
                lazyFetchData();
            }
        });
        return view;
    }

    private View getAddMoreView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_home_free_gifts_add_more,null);
        RoundLinearLayout goMoreGift = view.findViewById(R.id.rll_home_goMoreGift);
        goMoreGift.setOnClickListener(view1 -> {
            HolderActivity.startFragment(getContext(),HomeClassifyGiftsFragment.class);
            AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_NEW_USER_GIFT_VIEW_MORE);
        });
        return view;
    }

    private View getNoEndView(){
        mNoDateView = LayoutInflater.from(getContext()).inflate(R.layout.layout_footer_no_more,null);
        return mNoDateView;
    }

    private View getEmptyView(){
        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_nogoods,null);
        QMUIRoundButton qrb_refreshAgain = emptyView.findViewById(R.id.qrb_refreshAgain);
        qrb_refreshAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoading();
                lazyFetchData();
            }
        });
        return emptyView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_p_classify_home;
    }

    @Override
    public void initView() {
        mNo = getArguments().getString("type");
        mPresenter = new HomePClassifyPresenter();
        mPresenter.attachView(this);
        initData();
        initViewClick();
        initSmartRefreshLayout();
        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        rv_classify.setLayoutManager(manager);
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        mAdapter = new HomeClassifyAdapter(mCategorys);
        mGiftAdapter = new HomeFreeGiftAdapter(mGiftDatas,dm);
        mGiftAdapter.addFooterView(getAddMoreView());
        rv_classify.setAdapter(mAdapter);

        GridLayoutManager activityAreaManager = new GridLayoutManager(getContext(),2);
        rlv_activityArea.setLayoutManager(activityAreaManager);
        mActivityAreaAdapter = new HomeActivityAreaAdapter(getContext(),mActivityAreas);            //活动专区
        rlv_activityArea.setAdapter(mActivityAreaAdapter);

        LinearLayoutManager giftManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
        rlv_active_freeGift.setLayoutManager(giftManager);
        rlv_active_freeGift.setAdapter(mGiftAdapter);

        LinearLayoutManager fsManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
        rlv_fsWrpper.setLayoutManager(fsManager);
        mFlashSaleAdapter = new HomeFlashSaleAdapter(mFlashSales,dm);
        rlv_fsWrpper.setAdapter(mFlashSaleAdapter);
    }

    private void initViewClick() {
        rlv_fsWrpper.addOnScrollListener(new RecyclerView.OnScrollListener() {
            View firstChild;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView != null && recyclerView.getChildCount() > 0) {
                    firstChild = recyclerView.getChildAt(0);
                }
                int firstChildPosition = firstChild == null ? 0 : recyclerView.getChildLayoutPosition(firstChild);
//                mSwipeRefresh.setEnabled(firstChildPosition == 0 && firstChild.getTop()>=0);
            }
        });

        rlv_active_freeGift.addOnScrollListener(new RecyclerView.OnScrollListener() {
            View firstChild;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView != null && recyclerView.getChildCount() > 0) {
                    firstChild = recyclerView.getChildAt(0);
                }
                int firstChildPosition = firstChild == null ? 0 : recyclerView.getChildLayoutPosition(firstChild);
            }
        });

        srl_home.setOnTouchListener(new View.OnTouchListener() {
            private float mEndY;
            private float mStartY;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mStartY = event.getY();
                        break;
                    case MotionEvent.ACTION_HOVER_MOVE:
                        mEndY = event.getY();
                        if(mEndY - mStartY > 50){    //上滑隐藏
                            mMoveListener.moveUpHideTabSegment();
                        }else if(mEndY - mStartY < 50){  //下滑显示

                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return gestureDetector.onTouchEvent(event);
            }
        });

        rlv_popular.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY >= mFloatLimitHeight){
                    ll_floatParent.setVisibility(View.VISIBLE);
                    if(!SPManager.sGetBoolean(Constant.SP_HOME_POPULAR_FLOAT_GIFT)){   //已领为true 未领为false
                        iv_floatGift.setVisibility(View.VISIBLE);
                    }else{
                        iv_floatGift.setVisibility(View.GONE);
                    }
                }else{
                    ll_floatParent.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initData() {
        mFloatLimitHeight = (int) (QMUIDisplayHelper.getScreenHeight(getContext()));
    }

    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    });

    private void initSmartRefreshLayout() {
        srl_home.setRefreshHeader(new GlobalClassicsHeader(getContext()));
        srl_home.setRefreshFooter(new GlobalClassicsFooter(getContext()));
        srl_home.setHeaderHeight(60);
        srl_home.setFooterHeight(50);
        srl_home.setEnableLoadMore(false); //默认不让起有更多数据

        srl_home.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mCurrentPage = 1;
                isLoadMore = false;
                srl_home.finishRefresh(Constant.DEFAULT_SMARTLOADING_DELAY_TIMEOUT);
                lazyFetchData();
            }
        });

        srl_home.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (isLoadMore) {
                    mCurrentPage++;
                    srl_home.finishLoadMore(Constant.DEFAULT_SMARTLOADING_DELAY_TIMEOUT);
                    mPresenter.fetchGoodsInfo(mCurrentPage,mPageSize,mNo);
                }else{
                    srl_home.finishLoadMore();
                }
            }
        });
    }

    @Override
    public void fetchNetWorkState(boolean unknowNet) {
        if(!unknowNet){
            checkEmptyView();
            ll_bannerContainer.setVisibility(View.GONE);    //隐藏banner
            ll_giftWrapper.setVisibility(View.GONE);        //隐藏礼物
            rl_activityAreaWrapper.setVisibility(View.GONE);    //隐藏活动专区
            ll_flashSaleWrpper.setVisibility(View.GONE);
            ll_goodsWrapper.setVisibility(View.VISIBLE);
            tv_moreLoveTag.setVisibility(View.GONE);
            mAdapter.setEmptyView(getNoNetView());
        }
    }

    @Override
    public void checkEmptyView(){
        if(emptyView.getVisibility() == View.VISIBLE){
            emptyView.setVisibility(View.GONE);
            srl_home.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void lazyFetchData() {
        mPresenter.fetchHomePopularMax();
        mPresenter.fetchHomeAction();
        if(UserUtil.getInstance().isLogin()){
            mPresenter.fetchLastOrderInfo();
        }else{
            if(ll_lastOrderWrapper.getVisibility() == View.VISIBLE){
                ll_lastOrderWrapper.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.tv_browseMorefg,R.id.iv_home_banner,R.id.tv_fsMore,R.id.qiv_buyDeliverOne,R.id.iv_floatUp,R.id.ll_lastOrderWrapper,R.id.iv_floatGift,R.id.riv_checkLoginTask})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.tv_browseMorefg:
                HolderActivity.startFragment(getContext(),HomeClassifyGiftsFragment.class);
                AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_NEW_USER_GIFT_MORE);
                break;
            case R.id.iv_home_banner:
                if(UserUtil.getInstance().isLogin()){
                    HolderActivity.startFragment(getContext(),RewardFragment.class);
                }else{
                    getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                }
                break;
            case R.id.tv_fsMore:
                AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_FLASH_SALE_MORE);
                HolderActivity.startFragment(getContext(),FlashSaleFragment.class);
                break;
            case R.id.qiv_buyDeliverOne:
                HolderActivity.startFragment(getContext(),DeliverDoubleFragment.class);
                break;
            case R.id.iv_floatUp:
                srl_home.finishLoadMore();
                rlv_popular.smoothScrollTo(0,0);
                break;
            case R.id.ll_lastOrderWrapper:
                if(mLastOrderBean != null){
                    Bundle orderBundle = new Bundle();
                    orderBundle.putString("orderId",mLastOrderBean.getOrderUuid());
                    HolderActivity.startFragment(getContext(), OrderDetailFragment.class,orderBundle);
                }
                break;
            case R.id.iv_floatGift:
                if(!UserUtil.getInstance().isLogin()){
                    Intent goLogin = new Intent(getContext(), LoginActivity.class);
                    getContext().startActivity(goLogin);
                }else{
                    HolderActivity.startFragment(getContext(),HomeClassifyGiftsFragment.class);
                }
                break;
            case R.id.riv_checkLoginTask:
                if(!UserUtil.getInstance().isLogin()){
                    Intent goLogin = new Intent(getContext(), LoginActivity.class);
                    getContext().startActivity(goLogin);
                }else{
                    HolderActivity.startFragment(getContext(), LoginTaskCouponFragment.class);
                }
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        bn_active.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        bn_active.stop();
    }

    private void expireFlash(long expireTime){
        mFsTimer = new CountDownTimer(expireTime,1000) {
            @Override
            public void onTick(long residueTime) {
                GetMinutes(residueTime);
            }
            @Override
            public void onFinish() {
                tv_fs_h.setText("00");
                tv_fs_m.setText("00");
                tv_fs_s.setText("00");
            }
        };
        mFsTimer.start();
    }

    public String GetMinutes(long remainT) {
        remainT = remainT/1000;
        String timeStr;
        long hour = 0;
        long minute;
        long second;

        minute = remainT / 60;
        if (minute < 60) {
            second = remainT % 60;
            timeStr = unitFormat(minute) + ":" + unitFormat(second);
        } else {
            hour = minute / 60;
            if (hour > 1000)
                return "";
            minute = minute % 60;
            second = remainT - hour * 3600 - minute * 60;
            timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
        }
        tv_fs_h.setText("" + hour);
        tv_fs_m.setText("" + minute);
        tv_fs_s.setText("" + second);
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
    public void fetchCategoryList(ClassifyListBean result) {
        if(result != null){
            srl_home.finishLoadMore();
            isLoadMore = result.isHasMorePages();
            ll_goodsWrapper.setVisibility(View.VISIBLE);
            if(mCurrentPage == 1){
                if(DataUtil.idNotNull(result.getResults())){
                   mAdapter.setNewInstance(result.getResults());
                }
            }else{
                mAdapter.addData(result.getResults());
            }
        }
    }

    @Override
    public void fetchHomePopularMaxSuccess(String result) {
        if(result == null){
            return;
        }
        try {
            JSONObject allObj = new JSONObject(result);
            //首页的banner图,获取
            if(result.contains("ads")){
                String homeBannerJson = allObj.getString("ads");
                if(!TextUtils.isEmpty(homeBannerJson) && !homeBannerJson.equals("[]")){
                    List<HomeBannerBean> homeBanner = JsonUtils.deserializeList(homeBannerJson,HomeBannerBean[].class);
                    if(homeBanner != null){
                        if(DataUtil.idNotNull(homeBanner)){
                            ll_bannerContainer.setVisibility(View.VISIBLE);
                            iv_home_banner.setVisibility(View.GONE);
                            bn_active.setVisibility(View.VISIBLE);
                            bn_active.isAutoLoop(true);
                            mBannerAdapter = new HomeBannerAdapter(getContext(),homeBanner);
                            bn_active.setAdapter(mBannerAdapter)
                                    .setIndicator(new CircleIndicator(getContext()))
                                    .addBannerLifecycleObserver(this);
                        }else{
                            ll_bannerContainer.setVisibility(View.VISIBLE);
                            iv_home_banner.setVisibility(View.VISIBLE);
                            bn_active.setVisibility(View.GONE);
                        }
                    }
                }else{
                    ll_bannerContainer.setVisibility(View.VISIBLE);
                    iv_home_banner.setVisibility(View.VISIBLE);
                    bn_active.setVisibility(View.GONE);
                }
            }

            if(result.contains("gifts")){ //免费领取商品
                String homeGiftJson = allObj.getString("gifts");
                if(!TextUtils.isEmpty(homeGiftJson) && !homeGiftJson.equals("[]") ){
                    HomeFreeGiftBean homeGift = JsonUtils.deserialize(homeGiftJson,HomeFreeGiftBean.class);
                    if(DataUtil.idNotNull(homeGift.getResults())){
                        ll_giftWrapper.setVisibility(View.VISIBLE);
                        rlv_active_freeGift.setVisibility(View.VISIBLE);
                        List<HomeFreeGiftBean.GiftItem> reverseDatas = new ArrayList<>();
                        HomeFreeGiftBean.GiftItem item = new HomeFreeGiftBean.GiftItem();       //get free gift
                        item.setMainPhoto("-1");
                        reverseDatas.add(item);
                        reverseDatas.addAll(homeGift.getResults());
                        mGiftAdapter.setNewInstance(reverseDatas);
                    }else{
                        ll_giftWrapper.setVisibility(View.GONE);
                        rlv_active_freeGift.setVisibility(View.GONE);
                    }
                }
            }

            //闪购
            if(result.contains("flash")){
                String homeFlasJson = allObj.getString("flash");
                if(!TextUtils.isEmpty(homeFlasJson) && !homeFlasJson.equals("[]") ){
                    HomeFLashSaleBean homeFlash = JsonUtils.deserialize(homeFlasJson,HomeFLashSaleBean.class);
                    if(DataUtil.idNotNull(homeFlash.getResults()) && homeFlash.getRemainMillis() >0){
                        ll_flashSaleWrpper.setVisibility(View.VISIBLE);
                        mFlashSaleAdapter.setNewInstance(homeFlash.getResults());
                        expireFlash(homeFlash.getRemainMillis());
                    }else{
                        ll_flashSaleWrpper.setVisibility(View.GONE);
                    }
                }
            }

            if(result.contains("buyAndFreeUrl")){
                String deliverUrl = allObj.getString("buyAndFreeUrl");
                if(!TextUtils.isEmpty(deliverUrl)){
                    qiv_buyDeliverOne.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(qiv_buyDeliverOne,deliverUrl,R.mipmap.allwees_ic_default_goods);
                }else{
                    qiv_buyDeliverOne.setVisibility(View.GONE);
                }
            }

            if(result.contains("P01")){
                String homePopluarJson = allObj.getString("P01");
                if(!TextUtils.isEmpty(homePopluarJson) && !homePopluarJson.equals("[]")){
                    ClassifyListBean homePopluar =  JsonUtils.deserialize(homePopluarJson,ClassifyListBean.class);
                    if(homePopluar!= null){
                        riv_checkLoginTask.setVisibility(View.VISIBLE);
                        ll_goodsWrapper.setVisibility(View.VISIBLE);
                        tv_moreLoveTag.setVisibility(View.VISIBLE);
                        isLoadMore = homePopluar.isHasMorePages();
                        srl_home.setEnableLoadMore(isLoadMore);
                        if(mCurrentPage == 1){
                            if(DataUtil.idNotNull(homePopluar.getResults())){
                                mAdapter.setNewInstance(homePopluar.getResults());
                                if(!isLoadMore && !mAdapter.hasFooterLayout()){
                                    mAdapter.addFooterView(getNoEndView());
                                }
                            }else{
                                mAdapter.setEmptyView(getEmptyView());
                            }
                        }else{
                            mAdapter.addData(homePopluar.getResults());
                            if(!isLoadMore && !mAdapter.hasFooterLayout()){
                                mAdapter.addFooterView(getNoEndView());
                            }
                        }
                    }
                }
            }else{
                riv_checkLoginTask.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fetchHomeActionMaxSuccess(String result) {
        if(!TextUtils.isEmpty(result)){
            if(result.contains("items")){    //活动专区有数据
                rl_activityAreaWrapper.setVisibility(View.VISIBLE);
                List<HomePopularActionBean> realAtionList  =  JsonUtils.deserializeList(result,HomePopularActionBean[].class);
                if(DataUtil.idNotNull(realAtionList)){
                    mActivityAreaAdapter.setNewInstance(realAtionList);
                }
            }else{                           //活动专区没有数据
                rl_activityAreaWrapper.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void fetchLastOrdersSuccess(HomePopularLastOrderBean lastOrderBean) {    //获取最新的订单信息
        if(lastOrderBean == null){
            return;
        }
        mLastOrderBean = lastOrderBean;
        ll_lastOrderWrapper.setVisibility(View.VISIBLE);

        if(!TextUtils.isEmpty(lastOrderBean.getSkuPhoto())){
            ImageLoader.getInstance().displayImage(iv_lastOrderGoods,lastOrderBean.getSkuPhoto(),R.mipmap.allwees_ic_default_goods);
        }
        if(!TextUtils.isEmpty(lastOrderBean.getOrderState())){
            tv_lastOrderStatus.setText(lastOrderBean.getOrderState());
        }
        if(!TextUtils.isEmpty(lastOrderBean.getOrderDesc())){
            tv_lastOrderDescribute.setText(lastOrderBean.getOrderDesc());
        }
    }

    @Override
    public void fetchFail(String failReason) {
        ToastUtil.showToast(failReason);
    }

    @Override
    public void startLoading() {
        startProgressDialog(getContext());
    }

    @Override
    public void stopLoading() {
        stopProgressDialog();
    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(bn_active != null){
            bn_active.destroy();
            mFsTimer.cancel();
            mFsTimer = null;
        }
        if(mPresenter.isViewAttach()){
            mPresenter.onDestoryView();
            mPresenter = null;
            System.gc();
        }
    }
}
