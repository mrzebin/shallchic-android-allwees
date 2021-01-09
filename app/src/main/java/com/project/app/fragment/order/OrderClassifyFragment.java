package com.project.app.fragment.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.RecycerLoadMoreScrollView;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.project.app.R;
import com.project.app.adapter.OrderKindsAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.OrderBean;
import com.project.app.contract.OrderContract;
import com.project.app.presenter.OrderPresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

public class OrderClassifyFragment extends BaseMvpQmuiFragment<OrderPresenter> implements OrderContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.rlv_order)
    RecyclerView rlv_order;
    @BindView(R.id.rlsv_load)
    RecycerLoadMoreScrollView rlsv_load;
    @BindView(R.id.ll_endNoData)
    LinearLayout ll_endNoData;

    private final List<OrderBean.OrderResult> mData = new ArrayList<>();
    private OrderKindsAdapter mAdapter;
    private boolean isHasMore = false;
    private int mCurrentPage = 1;
    private final int mPageSize = 20;
    private String mType;

    public static OrderClassifyFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        OrderClassifyFragment fragment = new OrderClassifyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_classify_order;
    }

    @Override
    public void initView() {
        initWidget();
    }

    private void initWidget() {
        Bundle bundle = getArguments();
        if(bundle != null){
            mType = bundle.getString("type");
        }
        mPresenter = new OrderPresenter();
        mPresenter.attachView(this);
        rlv_order.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.allwees_theme_color,android.R.color.holo_blue_dark,android.R.color.holo_orange_dark);
        mAdapter = new OrderKindsAdapter(mData);
        rlv_order.setLayoutManager(new LinearLayoutManager(getContext()));
        rlv_order.setAdapter(mAdapter);

        rlsv_load.setScrollViewListener(new RecycerLoadMoreScrollView.ScrollViewListener() {
            @Override
            public void onLoadMore() {
                if(isHasMore){
                    mCurrentPage++;
                    onLoadData(false);
                }
            }
            @Override
            public void onScrollChanged(RecycerLoadMoreScrollView scrollView, int x, int y, int oldx, int oldy) {

            }
        });
    }

    private void inflateDataToViwe(OrderBean result) {
        if(result != null){
            isHasMore = result.getHasMorePages();
            if(mCurrentPage == 1){
                if(DataUtil.idNotNull(result.getResults())){
                    mAdapter.setNewInstance(result.getResults());
                    if(!isHasMore){
                        ll_endNoData.setVisibility(View.VISIBLE);
                    }else{
                        ll_endNoData.setVisibility(View.GONE);
                    }
                }else{
                    mAdapter.setEmptyView(getEmptyView());
                    ll_endNoData.setVisibility(View.GONE);
                }
            }else{
                if(DataUtil.idNotNull(result.getResults())){
                    mAdapter.addData(result.getResults());
                    if(!isHasMore){
                        ll_endNoData.setVisibility(View.VISIBLE);
                    }else{
                        ll_endNoData.setVisibility(View.GONE);
                    }
                }
            }
        }else{
            mAdapter.setEmptyView(getEmptyView());
            ll_endNoData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        onLoadData(true);
    }

    private void onLoadData(boolean isShow) {
        mPresenter.fetchOrderList(mCurrentPage,mPageSize,mType);
        if(isShow){
            mSwipeRefresh.setRefreshing(true);
            mSwipeRefresh.postDelayed(() -> {
                if(mSwipeRefresh != null){
                    mSwipeRefresh.setRefreshing(false);
                }
            }, Constant.DELAY_LOADING_TIME_OUT);
        }
    }

    private View getEmptyView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_order,null);
        QMUIRoundButton qrb_refresh = view.findViewById(R.id.qrb_refreshOrder);
        qrb_refresh.setOnClickListener(view1 -> onLoadData(true));
        return view;
    }

    @Override
    protected void lazyFetchData() {
        onLoadData(true);
    }

    @Override
    public void fetchOrderListSuccess(OrderBean result) {
        inflateDataToViwe(result);
    }

    @Override
    public void fetchOrderListFail(String reason) {
        ToastUtil.showToast(reason);
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
