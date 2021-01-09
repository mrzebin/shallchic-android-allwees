package com.project.app.fragment.pay;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.LoggerUtil;
import com.hb.basemodel.utils.SPManager;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.ChionWrapperBean;
import com.project.app.bean.PayOrderBean;
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
    @BindView(R.id.tv_pm_confirmPay)
    TextView tv_pm_confirmPay;
    @BindView(R.id.cl_useDebitC)
    ConstraintLayout cl_useDebitC;
    @BindView(R.id.cl_usePaypal)
    ConstraintLayout cl_usePaypal;
    @BindView(R.id.cl_useCashDelivery)
    ConstraintLayout cl_useCashDelivery;
    @BindView(R.id.iv_applyCash)
    ImageView iv_applyCash;
    @BindView(R.id.iv_pm_usePaypal)
    ImageView iv_pm_usePaypal;
    @BindView(R.id.iv_pm_useDelivery)
    ImageView iv_pm_useDelivery;
    @BindView(R.id.tv_payTotalP)
    TextView tv_payTotalP;
    @BindView(R.id.tv_payProductP)
    TextView tv_payProductP;
    @BindView(R.id.tv_payShippingP)
    TextView tv_payShippingP;
    @BindView(R.id.tv_payDiscountP)
    TextView tv_payDiscountP;
    @BindView(R.id.rl_discount)
    RelativeLayout rl_discount;
    @BindView(R.id.tv_pm_useDeliveryDes)
    TextView tv_pm_useDeliveryDes;
    @BindView(R.id.tv_sunit)
    TextView tv_sunit;

    private PmPayBindPhoneDialog mPayBindDialog;
    private String mOrderId;
    private Double mCodDeductP;
    private Double mAmtP;
    private Double mProductP;
    private Double mShippingP;
    private Double mCouponP;
    private Double mOriginSp;
    private Double mDeductionPrice;
    private String mRegion;
    private String mPhoneNum;
    private String p_unit;
    private boolean isAbbr = false;   //判断是不是中东国家
    private String mHintCod = "";
    private int mPayType   = 0;      //支付类型:0信用卡支付 1paypal支付 2cod支付

    final List<ImageView> mivChoices = new ArrayList<>();

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
            mOrderId          = bundle.getString("uuid");
            mPhoneNum         = bundle.getString("phoneNum");
            mCodDeductP       = bundle.getDouble("codDeductP");
            mAmtP             = bundle.getDouble("amtP");
            mProductP         = bundle.getDouble("productP");
            mShippingP        = bundle.getDouble("shippingP");
            mCouponP          = bundle.getDouble("couponP");
            mOriginSp         = bundle.getDouble("originShipping");
            mRegion           = bundle.getString("region");
            mDeductionPrice   = bundle.getDouble("deductionPrice");
            LoggerUtil.i("mDeductionPrice:" + mDeductionPrice);
        }
    }

    @OnClick({R.id.iv_back,R.id.tv_pm_confirmPay,R.id.cl_useDebitC,R.id.cl_usePaypal,R.id.cl_useCashDelivery})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.iv_back:
                popBackStack();
                break;
            case R.id.tv_pm_confirmPay:
                acrossCheck();
                break;
            case R.id.cl_useDebitC:
                mPayType = 0;
                resetIvState();
                break;
            case R.id.cl_usePaypal:
                mPayType = 1;
                resetIvState();
                break;
            case R.id.cl_useCashDelivery:
                mPayType = 2;
                resetIvState();
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
        buyEventMap.put(AppsFlyConfig.AF_EVENT_PURCHASE, SPManager.sGetString(Constant.SP_DEVICE_MODEL));
        AppsFlyEventUtils.sendAppInnerEvent(buyEventMap, AppsFlyConfig.AF_EVENT_PURCHASE);
    }

    private void resetIvState(){
        for(int i=0;i<mivChoices.size();i++){
            mivChoices.get(i).setImageResource(R.mipmap.ic_select_unpayment);
        }
        mivChoices.get(mPayType).setImageResource(R.mipmap.allwees_ic_landain);
    }

    private void initWidget() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        mPresenter = new PaymentPresenter();
        mPresenter.attachView(this);
        mivChoices.add(iv_applyCash);
        mivChoices.add(iv_pm_usePaypal);
        mivChoices.add(iv_pm_useDelivery);
        String prefixShip = getContext().getResources().getString(R.string.pm_cod_prefix);
        String suffixShip = getContext().getResources().getString(R.string.pm_cod_suffix);
        String hint_sFree = getContext().getResources().getString(R.string.str_free);
        mPayBindDialog    = new PmPayBindPhoneDialog(getContext(),true,true);

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

        p_unit = LocaleMirrorUtil.getCurrentSymbol();
        isAbbr = LocaleMirrorUtil.isAbbr();

        if(isAbbr){
            mPayType = 2;     //默认cod
            resetIvState();   //默认选中货到付款
            cl_usePaypal.setVisibility(View.GONE);
        }else{
            mPayType = 1;    //默认paypal
            resetIvState();
            cl_useCashDelivery.setVisibility(View.GONE);
        }

        if(mShippingP == 0){
            if(mOriginSp >0){
                tv_payShippingP.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tv_payShippingP.setText(MathUtil.Companion.formatPrice(mOriginSp));
                tv_sunit.setText(hint_sFree);
            }
        }else{
            tv_payShippingP.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
            tv_payShippingP.setText( MathUtil.Companion.formatPrice(mShippingP));
        }

        if(mCodDeductP == 0){
            tv_pm_useDeliveryDes.setVisibility(View.GONE);
        }else{
            mHintCod = prefixShip +" " + mCodDeductP+p_unit + " " + suffixShip;
            tv_pm_useDeliveryDes.setText(mHintCod);
        }

        if(mCouponP == 0){
            rl_discount.setVisibility(View.GONE);
        }else{
            tv_payDiscountP.setVisibility(View.VISIBLE);
        }

        tv_payTotalP.setText(MathUtil.Companion.formatPrice(mAmtP));
        tv_payProductP.setText(MathUtil.Companion.formatPrice(mProductP));
        tv_payDiscountP.setText("-" +  MathUtil.Companion.formatPrice(mCouponP));
    }

    @Override
    protected void lazyFetchData() {

    }

    @Override
    public void fetchFail(String failReason) {
        ToastUtil.showToast(failReason);
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
        }
    }

    @Override
    public void fetchCodPaySuccess(PayOrderBean result) {
        mPayBindDialog.dismiss();
        popBackStack();
        Bundle bundle = new Bundle();
        bundle.putString("orderId",result.getUuid());
        bundle.putString("orderType",result.getStateDesc());
        Intent goSuccess = HolderActivity.of(getContext(), PayStatusFragment.class,bundle);
        getContext().startActivity(goSuccess);
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
                Intent goWebPay = HolderActivity.of(getContext(), WebExplorerFragment.class,bundle);
                getContext().startActivity(goWebPay);
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
            bundle.putString("payUuid","");
            Intent intent = HolderActivity.of(getContext(), WebExplorerFragment.class,bundle);
            getContext().startActivity(intent);
        }
    }

    @Override
    public void fetchCreditPayFail(String result) {
        ToastUtil.showToast(result);
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
