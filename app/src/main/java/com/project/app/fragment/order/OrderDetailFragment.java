package com.project.app.fragment.order;

import android.content.Intent;
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
import com.project.app.adapter.OrderDetailKindsAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.LogisticTrackBean;
import com.project.app.bean.OrderDetailBean;
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

    private String mNo;
    private String mOrderType;
    private String mTrackNo;
    private OrderDetailBean mOrderDetailData;
    private OrderDetailKindsAdapter mAdapter;
    private LogisticDetailUtil mLogTrackUtil;
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
        mOrderType = bundle.getString("orderType");
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
        rlv_detailItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rlv_detailItems.setAdapter(mAdapter);
        mLogTrackUtil = new LogisticDetailUtil(getContext(),true,true);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.allwees_theme_color,android.R.color.holo_blue_dark,android.R.color.holo_orange_dark);

        mAdapter.setListener((item, taskType) -> {
            switch (taskType) {
                case "review":

                    break;
                case "refund": {
                    Bundle baba = new Bundle();
                    baba.putString("orderUuid", mNo);
                    baba.putString("orderItemUuid", item.getUuid());
                    Intent goRefundD = HolderActivity.of(getContext(), OrderRefundDetailFragment.class, baba);
                    getContext().startActivity(goRefundD);
                    break;
                }
                case "lookLog":
                    mTrackNo = item.getUuid();
                    mPresenter.checkLogisticTrack(mTrackNo);
                    break;
                case "receive":
                    mPresenter.receiveGoodsRequest(item.getUuid());
                    break;
                case "cancel": {
                    Bundle baba = new Bundle();
                    baba.putString("orderUuid", mNo);
                    baba.putString("orderItemUuid", item.getUuid());
                    Intent goCancel = HolderActivity.of(getContext(), OrderCancelByReasonragment.class, baba);
                    getContext().startActivity(goCancel);
                    break;
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEvent(RefreshDataEvent event){
        if(event.getmMsg().equals(Constant.EVENT_REFUND_SUCCESS)){
            mPresenter.fetchDetailData(mNo,mOrderType);
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

            if(result.getAmtCash() >0){
                tv_cashMoney.setText("-" + MathUtil.Companion.formatPrice(result.getAmtCash()));
            }else{
                rl_cash.setVisibility(View.GONE);
                v_cash.setVisibility(View.GONE);
            }

            if(result.getAmtDeduction() >0 ){
                tv_decution.setText(MathUtil.Companion.formatPrice(result.getAmtDeduction()));
            }else{
                rl_deduction.setVisibility(View.GONE);
                v_decution.setVisibility(View.GONE);
            }

            if(!TextUtils.isEmpty(result.getPromoCode()) && result.getAmtProductCoupon() >0){
                tv_promoCode.setText(result.getPromoCode());
                tv_couponPrice.setText("-" + MathUtil.Companion.formatPrice(result.getAmtProductCoupon()));
            }else{
                rl_pcl.setVisibility(View.GONE);
                v_pcl.setVisibility(View.GONE);
            }

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
            if(result.getPaymentAt() >0){
                tv_odPayDate.setText(StringUtils.getEnDateFormat(result.getPaymentAt()) + " " + result.getStateDesc());
            }

            String createTime = StringUtils.getEnDateFormat(result.getCreatedAt());
            String paytTime   = StringUtils.getEnDateFormat(result.getPaymentExpiredAt());

            tv_odCity.setText(sb_splid.toString());
            tv_orderDate.setText(createTime);
            tv_orderPayDate.setText(paytTime);
            tv_productMoney.setText(MathUtil.Companion.formatPrice(result.getAmtProduct()));
            tv_shippingMoney.setText(MathUtil.Companion.formatPrice(result.getAmtShipping()));
            tv_orderTotalMoney.setText(MathUtil.Companion.formatPrice(result.getAmt()));
        }
    }

    @Override
    public void onRefresh() {
        mSwipeRefresh.setRefreshing(false);
        mPresenter.fetchDetailData(mNo,mOrderType);
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
        mPresenter.fetchDetailData(mNo,mOrderType);
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
        mPresenter.fetchDetailData(mNo,mOrderType);
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
