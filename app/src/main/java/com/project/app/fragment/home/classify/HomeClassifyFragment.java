package com.project.app.fragment.home.classify;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.project.app.R;
import com.project.app.adapter.HomeClassifyAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.ClassifyListBean;
import com.project.app.contract.HomeClassifyContract;
import com.project.app.fragment.home.HomeController;
import com.project.app.presenter.HomeClassifyPresenter;
import com.project.app.ui.widget.GlobalClassicsFooter;
import com.project.app.ui.widget.GlobalClassicsHeader;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

public class HomeClassifyFragment extends BaseMvpQmuiFragment<HomeClassifyPresenter> implements HomeClassifyContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.srl_home)
    SmartRefreshLayout srl_home;
    @BindView(R.id.rv_classify)
    RecyclerView rv_classify;
    @BindView(R.id.emptyView)
    QMUIEmptyView emptyView;
    @BindView(R.id.iv_floatUp)
    ImageView iv_floatUp;

    private String mNo;
    private View mNoDateView;
    private int mPageSize    = 20;
    private int mCurrentPage = 1;
    private boolean isLoadMore = false;
    private int mFloatLimitHeight = 800;    //判断显示的高度,默认800
    private HomeClassifyAdapter mAdapter;
    private HomeClassifyPresenter mPresenter;
    private static HomeController.HostMasterListener mListener;
    private GridLayoutManager manager;
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

        srl_home.setRefreshHeader(new GlobalClassicsHeader(getContext()));
        srl_home.setRefreshFooter(new GlobalClassicsFooter(getContext()));
        srl_home.setHeaderHeight(60);
        srl_home.setFooterHeight(50);
        srl_home.setEnableLoadMore(false); //默认不让起有更多数据

        manager = new GridLayoutManager(getContext(),2);
        rv_classify.setLayoutManager(manager);
        mAdapter = new HomeClassifyAdapter(mCategorys);
        mAdapter.setAnimationEnable(true);
        mAdapter.setAnimationFirstOnly(false);
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        rv_classify.setAdapter(mAdapter);

        srl_home.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mCurrentPage = 1;
                isLoadMore = false;
                srl_home.finishRefresh(Constant.DEFAULT_SMARTLOADING_DELAY_TIMEOUT);
                mPresenter.fetchGoodsInfo(mCurrentPage,mPageSize,mNo);
                if(mAdapter.hasFooterLayout()){
                    mAdapter.removeAllFooterView();
                }
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

        rv_classify.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(castDistanceVertical() > mFloatLimitHeight){
                    iv_floatUp.setVisibility(View.VISIBLE);
                }else{
                    iv_floatUp.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 计算滑动的距离
     * @return
     */
    private int castDistanceVertical(){
         int iResult = 0;
        //找到即将移出屏幕Item的position,position是移出屏幕item的数量
        int position = manager.findFirstVisibleItemPosition();
        //根据position找到这个Item
        View firstVisiableChildView = manager.findViewByPosition(position);
        //获取Item的高
        int itemHeight = firstVisiableChildView.getHeight();
        //算出该Item还未移出屏幕的高度
        int itemTop = firstVisiableChildView.getTop();
        //position移出屏幕的数量*高度得出移动的距离
        int iposition = position * itemHeight;
        //减去该Item还未移出屏幕的部分可得出滑动的距离
        iResult = iposition - itemTop;
        return iResult;
    }

    private View getNoEndView(){
        mNoDateView = LayoutInflater.from(getContext()).inflate(R.layout.layout_footer_no_more,null);
        return mNoDateView;
    }

    public View getEmptyView(){
        View view = View.inflate(getContext(),R.layout.layout_empty_nogoods,null);
        LinearLayout ll_noGoods = view.findViewById(R.id.ll_wrapNoGoods);
        ll_noGoods.setVisibility(View.VISIBLE);
        QMUIRoundButton refreshAgain = view.findViewById(R.id.qrb_refreshAgain);
        refreshAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProgressDialog(getContext());
                mPresenter.fetchGoodsInfo(mCurrentPage,mPageSize,mNo);
            }
        });
        return view;
    }

    @OnClick({R.id.iv_floatUp})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.iv_floatUp:
                srl_home.finishLoadMore();
                rv_classify.smoothScrollToPosition(0);
                break;
        }
    }

    @Override
    public void fetchNetWorkState(boolean unknowNet) {
        if(!unknowNet){
            checkEmptyView();
            mAdapter.setEmptyView(getNoNetView());
        }
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

    private void checkEmptyView(){
        if(emptyView.getVisibility() == View.VISIBLE){
            emptyView.setVisibility(View.GONE);
            srl_home.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchGoodsInfo(mCurrentPage,mPageSize,mNo);
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        lazyFetchData();
    }

    @Override
    public void fetchCategoryList(ClassifyListBean result) {
        if(result != null){
            isLoadMore = result.isHasMorePages();
            srl_home.setEnableLoadMore(isLoadMore);
            if(mCurrentPage == 1){
                if(DataUtil.idNotNull(result.getResults())){
                   mAdapter.setNewInstance(result.getResults());
                   if(!isLoadMore && !mAdapter.hasFooterLayout()){
                       mAdapter.addFooterView(getNoEndView());
                   }
                }else{
                    mAdapter.setEmptyView(getEmptyView());
                }
            }else{
                if(DataUtil.idNotNull(result.getResults())){
                    mAdapter.addData(result.getResults());
                }
                if(!isLoadMore && !mAdapter.hasFooterLayout()){
                    mAdapter.addFooterView(getNoEndView());
                }
            }
        }
        checkEmptyView();
    }

    @Override
    public void fetchFail(String failReason) {
        ToastUtil.showToast(failReason);
        checkEmptyView();
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
        if(mPresenter != null){
            mPresenter.onDestoryView();
            mPresenter = null;
            System.gc();
        }
    }
}
