package com.project.app.fragment;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.RecycerLoadMoreScrollView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.project.app.R;
import com.project.app.adapter.AddMoreAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.AddMoreListBean;
import com.project.app.contract.AddMoreGoodsContract;
import com.project.app.presenter.AddMoreGoodsPresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

public class AddMoreGoodsFragment extends BaseMvpQmuiFragment<AddMoreGoodsPresenter> implements AddMoreGoodsContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.v_inflater)
    LinearLayout v_inflater;
    @BindView(R.id.srl_addMore)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.sv_moreGoodsWrapper)
    RecycerLoadMoreScrollView sv_moreGoodsWrapper;
    @BindView(R.id.rlv_goodsContainer)
    RecyclerView rlv_goodsContainer;
    @BindView(R.id.ll_endNoData)
    LinearLayout ll_endNoData;
    @BindView(R.id.cl_noData)
    ConstraintLayout cl_noData;
    @BindView(R.id.tv_topTitle)
    TextView tv_topTitle;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    private boolean isPrepared = false;
    private boolean isHasMore  = false;
    private int mCurrentPage = 5;
    private final int mPageSize    = 20;
    private final String mCategoryNo = "P05";
    private AddMoreAdapter mAdapter;
    private final List<AddMoreListBean.ClassifyItem> mMoreLists = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_add_more_goods;
    }

    @Override
    public void initView() {
        mPresenter = new AddMoreGoodsPresenter();
        mPresenter.attachView(this);
        initTopbar();
        initWidget();
    }

    private void initWidget() {
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.allwees_theme_color,android.R.color.holo_blue_dark,android.R.color.holo_orange_dark);
        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        rlv_goodsContainer.setLayoutManager(manager);
        mAdapter = new AddMoreAdapter(mMoreLists);
        mAdapter.setAnimationEnable(true);
        mAdapter.setAnimationFirstOnly(false);
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        rlv_goodsContainer.setAdapter(mAdapter);

        sv_moreGoodsWrapper.setScrollViewListener(new RecycerLoadMoreScrollView.ScrollViewListener() {
            @Override
            public void onLoadMore() {
                if (isHasMore){
                    mCurrentPage++;
                    mPresenter.fetchGoodsList(mCategoryNo,mCurrentPage,mPageSize);
                }
            }
            @Override
            public void onScrollChanged(RecycerLoadMoreScrollView scrollView, int x, int y, int oldx, int oldy) {

            }
        });
    }

    @OnClick({R.id.iv_back})
    public void onClickViewed(View view){
        if (view.getId() == R.id.iv_back) {
            popBackStack();
        }
    }

    private void initTopbar() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
    }

    @Override
    protected void lazyFetchData() {
        if(!isPrepared){
            mSwipeRefresh.setRefreshing(true);
            mPresenter.fetchGoodsList(mCategoryNo,mCurrentPage,mPageSize);
        }
    }

    @Override
    public void fetchSuccess(AddMoreListBean result) {
        if(result != null){
            isPrepared = true;
            isHasMore  = result.isHasMorePages();
            if(DataUtil.idNotNull(result.getResults())){
                if(mCurrentPage == 5){
                    cl_noData.setVisibility(View.GONE);
                    rlv_goodsContainer.setVisibility(View.VISIBLE);
                    mAdapter.setNewInstance(result.getResults());
                }else{
                    mAdapter.addData(result.getResults());
                }
                if(!result.isHasMorePages()){
                    ll_endNoData.setVisibility(View.VISIBLE);
                }else{
                    ll_endNoData.setVisibility(View.GONE);
                }
            }else{
                cl_noData.setVisibility(View.VISIBLE);
                rlv_goodsContainer.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void fetchFail(String failReason) {
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
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        isPrepared   = false;
        mCurrentPage = 5;
        mSwipeRefresh.setRefreshing(true);
        mPresenter.fetchGoodsList(mCategoryNo,mCurrentPage,mPageSize);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
