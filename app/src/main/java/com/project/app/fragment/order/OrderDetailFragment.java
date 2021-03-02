package com.project.app.fragment.order;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.adapter.OrderDetailCastAdapter;
import com.project.app.adapter.OrderDetailKindsAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.LogisticTrackBean;
import com.project.app.bean.OrderDetailBean;
import com.project.app.bean.OrderDetailCastItem;
import com.project.app.contract.OrderDetailContract;
import com.project.app.presenter.OrderDetailPresenter;
import com.project.app.ui.dialog.LogisticDetailUtil;
import com.project.app.utils.MathUtil;
import com.project.app.utils.StatusBarUtils;
import com.project.app.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

public class OrderDetailFragment extends BaseMvpQmuiFragment<OrderDetailPresenter> implements OrderDetailContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.srf_layout)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.ll_inflate)
    LinearLayout ll_inflate;
    @BindView(R.id.iv_tb_back)
    ImageView iv_tb_back;
    @BindView(R.id.tv_tb_title)
    TextView tv_tb_title;
    @BindView(R.id.tv_orderPayFun)
    TextView tv_orderPayFun;
    @BindView(R.id.tv_orderPayDate)
    TextView tv_orderPayDate;
    @BindView(R.id.tv_orderDate)
    TextView tv_orderDate;
    @BindView(R.id.tv_orderNo)
    TextView tv_orderNo;
    @BindView(R.id.tv_addressName)
    TextView tv_addressName;
    @BindView(R.id.tv_postCode)
    TextView tv_postCode;
    @BindView(R.id.tv_locationPath)
    TextView tv_locationPath;
    @BindView(R.id.tv_productMoney)
    TextView tv_productMoney;
    @BindView(R.id.tv_shippingMoney)
    TextView tv_shippingMoney;
    @BindView(R.id.tv_cashMoney)
    TextView tv_cashMoney;
    @BindView(R.id.tv_orderTotalMoney)
    TextView tv_orderTotalMoney;
    @BindView(R.id.rlv_detailItems)
    RecyclerView rlv_detailItems;
    @BindView(R.id.tv_payStatus)
    TextView tv_payStatus;
    @BindView(R.id.tv_odCity)
    TextView tv_odCity;
    @BindView(R.id.tv_promoCode)
    TextView tv_promoCode;
    @BindView(R.id.rl_cash)
    RelativeLayout rl_cash;
    @BindView(R.id.tv_couponPrice)
    TextView tv_couponPrice;
    @BindView(R.id.rl_pcl)
    RelativeLayout rl_pcl;
    @BindView(R.id.v_pcl)
    View v_pcl;
    @BindView(R.id.v_cash)
    View v_cash;
    @BindView(R.id.tv_odPayDate)
    TextView tv_odPayDate;
    @BindView(R.id.tv_odPayMethod)
    TextView tv_odPayMethod;
    @BindView(R.id.rl_deduction)
    RelativeLayout rl_deduction;
    @BindView(R.id.tv_decution)
    TextView tv_decution;
    @BindView(R.id.v_decution)
    View v_decution;
    @BindView(R.id.tv_backCashManual)
    TextView tv_backCashManual;
    @BindView(R.id.ll_backCashManual)
    LinearLayout ll_backCashManual;
    @BindView(R.id.rlv_castOrderPrice)
    RecyclerView rlv_castOrderPrice;

    private String mNo;
    private String mTrackNo;
    private OrderDetailBean mOrderDetailData;
    private OrderDetailKindsAdapter mAdapter;
    private OrderDetailCastAdapter mCastAdapter;
    private LogisticDetailUtil mLogTrackUtil;
    private List<OrderDetailCastItem> mOrderDeatilTolls = new ArrayList<>();
    private final List<OrderDetailBean.Items> mData = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_detail;
    }

    @Override
    public void initView() {
        initTopbar();
        initWidget();
    }

    private void initTopbar() {
        Bundle bundle = getArguments();
        mNo = bundle.getString("orderId");
        EventBus.getDefault().register(this);
    }

    private void initWidget() {
        String title = getContext().getResources().getString(R.string.order_detail_title);
        QMUIStatusBarHelper.setStatusBarDarkMode(getActivity());
        StatusBarUtils.setStatusBarView(getContext(),ll_inflate);
        mPresenter = new OrderDetailPresenter();
        mPresenter.attachView(this);
        tv_tb_title.setText(title);
        mAdapter = new OrderDetailKindsAdapter(mData);
        mCastAdapter = new OrderDetailCastAdapter(mOrderDeatilTolls);
        rlv_detailItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rlv_detailItems.setAdapter(mAdapter);
        mLogTrackUtil = new LogisticDetailUtil(getContext(),true,true);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.theme_color,android.R.color.holo_blue_dark,android.R.color.holo_orange_dark);
        rlv_castOrderPrice.setLayoutManager(new LinearLayoutManager(getContext()));
        rlv_castOrderPrice.setAdapter(mCastAdapter);

        mAdapter.setListener((item, taskType) -> {
            switch (taskType) {
                case "review":
                    Bundle reviewBundle = new Bundle();
                    reviewBundle.putString("orderUuid", mNo);
                    reviewBundle.putString("orderItemUuid",  item.getUuid());
                    reviewBundle.putString("orderGoodsName", item.getProduct().getName());
                    reviewBundle.putString("orderGoodsUrl",  item.getProduct().getMainPhoto());
                    HolderActivity.startFragment(getContext(), com.project.app.fragment.order.OrderReviewDetailFragment.class,reviewBundle);
                    break;
                case "refund": {
                    Bundle refundBundle = new Bundle();
                    refundBundle.putString("orderUuid", mNo);
                    refundBundle.putString("orderItemUuid", item.getUuid());
                    HolderActivity.startFragment(getContext(), com.project.app.fragment.order.OrderRefundDetailFragment.class,refundBundle);
                    break;
                }
                case "lookLog":
                    Bundle bundle = new Bundle();
                    bundle.putString("trackNo",item.getUuid());
                    HolderActivity.startFragment(getContext(),OrderLogisticsInformationFragment.class);
                    break;
                case "receive":
                    mPresenter.receiveGoodsRequest(item.getUuid());
                    break;
                case "cancel": {
                    Bundle baba = new Bundle();
                    baba.putString("orderUuid", mNo);
                    baba.putString("orderItemUuid", item.getUuid());
                    HolderActivity.startFragment(getContext(), com.project.app.fragment.order.OrderCancelByReasonragment.class,baba);
                    break;
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEvent(RefreshDataEvent event){
        if(event.getmMsg().equals(Constant.EVENT_REFRESH_ORDER_STATE)){
            mPresenter.fetchDetailData(mNo);
        }
    }

    @OnClick({R.id.iv_tb_back})
    public void onClickViewed(View view){
        if (view.getId() == R.id.iv_tb_back) {
            popBackStack();
        }
    }

    private void inflateData(OrderDetailBean result) {
        if(result != null){
            OrderDetailBean.ShippingAddress address = result.getShippingAddress();
            String addressName;

            if(!TextUtils.isEmpty(address.getAddressLine1())){
                addressName = address.getAddressLine1();
            }else{
                addressName = address.getAddressLine2();
            }

            if(!TextUtils.isEmpty(address.getCity())){
                tv_locationPath.setText(address.getFirstName() + address.getLastName());
            }else{
                tv_locationPath.setText(address.getProvince());
            }
            if(!TextUtils.isEmpty(address.getPhone())){
                tv_postCode.setText(address.getPhone());
            }
            if(!TextUtils.isEmpty(result.getNo())){
                tv_orderNo.setText(result.getNo());
            }
            if(!TextUtils.isEmpty(result.getNo())){
                tv_orderNo.setText(result.getNo());
            }
            if(!TextUtils.isEmpty(result.getNo())){
                tv_orderNo.setText(result.getNo());
            }
            if(!TextUtils.isEmpty(result.getStateDesc())){
                tv_payStatus.setText(result.getStateDesc());
            }

            List<OrderDetailCastItem> orderTempTolls = new ArrayList<>();

            //item total
            orderTempTolls.add(new OrderDetailCastItem(getResources().getString(R.string.order_detail_it), MathUtil.Companion.formatPrice(result.getAmtProduct()),0));

            //shippingPrice 则为free
            if(result.getAmtShipping() > 0){
                orderTempTolls.add(new OrderDetailCastItem(getResources().getString(R.string.cart_shipping),MathUtil.Companion.formatPrice(result.getAmtShipping()),0));
            }else{
                orderTempTolls.add(new OrderDetailCastItem(getResources().getString(R.string.cart_shipping),getResources().getString(R.string.str_free),1));
            }

            //promoCode
            if(!TextUtils.isEmpty(result.getPromoCode()) && result.getAmtProductDiscount() >0){
                String promoPrice = result.getPromoCode() + " -" + MathUtil.Companion.formatPrice(result.getAmtProductDiscount());
                orderTempTolls.add(new OrderDetailCastItem(getResources().getString(R.string.order_detail_pcl),promoPrice,1));
            }

            if(result.getAmtDeduction() >0){
                orderTempTolls.add(new OrderDetailCastItem(getResources().getString(R.string.cart_item_deduction_price),"-" + MathUtil.Companion.formatPrice(result.getAmtDeduction()),1));
            }

            //cashPrice
            if(result.getAmtCash() > 0){
                orderTempTolls.add(new OrderDetailCastItem(getResources().getString(R.string.order_detail_cash),"-" + MathUtil.Companion.formatPrice(result.getAmtCash()),1));
            }

            //codCost
            if(result.getCodCost() > 0){
                orderTempTolls.add(new OrderDetailCastItem(getResources().getString(R.string.order_detail_codcost),MathUtil.Companion.formatPrice(result.getCodCost()),1));
            }

            //duty
            if(result.getDuty() >0){
                orderTempTolls.add(new OrderDetailCastItem(getResources().getString(R.string.order_detail_import_duty),MathUtil.Companion.formatPrice(result.getDuty()),1));
            }

            //commission
            if(result.getCommission() >0){
                orderTempTolls.add(new OrderDetailCastItem(getResources().getString(R.string.order_detail_clearance_commission),MathUtil.Companion.formatPrice(result.getCommission()),1));
            }

            orderTempTolls.add(new OrderDetailCastItem(getResources().getString(R.string.cart_ot),MathUtil.Companion.formatPrice(result.getAmt()),2,false));
            mCastAdapter.setNewInstance(orderTempTolls);

            StringBuilder sb_splid = new StringBuilder();

            if(!TextUtils.isEmpty(address.getStreet())){
                sb_splid.append(address.getStreet()+ " ");
            }
            if(!TextUtils.isEmpty(address.getCity())){
                sb_splid.append(address.getCity() + " ");
            }
            if(!TextUtils.isEmpty(address.getProvince())){
                sb_splid.append(address.getProvince() + " ");
            }
            if(!TextUtils.isEmpty(address.getCountry())){
                sb_splid.append(address.getCountry() + " ");
            }
            if(!TextUtils.isEmpty(address.getZipCode())){
                sb_splid.append(address.getZipCode() + " ");
            }
            if(addressName.contains("#")){
                sb_splid.append(StringUtils.filterZipCode(addressName) + " ");   //提取code码
                tv_addressName.setText(StringUtils.filterValidAddress(addressName));
            }else{
                tv_addressName.setText(addressName);
            }
            if(!TextUtils.isEmpty(address.getNote())){
                sb_splid.append(address.getNote());
            }
            if(DataUtil.idNotNull(result.getItems())){
                mAdapter.setNewInstance(result.getItems());
            }
            if(!TextUtils.isEmpty(result.getPaymentType())){
                tv_orderPayFun.setText(result.getPaymentType());
            }
            if(!TextUtils.isEmpty(result.getStateDesc())){
                tv_odPayMethod.setText(result.getStateDesc());
            }

            String createTime = StringUtils.getEnDateFormat(result.getCreatedAt());
            String paytTime   = StringUtils.getEnDateFormat(result.getPaymentExpiredAt());

            tv_odCity.setText(sb_splid.toString());
            tv_orderDate.setText(createTime);
            tv_orderPayDate.setText(paytTime);
        }
    }

    @Override
    public void onRefresh() {
        mSwipeRefresh.setRefreshing(false);
        mPresenter.fetchDetailData(mNo);
        mSwipeRefresh.postDelayed(() -> {
            if(mSwipeRefresh != null){
                mSwipeRefresh.setRefreshing(false);
            }
        }, Constant.DELAY_LOADING_TIME_OUT);
    }

    @Override
    protected void lazyFetchData() {
        if(mNo == null){
            return;
        }
        mPresenter.fetchDetailData(mNo);
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
    public void fetchDetailSuccess(OrderDetailBean result) {
        this.mOrderDetailData = result;
        inflateData(result);
    }

    @Override
    public void fetchDetailFail(String msg) {
        ToastUtil.showToast(msg);
    }

    @Override
    public void fetchLogisticTSuccess(LogisticTrackBean tracks) {
        if(tracks == null){
            mLogTrackUtil.syncNilLogisticDatas();
        }else{
            mLogTrackUtil.syncLogisticDatas(tracks);
        }
    }

    @Override
    public void fetchLogisticTFail(String msg) {
        ToastUtil.showToast(msg);
    }

    @Override
    public void refundSuccess() {
        mPresenter.fetchDetailData(mNo);
    }

    @Override
    public void refundFail(String result) {
        ToastUtil.showToast(result);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
