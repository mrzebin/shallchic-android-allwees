package com.project.app.fragment.home.classify;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.image.ImageLoader;
import com.hb.basemodel.other.UserUtil;
import com.hb.basemodel.uicomponent.roundview.RoundLinearLayout;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.RecycerLoadMoreScrollView;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.activity.LoginActivity;
import com.project.app.adapter.HomeBannerAdapter;
import com.project.app.adapter.HomeClassifyAdapter;
import com.project.app.adapter.HomeFlashSaleAdapter;
import com.project.app.adapter.HomeFreeGiftAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.ActionFreeOneBean;
import com.project.app.bean.ClassifyListBean;
import com.project.app.bean.HomeBannerBean;
import com.project.app.bean.HomeFLashSaleBean;
import com.project.app.bean.HomeFreeGiftBean;
import com.project.app.contract.HomePClassifyContract;
import com.project.app.fragment.earn.RewardFragment;
import com.project.app.fragment.home.HomeController;
import com.project.app.presenter.HomePClassifyPresenter;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * create by hb
 * Popular 分类
 */
public class HomePClassifyFragment extends BaseMvpQmuiFragment<HomePClassifyPresenter> implements HomePClassifyContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.srf_classify)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.rv_classify)
    RecyclerView rv_classify;
    @BindView(R.id.bn_active)
    Banner bn_active;
    @BindView(R.id.rlv_goods)
    RecycerLoadMoreScrollView rlv_goods;
    @BindView(R.id.ll_endNoData)
    LinearLayout ll_endNoData;
    @BindView(R.id.ll_bannerContainer)
    LinearLayout ll_bannerContainer;
    @BindView(R.id.iv_home_banner)
    QMUIRadiusImageView iv_home_banner;
    @BindView(R.id.rlv_active_freeGift)
    RecyclerView rlv_active_freeGift;
    @BindView(R.id.ll_giftWrapper)
    LinearLayout ll_giftWrapper;
    @BindView(R.id.tv_browseMorefg)
    TextView tv_browseMorefg;
    @BindView(R.id.tv_moreLoveTag)
    TextView tv_moreLoveTag;
    @BindView(R.id.ll_flashSale)
    LinearLayout ll_flashSale;
    @BindView(R.id.rlv_fsWrpper)
    RecyclerView rlv_fsWrpper;
    @BindView(R.id.tv_fsMore)   //查看更多flashSale
    TextView tv_fsMore;
    @BindView(R.id.qiv_buyDeliverOne)  //买一送一
    QMUIRadiusImageView qiv_buyDeliverOne;
    @BindView(R.id.tv_fs_h)
    TextView tv_fs_h;
    @BindView(R.id.tv_fs_m)
    TextView tv_fs_m;
    @BindView(R.id.tv_fs_s)
    TextView tv_fs_s;

    private String mNo;
    private int mPageSize    = 20;
    private int mCurrentPage = 1;
    private boolean isLoadMoreEnable = false;
    private HomeClassifyAdapter    mAdapter;
    private HomeFreeGiftAdapter    mGiftAdapter;
    private HomeFlashSaleAdapter   mFlashSaleAdapter;
    private CountDownTimer mFsTimer;

    private final List<ClassifyListBean.ClassifyItem> mCategorys = new ArrayList<>();
    private final List<HomeFLashSaleBean.FlashSaleBean> mFlashSales = new ArrayList<>();
    private final List<HomeFreeGiftBean.GiftItem> mGiftDatas = new ArrayList<>();

    public static HomePClassifyFragment newInstance(String no, HomeController.HostMasterListener listener) {
        Bundle bundle = new Bundle();
        bundle.putString("type",no);
        HomePClassifyFragment fragment = new HomePClassifyFragment();
        fragment.setArguments(bundle);
        return fragment;
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
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.allwees_theme_color,android.R.color.holo_blue_dark,android.R.color.holo_orange_dark);
        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        rv_classify.setLayoutManager(manager);
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        mAdapter = new HomeClassifyAdapter(mCategorys);
        mGiftAdapter = new HomeFreeGiftAdapter(mGiftDatas,dm);
        mGiftAdapter.addFooterView(getAddMoreView());
        rv_classify.setAdapter(mAdapter);
        
        LinearLayoutManager giftManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        rlv_active_freeGift.setLayoutManager(giftManager);
        rlv_active_freeGift.setAdapter(mGiftAdapter);
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
                mSwipeRefresh.setEnabled(firstChildPosition == 0 && firstChild.getTop()>=0);
            }
        });

        LinearLayoutManager fsManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        rlv_fsWrpper.setLayoutManager(fsManager);
        mFlashSaleAdapter = new HomeFlashSaleAdapter(mFlashSales,dm);
        rlv_fsWrpper.setAdapter(mFlashSaleAdapter);

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
                mSwipeRefresh.setEnabled(firstChildPosition == 0 && firstChild.getTop()>=0);
            }
        });

        rlv_goods.setScrollViewListener(new RecycerLoadMoreScrollView.ScrollViewListener() {
            @Override
            public void onLoadMore() {
                if (isLoadMoreEnable) {
                    mCurrentPage++;
                    mPresenter.fetchGoodsInfo(mCurrentPage,mPageSize,mNo);
                }
            }
            @Override
            public void onScrollChanged(RecycerLoadMoreScrollView scrollView, int x, int y, int oldx, int oldy) {

            }
        });
    }

    @Override
    public void fetchNetWorkState(boolean unknowNet) {
        if(!unknowNet){
            ll_bannerContainer.setVisibility(View.GONE);
            ll_giftWrapper.setVisibility(View.GONE);
            mAdapter.setEmptyView(getNoNetView());
        }
    }

    private View getNoNetView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_no_net,null);
        QMUIRoundButton qrb_refreshData = view.findViewById(R.id.qrb_refreshNet);
        qrb_refreshData.setOnClickListener(view1 -> mPresenter.fetchGoodsInfo(mCurrentPage,mPageSize,mNo));
        return view;
    }

    private View getAddMoreView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_home_free_gifts_add_more,null);
        RoundLinearLayout goMoreGift = view.findViewById(R.id.rll_home_goMoreGift);
        goMoreGift.setOnClickListener(view1 -> {
            Intent goFreeG  = HolderActivity.of(getContext(),HomeClassifyGiftsFragment.class);
            getContext().startActivity(goFreeG);
        });
        return view;
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchHomePopularMax();
        mSwipeRefresh.setRefreshing(true);
    }

    @OnClick({R.id.tv_browseMorefg,R.id.iv_home_banner,R.id.tv_fsMore,R.id.qiv_buyDeliverOne})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.tv_browseMorefg:
                Intent goFreeG  = HolderActivity.of(getContext(),HomeClassifyGiftsFragment.class);
                getContext().startActivity(goFreeG);
            case R.id.iv_home_banner:
                if(UserUtil.getInstance().isLogin()){
                    Intent goReward = HolderActivity.of(getContext(), RewardFragment.class);
                    getContext().startActivity(goReward);
                }else{
                    getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                }
                break;
            case R.id.tv_fsMore:
                Intent goFlashSale  = HolderActivity.of(getContext(), FlashSaleFragment.class);
                getContext().startActivity(goFlashSale);
                break;
            case R.id.qiv_buyDeliverOne:
                Intent goDoubleBuy  = HolderActivity.of(getContext(), DeliverDoubleFragment.class);
                getContext().startActivity(goDoubleBuy);
                break;
        }
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        lazyFetchData();
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
    public void fetchFreeGiftSuccess(HomeFreeGiftBean result) {
        if(result != null){
            if(DataUtil.idNotNull(result.getResults())){
                ll_giftWrapper.setVisibility(View.VISIBLE);
                rlv_active_freeGift.setVisibility(View.VISIBLE);
                List<HomeFreeGiftBean.GiftItem> reverseDatas = new ArrayList<>();
                HomeFreeGiftBean.GiftItem item = new HomeFreeGiftBean.GiftItem();       //get free gift
                item.setMainPhoto("-1");
                reverseDatas.add(item);
                reverseDatas.addAll(result.getResults());
                mGiftAdapter.setNewInstance(reverseDatas);
            }else{
                ll_giftWrapper.setVisibility(View.GONE);
                rlv_active_freeGift.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void fetchCategoryList(ClassifyListBean result) {
        if(result != null){
            tv_moreLoveTag.setVisibility(View.VISIBLE);
            if(mCurrentPage == 1){
                if(DataUtil.idNotNull(result.getResults())){
                   mAdapter.setNewInstance(result.getResults());
                }
            }else{
                mAdapter.addData(result.getResults());
            }
            isLoadMoreEnable = result.isHasMorePages();
            if(!isLoadMoreEnable){
                ll_endNoData.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 成功获取banner数据
     * @param result
     */
    @Override
    public void fetchBannerSuccess(HomeBannerBean result) {

    }

    @Override
    public void fetchFlashSaleSuccess(HomeFLashSaleBean result) {
        if(result != null){
            if(DataUtil.idNotNull(result.getResults()) && result.getRemainMillis() >0){
                ll_flashSale.setVisibility(View.VISIBLE);
                mFlashSaleAdapter.setNewInstance(result.getResults());
                expireFlash(result.getRemainMillis());
            }else{
                ll_flashSale.setVisibility(View.GONE);
            }
        }
    }

    //买一送一
    @Override
    public void fetchActionFreeOneSuccess(ActionFreeOneBean result) {

    }

    @Override
    public void fetchHomePopularMaxSuccess(String result) {
        mSwipeRefresh.setRefreshing(false);

        if(result == null){
            return;
        }
        try {
            JSONObject allObj = new JSONObject(result);
            //首页的banner图,获取
            String homeBannerJson = allObj.getString("ads");
            if(!TextUtils.isEmpty(homeBannerJson) && !homeBannerJson.equals("[]")){
                List<HomeBannerBean> homeBanner = JsonUtils.deserializeList(homeBannerJson,HomeBannerBean[].class);
                if(homeBanner != null){
                    if(DataUtil.idNotNull(homeBanner)){
                        ll_bannerContainer.setVisibility(View.VISIBLE);
                        iv_home_banner.setVisibility(View.GONE);
                        bn_active.setVisibility(View.VISIBLE);
                        HomeBannerAdapter  nBannerAdapter = new HomeBannerAdapter(getContext(),homeBanner);
                        bn_active.setAdapter(nBannerAdapter)
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

            //免费领取商品
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

            //闪购
            String homeFlasJson = allObj.getString("flash");
            if(!TextUtils.isEmpty(homeFlasJson) && !homeFlasJson.equals("[]") ){
                HomeFLashSaleBean homeFlash = JsonUtils.deserialize(homeFlasJson,HomeFLashSaleBean.class);
                if(DataUtil.idNotNull(homeFlash.getResults()) && homeFlash.getRemainMillis() >0){
                    ll_flashSale.setVisibility(View.VISIBLE);
                    mFlashSaleAdapter.setNewInstance(homeFlash.getResults());
                    expireFlash(homeFlash.getRemainMillis());
                }else{
                    ll_flashSale.setVisibility(View.GONE);
                }
            }

            if(result.contains("buyAndFreeUrl")){
                String deliverUrl = allObj.getString("buyAndFreeUrl");
                if(!TextUtils.isEmpty(deliverUrl)){
                    qiv_buyDeliverOne.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(qiv_buyDeliverOne,deliverUrl + Constant.mGlobalThumbnailStyle,R.mipmap.allwees_ic_default_goods);
                }else{
                    qiv_buyDeliverOne.setVisibility(View.GONE);
                }
            }

            String homePopluarJson = allObj.getString("P01");
            if(!TextUtils.isEmpty(homePopluarJson) && !homePopluarJson.equals("[]")){
                ClassifyListBean homePopluar =  JsonUtils.deserialize(homePopluarJson,ClassifyListBean.class);
                if(homePopluar!= null){
                    tv_moreLoveTag.setVisibility(View.VISIBLE);
                    if(mCurrentPage == 1){
                        if(DataUtil.idNotNull(homePopluar.getResults())){
                            mAdapter.setNewInstance(homePopluar.getResults());
                        }
                    }else{
                        mAdapter.addData(homePopluar.getResults());
                    }
                    isLoadMoreEnable = homePopluar.isHasMorePages();
                    if(!isLoadMoreEnable){
                        ll_endNoData.setVisibility(View.VISIBLE);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fetchFlashSaleFail(String result) {

    }

    @Override
    public void fetchFail(String failReason) {
        ToastUtil.showToast(failReason);
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void startLoading() {
        mSwipeRefresh.setRefreshing(true);
    }

    @Override
    public void stopLoading() {
        mSwipeRefresh.setRefreshing(false);
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
    }
}
