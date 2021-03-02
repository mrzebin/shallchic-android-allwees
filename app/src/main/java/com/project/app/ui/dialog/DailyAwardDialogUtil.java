package com.project.app.ui.dialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.app.R;
import com.project.app.bean.DailySignInBean;
import com.project.app.utils.MathUtil;
import com.project.app.utils.StringUtils;

/**
 * create by hb
 * 登录满7天奖励弹窗
 *
 * {
 *       "msg" : "Success",
 *       "code" : 1,
 *       "data" : {
 *         "signedIn" : false,
 *         "signedTimes" : 7,
 *         "userCoupon" : {
 *           "uuid" : "827b6be78c29445bb10cf5b3c46d4492",
 *           "status" : 0,
 *           "createdAt" : 1614413557993,
 *           "updatedAt" : 1614413557993,
 *           "idx" : 0,
 *           "no" : "WFAGN7EJ",
 *           "userUuid" : "5f1c517ab5a1400f9a8d984890f9931c",
 *           "receiveDate" : 1614413557992,
 *           "overdueTime" : 1616141557992,
 *           "couponUuid" : "c9611687196e4a2ca246625dbb86030e",
 *           "couponName" : "Daily Login Bonus",
 *           "couponUseType" : "SYSTEM_SING",
 *           "couponType" : "RATE",
 *           "faceValue" : 0.95,
 *           "maxDeductAmt" : 50.00,
 *           "valueText" : "5%",
 *           "statusDesc" : "normal"
 *         },
 *         "completedAt" : 1614384000000
 *       }
 *     }
 *
 */
public class DailyAwardDialogUtil {
    private final Context mContext;
    private View mView;
    private final Dialog mDialog;
    private ScaleAnimation mScaleAnima;
    private TextView tv_couponRate;
    private TextView tv_dailyExpireDate;
    private TextView tv_maxCouponAmt;
    private TextView tv_couponNo;
    private TextView tv_congratOff;
    private ImageView iv_startShopping;
    private LinearLayout ll_kissCoupon;
    private ImageView iv_copyTrackNo;
    private DailySignInBean.UserCouponItem mData;

    public DailyAwardDialogUtil(Context context, boolean isCancelable, boolean iscancelOutside){
        this.mContext = context;
        mDialog = new Dialog(context, R.style.ScaleDialog);
        mDialog.setCancelable(isCancelable);
        mDialog.setCanceledOnTouchOutside(iscancelOutside);
        build();
    }

    public void build(){
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_full_daily_award,null);
        mDialog.setContentView(mView);
        WindowManager wManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wManager.getDefaultDisplay();
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        wlp.width = (int) (display.getWidth()*0.9);
        wlp.height = (int) (display.getHeight()*0.8);
        window.setAttributes(wlp);
        initChildView();
        initAnim();
    }

    private void initAnim() {
        mScaleAnima = new ScaleAnimation(0.9f,1,0.9f,1f, 0.5f,0.5f);
        mScaleAnima.setDuration(300);
        mScaleAnima.setFillAfter(false);
        mScaleAnima.setRepeatCount(0);
    }

    private void initChildView() {
        tv_couponRate      = mView.findViewById(R.id.tv_couponRate);
        tv_maxCouponAmt    = mView.findViewById(R.id.tv_maxCouponAmt);
        tv_dailyExpireDate = mView.findViewById(R.id.tv_dailyExpireDate);
        tv_couponNo        = mView.findViewById(R.id.tv_couponNo);
        tv_congratOff      = mView.findViewById(R.id.tv_congratOff);
        iv_startShopping   = mView.findViewById(R.id.iv_startShopping);
        ll_kissCoupon      = mView.findViewById(R.id.ll_kissCoupon);
        iv_copyTrackNo     = mView.findViewById(R.id.iv_copyTrackNo);

        ll_kissCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_copyTrackNo.setAnimation(mScaleAnima);
                iv_copyTrackNo.startAnimation(mScaleAnima);
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", mData.getNo());
                cm.setPrimaryClip(mClipData);
            }
        });

        iv_startShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void dismiss() {
        if(mDialog != null){
            mDialog.dismiss();
        }
    }

    public void show() {
        if(mDialog != null && !mDialog.isShowing()){
            mDialog.show();
        }
    }

    public void syncCouponData(DailySignInBean.UserCouponItem userCoupon) {
        this.mData = userCoupon;

        if(!TextUtils.isEmpty(userCoupon.getValueText())){
            tv_couponRate.setText(userCoupon.getValueText());
        }
        if(userCoupon.getOverdueTime() >0){
            tv_dailyExpireDate.setText(StringUtils.getEnMDFormat(userCoupon.getOverdueTime()));
        }

        StringBuffer sbMaxBounds = new StringBuffer();
        String maxBunds = mContext.getResources().getString(R.string.dialog_daily_award_describe_max);
        sbMaxBounds.append(maxBunds + " ");
        sbMaxBounds.append(MathUtil.Companion.formatPrice(userCoupon.getMaxDeductAmt()));
        tv_maxCouponAmt.setText(sbMaxBounds.toString());

        StringBuffer sbCongrats = new StringBuffer();
        String  congratPrefixOff =  mContext.getResources().getString(R.string.dialog_daily_award_describe_rate_off);
        String  congratSurfixOff = mContext.getResources().getString(R.string.dialog_daily_award_off);

        if(!TextUtils.isEmpty(userCoupon.getValueText())){
            sbCongrats.append(congratPrefixOff + " ");
            sbCongrats.append(userCoupon.getValueText());
            sbCongrats.append(congratSurfixOff + " ");
            tv_congratOff.setText(sbCongrats.toString());
        }

        if(!TextUtils.isEmpty(userCoupon.getNo())){
            tv_couponNo.setText(userCoupon.getNo());
        }
    }
}
