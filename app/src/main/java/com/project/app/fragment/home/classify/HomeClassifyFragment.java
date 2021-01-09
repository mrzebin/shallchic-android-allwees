package com.project.app.fragment.home.classify;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.RecycerLoadMoreScrollView;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.project.app.R;
import com.project.app.adapter.HomeClassifyAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.ClassifyListBean;
import com.project.app.contract.HomeClassifyContract;
import com.project.app.fragment.home.HomeController;
import com.project.app.presenter.HomeClassifyPresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

public class HomeClassifyFragment extends BaseMvpQmuiFragment<HomeClassifyPresenter> implements HomeClassifyContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.srf_classify)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.rv_classify)
    RecyclerView rv_classify;
    @BindView(R.id.rlv_goods)
    RecycerLoadMoreScrollView rlv_goods;
    @BindView(R.id.ll_endNoData)
    LinearLayout ll_endNoData;

    private String mNo;
    private int mPageSize    = 20;
    private int mCurrentPage = 1;
    private boolean isLoadMoreEnable = false;
    private HomeClassifyAdapter mAdapter;
    private HomeClassifyPresenter mPresenter;
    private static HomeController.HostMasterListener mListener;
    private final List<ClassifyListBean.ClassifyItem> mCategorys = new ArrayList<>();

    public static HomeClassifyFragment newInstance(String no, HomeController.HostMasterListener listener) {
        Bundle bundle = new Bundle();
        bundle.putString("type",no);
        HomeClassifyFragment fragment = new HomeClassifyFragment();
        fragment.setArguments(bundle);
        mListener = listener;
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_classify_home;
    }

    @Override
    public void initView() {
        mNo = getArguments().getString("type");
        mPresenter = new HomeClassifyPresenter();
        mPresenter.attachView(this);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.allwees_theme_color,android.R.color.holo_blue_dark,android.R.color.holo_orange_dark);
        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        rv_classify.setLayoutManager(manager);
        mAdapter = new HomeClassifyAdapter(mCategorys);
        mAdapter.setAnimationEnable(true);
        mAdapter.setAnimationFirstOnly(false);
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        rv_classify.setAdapter(mAdapter);

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
            mAdapter.setEmptyView(getNoNetView());
        }
    }

    private View getNoNetView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_no_net,null);
        QMUIRoundButton qrb_refreshData = view.findViewById(R.id.qrb_refreshNet);
        qrb_refreshData.setOnClickListener(view1 -> mPresenter.fetchGoodsInfo(mCurrentPage,mPageSize,mNo));
        return view;
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchGoodsInfo(mCurrentPage,mPageSize,mNo);
        mSwipeRefresh.setRefreshing(true);
        mSwipeRefresh.postDelayed(() -> {
            if(mSwipeRefresh != null){
                mSwipeRefresh.setRefreshing(false);
            }
        }, Constant.DELAY_LOADING_TIME_OUT);
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        lazyFetchData();
    }

    @Override
    public void fetchCategoryList(ClassifyListBean result) {
        if(result != null){
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

}
