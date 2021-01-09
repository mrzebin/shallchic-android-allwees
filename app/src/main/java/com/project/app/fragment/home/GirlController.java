package com.project.app.fragment.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.other.UserUtil;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.RecycerLoadMoreScrollView;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.project.app.R;
import com.project.app.adapter.WishlistAdapter;
import com.project.app.base.BaseMvpController;
import com.project.app.bean.WishDataBean;
import com.project.app.contract.WishlistContract;
import com.project.app.presenter.WishListPresenter;
import com.project.app.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GirlController extends BaseMvpController<WishListPresenter> implements WishlistContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.qmTopBar)
    QMUITopBarLayout qmTopBar;
    @BindView(R.id.fl_inflate)
    FrameLayout fl_inflate;
    @BindView(R.id.srf_girl)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.rv_girl)
    RecyclerView rv_girl;
    @BindView(R.id.rlv_girl)
    RecycerLoadMoreScrollView rlv_girl;
    @BindView(R.id.ll_endNoData)
    LinearLayout ll_endNoData;
    
    private final int mPageSize    = 20;
    private int mCurrentPage = 1;
    private boolean isPrepared = false;
    private boolean isLoadMore = false;
    private WishlistAdapter mAdapter;
    private final List<WishDataBean.ProductDtoWrapp> mCategorys = new ArrayList<>();

    public GirlController(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.girl_layout,this);
        ButterKnife.bind(this,view);
        initTopBar();
        initView();
        onRefreshView();
    }

    @Override
    public void onRefresh() {
        isPrepared = false;
        mCurrentPage = 1;
        if(UserUtil.getInstance().isLogin()){
            mPresenter.fetchWishList(mCurrentPage,mPageSize);
        }else{
            mAdapter.setEmptyView(getEmptyView());
        }
        initLoadStyle();
    }

    public void onRefreshView() {
        if(UserUtil.getInstance().isLogin()){
            if(!isPrepared){
                mPresenter.fetchWishList(mCurrentPage,mPageSize);
            }
        }else{
            rv_girl.setAdapter(mAdapter);
            mAdapter.setEmptyView(getEmptyView());
        }
        initLoadStyle();
    }


    public void syncFavorite(){
        if(UserUtil.getInstance().isLogin()){
            mPresenter.fetchWishList(mCurrentPage,mPageSize);
        }
    }

    private void initLoadStyle(){
        if(!isPrepared){
            mSwipeRefresh.setRefreshing(true);
        }
        mSwipeRefresh.postDelayed(() -> {
            if(mSwipeRefresh != null){
                mSwipeRefresh.setRefreshing(false);
            }
        },Constant.DELAY_LOADING_TIME_OUT);
    }

    private void initView() {
        mPresenter = new WishListPresenter();
        mPresenter.attachView(this);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.allwees_theme_color,android.R.color.holo_blue_dark,android.R.color.holo_orange_dark);
        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        rv_girl.setLayoutManager(manager);
        mAdapter = new WishlistAdapter(mCategorys);
        mAdapter.setAnimationEnable(false);
        rv_girl.setAdapter(mAdapter);

        rlv_girl.setScrollViewListener(new RecycerLoadMoreScrollView.ScrollViewListener() {
            @Override
            public void onLoadMore() {
                if (isLoadMore) {
                    mCurrentPage++;
                    if(UserUtil.getInstance().isLogin()){
                        mPresenter.fetchWishList(mCurrentPage,mPageSize);
                    }
                }
            }
            @Override
            public void onScrollChanged(RecycerLoadMoreScrollView scrollView, int x, int y, int oldx, int oldy) {

            }
        });
    }

    private void initTopBar() {
        String title = getContext().getString(R.string.wishlist_title);
        qmTopBar.setTitle(title);
        StatusBarUtils.setStatusBarView(getContext(),fl_inflate);
    }

    private View getEmptyView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_wishlist_nodata,null);
        QMUIRoundButton qrb_goShipping = view.findViewById(R.id.qrb_goShipping);
        qrb_goShipping.setOnClickListener(view1 -> mHomeControlListener.startIntent());
        return view;
    }

    private View getNoNetView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_no_net,null);
        QMUIRoundButton qrb_refreshData = view.findViewById(R.id.qrb_refreshNet);
        qrb_refreshData.setOnClickListener(view1 -> {
            if(UserUtil.getInstance().isLogin()){
                if(!isPrepared){
                    mPresenter.fetchWishList(mCurrentPage,mPageSize);
                }
            }
        });
        return view;
    }

    public void syncExitApp() {
        List<WishDataBean.ProductDtoWrapp> mExitData = new ArrayList<>();
        mAdapter.setNewInstance(mExitData);   //清理之前的数据
        mAdapter.setEmptyView(getEmptyView());
    }

    @Override
    public void fetchSuccess(WishDataBean result) {
        isPrepared = true;
        if(result != null){
            if(mCurrentPage == 1){
                if(DataUtil.idNotNull(result.getResults())){
                    mAdapter.setNewInstance(result.getResults());
                    isLoadMore = result.getHasMorePages();
                    if(!isLoadMore){
                        ll_endNoData.setVisibility(VISIBLE);
                    }
                }else{
                    mAdapter.setNewInstance(mCategorys);
                    mAdapter.setEmptyView(getEmptyView());
                }
            }else{
                mAdapter.addData(result.getResults());
                isLoadMore = result.getHasMorePages();
                if(!isLoadMore){
                    ll_endNoData.setVisibility(VISIBLE);
                }
            }
        }
    }

    @Override
    public void fetchFail(String failReason) {
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void fetchNetWorkState(boolean unknowNet) {
        if(!unknowNet){
            mAdapter.setEmptyView(getNoNetView());
        }
    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {
        mSwipeRefresh.setRefreshing(false);
    }


}
