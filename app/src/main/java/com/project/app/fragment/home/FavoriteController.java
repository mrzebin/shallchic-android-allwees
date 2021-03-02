package com.project.app.fragment.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.other.UserUtil;
import com.hb.basemodel.utils.DataUtil;
import com.project.app.R;
import com.project.app.adapter.WishlistAdapter;
import com.project.app.base.BaseMvpController;
import com.project.app.bean.WishDataBean;
import com.project.app.contract.WishlistContract;
import com.project.app.presenter.WishListPresenter;
import com.project.app.ui.widget.GlobalClassicsFooter;
import com.project.app.ui.widget.GlobalClassicsHeader;
import com.project.app.utils.StatusBarUtils;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 收藏
 */
public class FavoriteController extends BaseMvpController<WishListPresenter> implements WishlistContract.View {
    @BindView(R.id.qmTopBar)
    QMUITopBarLayout qmTopBar;
    @BindView(R.id.fl_inflate)
    FrameLayout fl_inflate;
    @BindView(R.id.srl_favorite)
    SmartRefreshLayout srl_favorite;
    @BindView(R.id.rv_favorite)
    RecyclerView rlv_favorite;

    private final int mPageSize    = 20;
    private int mCurrentPage = 1;
    private boolean isPrepared = false;
    private boolean isLoadMore = false;
    private WishlistAdapter mAdapter;
    private View mNoDateView;
    private final List<WishDataBean.ProductDtoWrapp> mCategorys = new ArrayList<>();

    public FavoriteController(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_layout,this);
        ButterKnife.bind(this,view);
        initTopBar();
        initView();
        onRefreshView();
    }

    public void onRefreshView() {
        if(UserUtil.getInstance().isLogin()){
            if(!isPrepared){
                mPresenter.fetchWishList(mCurrentPage,mPageSize);
            }
        }else{
            rlv_favorite.setAdapter(mAdapter);
            mAdapter.setEmptyView(getEmptyView());
        }
    }

    public void syncFavorite(){
        if(UserUtil.getInstance().isLogin()){
            mPresenter.fetchWishList(mCurrentPage,mPageSize);
        }
    }

    private void initView() {
        mPresenter = new WishListPresenter();
        mPresenter.attachView(this);
        srl_favorite.setRefreshHeader(new GlobalClassicsHeader(getContext()));
        srl_favorite.setRefreshFooter(new GlobalClassicsFooter(getContext()));
        srl_favorite.setHeaderHeight(60);
        srl_favorite.setFooterHeight(50);
        srl_favorite.setEnableLoadMore(false); //默认不让起有更多数据

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        rlv_favorite.setLayoutManager(manager);
        mAdapter = new WishlistAdapter(mCategorys);
        mAdapter.setAnimationEnable(false);
        rlv_favorite.setAdapter(mAdapter);

        srl_favorite.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mCurrentPage = 1;
                isLoadMore = false;
                srl_favorite.finishRefresh(Constant.DEFAULT_SMARTLOADING_DELAY_TIMEOUT);
                if (UserUtil.getInstance().isLogin()) {
                    mPresenter.fetchWishList(mCurrentPage, mPageSize);
                }
            }
        });

        srl_favorite.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (isLoadMore) {
                    mCurrentPage++;
                    if (UserUtil.getInstance().isLogin()) {
                        mPresenter.fetchWishList(mCurrentPage, mPageSize);
                        srl_favorite.finishLoadMore(Constant.DEFAULT_SMARTLOADING_DELAY_TIMEOUT);
                    }
                }else{
                    srl_favorite.finishLoadMore();
                }
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

    private View getNoEndView(){
        mNoDateView = LayoutInflater.from(getContext()).inflate(R.layout.layout_footer_no_more,null);
        return mNoDateView;
    }

    private View getNoNetView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_no_net,null);
        QMUIRoundButton qrb_refreshData = view.findViewById(R.id.qrb_refreshNet);
        qrb_refreshData.setOnClickListener(view1 -> {
            if(UserUtil.getInstance().isLogin()){
                showProgressDialog();
                mPresenter.fetchWishList(mCurrentPage,mPageSize);
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
            isLoadMore = result.getHasMorePages();
            if(mCurrentPage == 1){
                if(DataUtil.idNotNull(result.getResults())){
                    mAdapter.setNewInstance(result.getResults());
                    if(result.getResults().size() <20){    //这里接口有点问题
                        isLoadMore = false;
                    }
                }else{
                    mAdapter.setNewInstance(new ArrayList<WishDataBean.ProductDtoWrapp>());    //注意这里要刷新数据
                    mAdapter.setEmptyView(getEmptyView());
                    srl_favorite.setEnableLoadMore(false);
                }
            }else{
                mAdapter.addData(result.getResults());
            }
            if(!isLoadMore){
                if(!mAdapter.hasFooterLayout()){
                    mAdapter.addFooterView(getNoEndView());
                }
            }else{
                srl_favorite.setEnableLoadMore(true);
            }
        }
    }

    @Override
    public void fetchFail(String failReason) {

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

    }


}
