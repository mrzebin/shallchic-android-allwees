package com.project.app.fragment.home.classify;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.other.UserUtil;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.RecycerLoadMoreScrollView;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.project.app.R;
import com.project.app.adapter.CategoryClassifyAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.ClassifyListBean;
import com.project.app.contract.HomeClassifyContract;
import com.project.app.presenter.HomeClassifyPresenter;
import com.project.app.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 分类的详情
 */
public class CategoryClassifyFragment extends BaseMvpQmuiFragment<HomeClassifyPresenter> implements HomeClassifyContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.srf_classify)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.rv_classify)
    RecyclerView rv_classify;
    @BindView(R.id.rlv_goods)
    RecycerLoadMoreScrollView rlv_goods;
    @BindView(R.id.ll_endNoData)
    LinearLayout ll_endNoData;
    @BindView(R.id.ll_inflateTp)
    LinearLayout ll_inflateTp;
    @BindView(R.id.tv_topTitle)
    TextView tv_topTitle;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    private HomeClassifyPresenter mPresenter;
    private CategoryClassifyAdapter mAdapter;
    private final List<ClassifyListBean.ClassifyItem> mCategorys = new ArrayList<>();
    private String mNo;
    private String mCategoryName;
    private int mPageSize = 20;
    private int mCurrentPage = 1;
    private boolean isLoadMoreEnable = false;
    private static final int REFRESH_LOADING_TIMEOUT = 3;
    
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == REFRESH_LOADING_TIMEOUT) {
                if (mSwipeRefresh != null) {
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        }
    };

    public static CategoryClassifyFragment newInstance(String no) {
        Bundle bundle = new Bundle();
        bundle.putString("type",no);
        CategoryClassifyFragment fragment = new CategoryClassifyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_classify_category;
    }

    @Override
    public void initView() {
        initTapbar();
        mPresenter = new HomeClassifyPresenter();
        mPresenter.attachView(this);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.allwees_theme_color,android.R.color.holo_blue_dark,android.R.color.holo_orange_dark);
        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        rv_classify.setLayoutManager(manager);
        mAdapter = new CategoryClassifyAdapter(mCategorys);
        mAdapter.setAnimationEnable(true);
        mAdapter.setAnimationFirstOnly(false);
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        rv_classify.setAdapter(mAdapter);

        rlv_goods.setScrollViewListener(new RecycerLoadMoreScrollView.ScrollViewListener() {
            @Override
            public void onLoadMore() {
                if (isLoadMoreEnable) {
                    mCurrentPage++;
                    mHandler.sendEmptyMessageDelayed(REFRESH_LOADING_TIMEOUT, Constant.DELAY_LOADING_TIME_OUT);
                    mPresenter.fetchGoodsInfo(mCurrentPage,mPageSize,mNo);
                }
            }
            @Override
            public void onScrollChanged(RecycerLoadMoreScrollView scrollView, int x, int y, int oldx, int oldy) {

            }
        });
    }

    private void initTapbar() {
        mNo = getArguments().getString("type");
        mCategoryName = getArguments().getString("name");
        tv_topTitle.setText(mCategoryName);
        StatusBarUtils.setStatusBarView(getActivity(),ll_inflateTp);
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
    }

    @Override
    protected void lazyFetchData() {
        mHandler.sendEmptyMessageDelayed(REFRESH_LOADING_TIMEOUT, Constant.DELAY_LOADING_TIME_OUT);
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
    public void fetchNetWorkState(boolean unknowNet) {
        if(!unknowNet){
            mAdapter.setEmptyView(getNoNetView());
        }
    }

    @OnClick({R.id.iv_back})
    public void onClickViewed(View view){
        if (view.getId() == R.id.iv_back) {
            popBackStack();
        }
    }

    private View getNoNetView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_no_net,null);
        QMUIRoundButton qrb_refreshData = view.findViewById(R.id.qrb_refreshNet);
        qrb_refreshData.setOnClickListener(view1 -> {
            if(UserUtil.getInstance().isLogin()){
                mPresenter.fetchGoodsInfo(mCurrentPage,mPageSize,mNo);
            }
        });
        return view;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
