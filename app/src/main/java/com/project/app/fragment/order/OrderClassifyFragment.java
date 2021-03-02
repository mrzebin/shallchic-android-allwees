package com.project.app.fragment.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.RecycerLoadMoreScrollView;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.adapter.OrderKindsAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.OrderBean;
import com.project.app.contract.OrderContract;
import com.project.app.presenter.OrderPresenter;
import com.project.app.ui.dialog.OrderReceiverRemindUtil;

import org.greenrobot.eventbus.EventBus;

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

    private List<OrderBean.OrderResult> mData = new ArrayList<>();
    private OrderKindsAdapter mAdapter;
    private boolean isHasMore = false;
    private int mCurrentPage = 1;
    private final int mPageSize = 20;
    private String mType;
    private OrderReceiverRemindUtil mReceiveRemindUtil;

    public static com.project.app.fragment.order.OrderClassifyFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        com.project.app.fragment.order.OrderClassifyFragment fragment = new com.project.app.fragment.order.OrderClassifyFragment();
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
        mReceiveRemindUtil = new OrderReceiverRemindUtil(getContext(),true,true);
        mReceiveRemindUtil.setMListener(new OrderReceiverRemindUtil.IReceiveListener() {
            @Override
            public void sureReceive(String uuid) {
                startProgressDialog(getContext());
                mPresenter.receiveGoodsRequest(uuid);
            }
        });

        rlv_order.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.theme_color,android.R.color.holo_blue_dark,android.R.color.holo_orange_dark);
        mAdapter = new OrderKindsAdapter(mData);
        rlv_order.setLayoutManager(new LinearLayoutManager(getContext()));
        rlv_order.setAdapter(mAdapter);
        mAdapter.setListener(new OrderKindsAdapter.IOrderStateListener() {
            @Override
            public void doReview(String goodsPortraitUrl,String goodsName,String orderUuid,String uuid) {
                Bundle baba = new Bundle();
                baba.putString("orderUuid", orderUuid);
                baba.putString("orderItemUuid", uuid);
                baba.putString("orderGoodsUrl",goodsPortraitUrl);
                baba.putString("orderGoodsName",goodsName);
                HolderActivity.startFragment(getContext(), com.project.app.fragment.order.OrderReviewDetailFragment.class,baba);
            }

            @Override
            public void doRefund(String orderUuid,String uuid) {
                Bundle baba = new Bundle();
                baba.putString("orderUuid", orderUuid);
                baba.putString("orderItemUuid", uuid);
                HolderActivity.startFragment(getContext(),OrderRefundDetailFragment.class,baba);
            }

            @Override
            public void doReceive(String uuid) {
                mReceiveRemindUtil.show(uuid);
            }

            @Override
            public void doCancel(String orderUuid,String uuid) {
                Bundle baba = new Bundle();
                baba.putString("orderUuid", orderUuid);
                baba.putString("orderItemUuid", uuid);
                HolderActivity.startFragment(getContext(),OrderCancelByReasonragment.class,baba);
            }

            @Override
            public void doRepurchase(String orderUuid) {   //加购
                mPresenter.requestRepurchase(orderUuid);
            }
        });

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

    private View getFooterView(){
        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.layout_footer_no_more,null);
        return footerView;
    }

    private void inflateDataToViwe(OrderBean result) {
        if(result != null){
            isHasMore = result.getHasMorePages();
            if(mCurrentPage == 1){
                if(DataUtil.idNotNull(result.getResults())){
                    mAdapter.setNewInstance(result.getResults());
                    if(!isHasMore && !mAdapter.hasFooterLayout()){
                        mAdapter.addFooterView(getFooterView());
                    }
                }else{
                    mAdapter.setNewInstance(new ArrayList<OrderBean.OrderResult>());
                    mAdapter.setEmptyView(getEmptyView());
                }
            }else{
                if(DataUtil.idNotNull(result.getResults())){
                    mAdapter.addData(result.getResults());
                    if(!isHasMore && !mAdapter.hasFooterLayout()){
                        mAdapter.addFooterView(getFooterView());
                    }
                }
            }
        }else{
            mAdapter.setEmptyView(getEmptyView());
        }
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        onLoadData(true);
    }

    public void onLoadData(boolean isShow) {
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
        qrb_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProgressDialog(getContext());
                onLoadData(false);
            }
        });
        return view;
    }

    @Override
    public void lazyFetchData() {
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

    /**
     * 接收商品成功
     * @param msg
     */
    @Override
    public void RequestReceiveSuccess(String msg) {

    }

    /**
     * 接收商品失败
     * @param msg
     */
    @Override
    public void requestReceiveFail(String msg) {

    }

    @Override
    public void requestRepurchaseSuccess(String msg) {
        ToastUtil.showToast(msg);
        EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_REFRESH_CAR));   //刷新购物车
    }

    @Override
    public void requestRepurchaseFail(String msg) {
        ToastUtil.showToast(msg);
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
}
