package com.project.app.fragment.home.classify;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.image.ImageLoader;
import com.hb.basemodel.other.UserUtil;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.SPManager;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.project.app.R;
import com.project.app.activity.LoginActivity;
import com.project.app.adapter.DeliverDoubleAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.ActionFreeOneBean;
import com.project.app.contract.ActionDeliverDoubleContract;
import com.project.app.presenter.ActionDeliverDoublePresenter;
import com.project.app.ui.widget.GlobalClassicsFooter;
import com.project.app.ui.widget.GlobalClassicsHeader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 *  买一送一/
 */
public class DeliverDoubleFragment extends BaseMvpQmuiFragment<ActionDeliverDoublePresenter> implements ActionDeliverDoubleContract.View{
    @BindView(R.id.srl_deliverDouble)
    SmartRefreshLayout srl_deliver;
    @BindView(R.id.rv_actionGetDoubleGoods)
    RecyclerView rv_goodItems;
    @BindView(R.id.iv_activeDeliverTow)
    QMUIRadiusImageView iv_head;
    @BindView(R.id.tv_buyNum)
    TextView tv_buyNum;
    @BindView(R.id.cl_comeMyCart)
    ConstraintLayout cl_comeMyCart;
    @BindView(R.id.v_hasCart)
    View v_hasCart;
    @BindView(R.id.emptyView)
    QMUIEmptyView emptyView;
    @BindView(R.id.tv_topTitle)
    TextView tv_topTitle;

    private View mNoDateView;
    private int mCurrentPage = 1;
    private int mPageSize = 20;
    private String mMarketTitle;
    private String mMarketType = "16";   //默认是16
    private boolean isLoadMore = false;
    private List<ActionFreeOneBean.ClassifyItem> mData = new ArrayList<>();
    private DeliverDoubleAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_deliver_double;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        initBundle();
        initWidget();
        initSmartRefresh();
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if(bundle != null){
            mMarketTitle  = bundle.getString("marketTitle");
            mMarketType   = bundle.getString("marketId");
        }
    }

    private void initSmartRefresh() {
        srl_deliver.setRefreshHeader(new GlobalClassicsHeader(getContext()));
        srl_deliver.setRefreshFooter(new GlobalClassicsFooter(getContext()));
        srl_deliver.setHeaderHeight(60);
        srl_deliver.setFooterHeight(50);
        srl_deliver.setEnableLoadMore(false); //默认不让起有更多数据

        srl_deliver.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mCurrentPage = 1;
                isLoadMore = false;
                srl_deliver.finishRefresh(Constant.DEFAULT_SMARTLOADING_DELAY_TIMEOUT);
                mPresenter.fetchActionFreeOne(mMarketType,mCurrentPage,mPageSize);
            }
        });

        srl_deliver.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (isLoadMore) {
                    mCurrentPage++;
                    srl_deliver.finishLoadMore(Constant.DEFAULT_SMARTLOADING_DELAY_TIMEOUT);
                    mPresenter.fetchActionFreeOne(mMarketType,mCurrentPage,mPageSize);
                }else{
                    srl_deliver.finishLoadMore();
                }
            }
        });
    }

    private void initWidget() {
        mPresenter = new ActionDeliverDoublePresenter();
        mPresenter.attachView(this);
        mAdapter = new DeliverDoubleAdapter(mData);
        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        rv_goodItems.setLayoutManager(manager);
        rv_goodItems.setAdapter(mAdapter);

        if(!TextUtils.isEmpty(mMarketTitle)){
            tv_topTitle.setText(mMarketTitle);
        }
        changeCartNum();
    }

    public View getEmptyView(){
        View emptyData = View.inflate(getContext(), R.layout.layout_empty_nogoods,null);
        LinearLayout ll_wrapNoGoods = emptyData.findViewById(R.id.ll_wrapNoGoods);
        QMUIRoundButton qrb_refreshAgain = emptyData.findViewById(R.id.qrb_refreshAgain);
        ll_wrapNoGoods.setVisibility(View.VISIBLE);
        qrb_refreshAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.fetchActionFreeOne(mMarketType,mCurrentPage,mPageSize);
            }
        });
        return emptyData;
    }

    public View getEmptyHttpExceptView(){
        View exceptView = View.inflate(getContext(),R.layout.layout_no_net,null);
        return exceptView;
    }

    private void changeCartNum() {
        int cartSkuNum = SPManager.sGetInt(Constant.SP_SAVE_CART_GOODS_NUM);
        String buyNum = "";
        if(cartSkuNum > 0){
            tv_buyNum.setVisibility(View.VISIBLE);
            v_hasCart.setVisibility(View.VISIBLE);
            if (cartSkuNum >99){
                buyNum = cartSkuNum + "+";
            }else{
                buyNum = String.valueOf(cartSkuNum);
            }
            tv_buyNum.setText(String.valueOf(buyNum));
        }else{
            tv_buyNum.setVisibility(View.GONE);
            v_hasCart.setVisibility(View.GONE);
        }
    }

    private boolean checkLogin(){
        UserUtil userUtil = UserUtil.getInstance();
        if(!userUtil.isLogin()) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            getContext().startActivity(intent);
        }
        return userUtil.isLogin();
    }

    private View getNoEndView(){
        mNoDateView = LayoutInflater.from(getContext()).inflate(R.layout.layout_footer_no_more,null);
        return mNoDateView;
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchActionFreeOne(mMarketType,mCurrentPage,mPageSize);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshDataEvent event){
        if (event.getmMsg().equals(Constant.EVNET_CHANGE_CURRENT_CART_NUM)){
            changeCartNum();
        }
    }

    @OnClick({R.id.iv_back,R.id.iv_shipCart_fs})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.iv_back:
                popBackStack();
                break;
            case R.id.iv_shipCart_fs:
                if(checkLogin()){
                    clearHolderStack();
                    EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_BANNER_SKIP_CART));
                }
                break;
        }
    }

    @Override
    public void fetchActionFreeOneSuccess(ActionFreeOneBean result) {
        String coverHeadUrl = result.getBuyAndFreeUrl();
        if(!TextUtils.isEmpty(coverHeadUrl)){
            iv_head.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(iv_head,coverHeadUrl,R.mipmap.allwees_ic_default_goods);
        }else{
            iv_head.setVisibility(View.GONE);
        }
        isLoadMore = result.getResults().isHasMorePages();
        srl_deliver.setEnableLoadMore(isLoadMore);
        srl_deliver.finishLoadMore();
        if(mCurrentPage == 1){
            if(DataUtil.idNotNull(result.getResults().getResults())){
                mAdapter.setNewInstance(result.getResults().getResults());
                if(!isLoadMore && !mAdapter.hasFooterLayout()){
                    mAdapter.addFooterView(getNoEndView());
                }
            }else{
                mAdapter.setEmptyView(getEmptyView());
            }
        }else{
            if(DataUtil.idNotNull(result.getResults().getResults())){
                mAdapter.addData(result.getResults().getResults());
                if(!isLoadMore && !mAdapter.hasFooterLayout()){
                    mAdapter.addFooterView(getNoEndView());
                }
            }
        }
    }

    @Override
    public void fetchActionFreeOneFail(String msg) {
        ToastUtil.showToast(msg);
    }

    @Override
    public void loadMatchViewStatus(int loadStatus) {
        emptyView.show(false);
        emptyView.setVisibility(View.GONE);
        srl_deliver.setVisibility(View.VISIBLE);
        switch (loadStatus){
            case 1:       //网络异常
                mAdapter.setEmptyView(getEmptyView());
                break;
            case 2:       //无数据
                mAdapter.setEmptyView(getEmptyHttpExceptView());
                break;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(mPresenter != null){
            mPresenter.onDestoryView();
            mPresenter = null;
            System.gc();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
