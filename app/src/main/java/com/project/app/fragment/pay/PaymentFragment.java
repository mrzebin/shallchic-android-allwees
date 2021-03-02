package com.project.app.fragment.pay;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsflyer.AFInAppEventParameterName;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.SPManager;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.adapter.PaymentCastDetailAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.CartItemReqBean;
import com.project.app.bean.ChionWrapperBean;
import com.project.app.bean.PayOrderBean;
import com.project.app.bean.PaymentCastBean;
import com.project.app.bean.PaymentCheckBean;
import com.project.app.config.AppsFlyConfig;
import com.project.app.contract.PaymentContract;
import com.project.app.fragment.WebExplorerFragment;
import com.project.app.presenter.PaymentPresenter;
import com.project.app.ui.dialog.PmPayBindPhoneDialog;
import com.project.app.utils.AppsFlyEventUtils;
import com.project.app.utils.LocaleMirrorUtil;
import com.project.app.utils.MathUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加信用卡
 */
public class PaymentFragment extends BaseMvpQmuiFragment<PaymentPresenter> implements PaymentContract.View {
    @BindView(R.id.tv_topTitle)
    TextView tv_topTitle;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.srl_payParent)
    SmartRefreshLayout srl_payParent;
    @BindView(R.id.emptyView)
    QMUIEmptyView emptyView;
    @BindView(R.id.btn_pm_confirmPay)
    QMUIRoundButton btn_pm_confirmPay;
    @BindView(R.id.cl_payByDebitCard)
    ConstraintLayout cl_payByDebitCard;
    @BindView(R.id.cl_payByPaypal)
    ConstraintLayout cl_payByPaypal;
    @BindView(R.id.cl_payByCashDelivery)
    ConstraintLayout cl_payByCashDelivery;
    @BindView(R.id.iv_applyCash)
    ImageView iv_applyCash;
    @BindView(R.id.iv_pm_usePaypal)
    ImageView iv_pm_usePaypal; 
    @BindView(R.id.iv_pm_useDelivery)
    ImageView iv_pm_useDelivery;
    @BindView(R.id.tv_payTotalPrice)
    TextView tv_payTotalPrice;
    @BindView(R.id.tv_pm_useDeliveryDes)
    TextView tv_pm_useDeliveryDes;
    @BindView(R.id.iv_arrowExtension)
    ImageView iv_arrowExtension;
    @BindView(R.id.ll_arrow)
    LinearLayout ll_arrow;
    @BindView(R.id.tv_castBackHint)
    TextView tv_castBackHint;
    @BindView(R.id.ll_cashBackWrapper)
    LinearLayout ll_cashBackWrapper;
    @BindView(R.id.v_paypal)
    View v_paypal;
    @BindView(R.id.v_cash)
    View v_cash;
    @BindView(R.id.rlv_castPayPrice)   //计算的价格
    RecyclerView rlv_castPayPrice;

    private PmPayBindPhoneDialog mPayBindDialog;
    private String mOrderId;
    private String mRegion;
    private String mPhoneNum;
    private boolean isInit = false;    //判断是不是初始化
    private boolean isAbbr = false;   //判断是不是中东国家
    private boolean isShippingCart = true; //判断是不是购物车
    private String mHintCod = "";
    private int mPayType   = 0;      //支付类型:0信用卡支付 1paypal支付 2cod支付
    private Animation mArrowExtension;
    private String mCodShippingLinkPrefix;
    private String mCodShippingLinkSuffix;
    private PayOrderBean mPayOrderBean;    //进来时候传的数据
    private CartItemReqBean mRequestOrderFrom;
    final List<ImageView> mivChoices = new ArrayList<>();
    private List<PaymentCastBean> mPaymentItems = new ArrayList<>();
    private final String PAYMENT_TYPE_PAYPAL = "PAYPAL";
    private final String PAYMENT_TYPE_PAYBY = "PAYBY";
    private final String PAYMENT_TYPE_OCEANPAY = "OCEANPAY";
    private final String PAYMENT_TYPE_VISA = "VISA";
    private final String PAYMENT_TYPE_MASTERCARD = "MASTERCARD";
    private final String PAYMENT_TYPE_AMEX = "AMEX";
    private final String PAYMENT_TYPE_JCB = "JCB";
    private final String PAYMENT_TYPE_DISCOVER = "DISCOVER";
    private final String PAYMENT_TYPE_COUPON = "COUPON";
    private final String PAYMENT_TYPE_COD = "COD";
    private PaymentCastDetailAdapter mCastAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pay_ment;
    }

    @Override
    public void initView() {
        initBundle();
        initWidget();
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if(bundle != null){
            mPhoneNum             = bundle.getString("phoneNum");
            mRegion               = bundle.getString("region");
            String holoPayOrder   = bundle.getString("orderPayResult");
            String requestForm    = bundle.getString("orderForm");
            mPayOrderBean         = JsonUtils.deserialize(holoPayOrder,PayOrderBean.class);
            mRequestOrderFrom     = JsonUtils.deserialize(requestForm,CartItemReqBean.class);
        }
    }

    private void entiryGlobalValue(PayOrderBean bean){
        if(bean == null){
            return;
        }
        mOrderId              = bean.getUuid();
        List<PaymentCastBean> tempTolls = new ArrayList<>();
        //total
        tv_payTotalPrice.setText(MathUtil.Companion.formatPrice(bean.getAmt()));
        //item total
        if(isShippingCart){
            tempTolls.add(new PaymentCastBean(getResources().getString(R.string.pm_prefix_tag_product_total),MathUtil.Companion.formatPrice(bean.getOriginalAmtProduct()),0));
        }else{
            tempTolls.add(new PaymentCastBean(getResources().getString(R.string.pm_prefix_tag_product_total),MathUtil.Companion.formatPrice(bean.getAmtProductAfterAdd()),0));
        }

        //shipping
        double shippingPrice = bean.getAmtShipping();
        if(shippingPrice == 0){ //这个是免运费
            String freeShippingPrefix = getResources().getString(R.string.str_upper_free);
            if(isShippingCart){
                tempTolls.add(new PaymentCastBean(getResources().getString(R.string.pm_prefix_tag_shipping),MathUtil.Companion.formatPrice(bean.getOriginalAmtShipping()),1,1,freeShippingPrefix));
            }else{
                tempTolls.add(new PaymentCastBean(getResources().getString(R.string.pm_prefix_tag_shipping),MathUtil.Companion.formatPrice(bean.getAmtShippingAfterAdd()),1,1,freeShippingPrefix));
            }
        }else{
            tempTolls.add(new PaymentCastBean(getResources().getString(R.string.pm_prefix_tag_shipping),MathUtil.Companion.formatPrice(bean.getAmtShipping()),0));
        }

        //couponPrice
        if(bean.getAmtProductDiscount() > 0){
            String model_couponPrice = "-" + MathUtil.Companion.formatPrice(bean.getAmtProductDiscount());
            tempTolls.add(new PaymentCastBean(getResources().getString(R.string.pm_prefix_tag_Discount),model_couponPrice,1));
        }

        if(!TextUtils.isEmpty(bean.getPromoCode())){
            String promoCode = bean.getPromoCode();
            tempTolls.add(new PaymentCastBean(getResources().getString(R.string.pm_prefix_tag_promo_code),promoCode,1));
        }

        //dutyPrice
        if(bean.getDuty() > 0){
            tempTolls.add(new PaymentCastBean(getResources().getString(R.string.pm_prefix_tag_duty),MathUtil.Companion.formatPrice(bean.getDuty()),2));
        }

        //commissionPrice
        if(bean.getCommission() > 0){
            tempTolls.add(new PaymentCastBean(getResources().getString(R.string.pm_prefix_tag_commission),MathUtil.Companion.formatPrice(bean.getCommission()),2));
        }

        //backcashPrice
        if(bean.getCashBackAmt() > 0){
            tv_castBackHint.setText(MathUtil.Companion.formatPrice(bean.getCashBackAmt()) + " " + getResources().getString(R.string.pm_cast_back_reward_amt_prefix));
        }
        mCastAdapter.setNewInstance(tempTolls);
    }

    @OnClick({R.id.iv_back,R.id.btn_pm_confirmPay,R.id.cl_payByDebitCard,R.id.cl_payByPaypal,R.id.cl_payByCashDelivery,R.id.iv_arrowExtension})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.iv_back:
                popBackStack();
                break;
            case R.id.btn_pm_confirmPay:
                mPresenter.createPayOrder(mRequestOrderFrom);
                break;
            case R.id.cl_payByDebitCard:
                mPayType = 0;
                resetIvState();
                lazyFetchData();
                break;
            case R.id.cl_payByPaypal:
                mPayType = 1;
                resetIvState();
                lazyFetchData();
                break;
            case R.id.cl_payByCashDelivery:
                mPayType = 2;
                resetIvState();
                lazyFetchData();
                break;
            case R.id.iv_arrowExtension:

                break;
        }
    }

    private void acrossCheck(){
        if(mPayType == 0){   //信用卡支付
            mPresenter.creditCardPay(mOrderId);
        }else if(mPayType == 1){
            mPresenter.goPayPal(mOrderId);
        }else if(mPayType == 2){
            mPresenter.checkCod(mOrderId);
        }
        Map<String,Object> buyEventMap = new HashMap<>();
        buyEventMap.put(AFInAppEventParameterName.CUSTOMER_USER_ID, SPManager.sGetString(Constant.SP_DEVICE_ID_FLAG));
        AppsFlyEventUtils.sendAppInnerEvent(buyEventMap, AppsFlyConfig.AF_EVENT_PURCHASE);
    }

    //注意每次调用都要刷新接口
    private void resetIvState(){
        for(int i=0;i<mivChoices.size();i++){
            mivChoices.get(i).setImageResource(R.mipmap.allwees_circle_yellow_default);
        }
        mivChoices.get(mPayType).setImageResource(R.mipmap.allwees_circle_yellow_select);
    }

    private void initWidget() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        btn_pm_confirmPay.setChangeAlphaWhenPress(true);
        mPresenter = new PaymentPresenter();
        mPresenter.attachView(this);
        isAbbr = LocaleMirrorUtil.isAbbr();
        mivChoices.add(iv_applyCash);
        mivChoices.add(iv_pm_usePaypal);
        mivChoices.add(iv_pm_useDelivery);

        rlv_castPayPrice.setLayoutManager(new LinearLayoutManager(getContext()));
        mCastAdapter = new PaymentCastDetailAdapter(mPaymentItems);
        rlv_castPayPrice.setAdapter(mCastAdapter);
        entiryGlobalValue(mPayOrderBean);

        mArrowExtension = AnimationUtils.loadAnimation(getContext(),R.anim.anim_arrow_hide_or_show_extension_paystatus);    //箭头显示或隐藏的动画
        mArrowExtension.setInterpolator(new LinearInterpolator());

        mCodShippingLinkPrefix = getContext().getResources().getString(R.string.pm_cod_prefix);
        mCodShippingLinkSuffix = getContext().getResources().getString(R.string.pm_cod_suffix);
        mPayBindDialog    = new PmPayBindPhoneDialog(getContext(),false,false);

        mPayBindDialog.setListener(new PmPayBindPhoneDialog.Reject_callBack() {
            @Override
            public void sendSmsVC() {
                mPresenter.smsVerify(mOrderId,mPhoneNum);
            }

            @Override
            public void confirmPay(String code) {
                mPresenter.codPay(mOrderId,mPhoneNum,code);
            }
        });

        if(isAbbr){
            mPayType = 2;     //默认cod
            resetIvState();   //默认选中货到付款
            cl_payByPaypal.setVisibility(View.GONE);
            v_paypal.setVisibility(View.GONE);
        }else{
            mPayType = 0;    //默认信用卡
            resetIvState();
            cl_payByCashDelivery.setVisibility(View.GONE);
            v_cash.setVisibility(View.GONE);
        }
    }

    @Override
    protected void lazyFetchData() {
        if(mPayType == 0){
            mRequestOrderFrom.setPaymentType(PAYMENT_TYPE_OCEANPAY);
        }else if(mPayType == 1){
            mRequestOrderFrom.setPaymentType(PAYMENT_TYPE_PAYPAL);
        }else if(mPayType == 2){
            mRequestOrderFrom.setPaymentType(PAYMENT_TYPE_COD);
        }
        mPresenter.refreshPayOrder(isInit,mRequestOrderFrom);
    }

    @Override
    public void fetchCheckCodFail(String result) {
        ToastUtil.showToast(result);
    }

    @Override
    public void fetchSmsVerfyFail(String result) {
        ToastUtil.showToast(result);
    }

    @Override
    public void fetchSmsVerfySuccess(String result) {
        mPayBindDialog.startTimer();
    }

    @Override
    public void fetchCodPayFail(String result) {
        ToastUtil.showToast(result);
    }

    @Override
    public void fetchCheckCodSuccess(PaymentCheckBean result) {
        stopProgressDialog();
        if(result.getPhoneCheck()){
            mPayBindDialog.syncData(mPhoneNum);
            mPayBindDialog.show();
        }else{
            mPresenter.codPay(mOrderId,mPhoneNum,"");
        }
    }

    @Override
    public void fetchCodPaySuccess(PayOrderBean result) {
        mPayBindDialog.dismiss();
        popBackStack();
        Bundle bundle = new Bundle();
        bundle.putString("orderId",result.getUuid());
        HolderActivity.startFragment(getContext(), com.project.app.fragment.pay.PayStatusFragment.class,bundle);
    }

    @Override
    public void fetchPapalH5(ChionWrapperBean result) {
        stopProgressDialog();
        if(result != null){
            String approvalURL = result.getApprovalURL();
            if(!TextUtils.isEmpty(approvalURL)){
                Bundle bundle = new Bundle();
                bundle.putString("webUrl",approvalURL);
                bundle.putString("type","0");
                bundle.putString("payUuid",result.getOrderUuid());
                HolderActivity.startFragment(getContext(),WebExplorerFragment.class,bundle);
            }
        }
    }

    @Override
    public void fetchCreditPaySuccess(String result) {
        stopProgressDialog();
        if(result != null){
            String url = "";
            Bundle bundle = new Bundle();
            bundle.putString("webUrl", url);
            bundle.putString("type","3");
            bundle.putString("payParams", result);
            bundle.putString("payUuid",mOrderId);
            HolderActivity.startFragment(getContext(),WebExplorerFragment.class,bundle);
        }
    }

    @Override
    public void fetchCreditPayFail(String result) {
        ToastUtil.showToast(result);
    }

    @Override
    public void fetchRefreshOrderSuccess(PayOrderBean result) {      //生成订单成功
        isInit = true;                                              //已经初始化
        if(emptyView.getVisibility() == View.VISIBLE){
            emptyView.setVisibility(View.GONE);
            srl_payParent.setVisibility(View.VISIBLE);
        }
        if(result != null){
            isShippingCart = false;
            entiryGlobalValue(result);
        }
    }

    //生成订单成功
    @Override
    public void fetchCreateOrderSuccess(PayOrderBean result) {
        if(result == null){
            return;
        }
        if(!TextUtils.isEmpty(result.getUuid())){
            mOrderId = result.getUuid();
            acrossCheck();
        }
    }

    @Override
    public void fetchFail(String failReason) {
        ToastUtil.showToast(failReason);
        isInit = true;              //已经初始化
        if(emptyView.getVisibility() == View.VISIBLE){
            emptyView.setVisibility(View.GONE);
            srl_payParent.setVisibility(View.VISIBLE);
        }
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
        stopProgressDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPayBindDialog.clear();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
