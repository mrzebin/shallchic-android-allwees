package com.project.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.basemodel.utils.ToastUtil;
import com.hb.basemodel.view.DelEditView;
import com.project.app.R;

/**
 * 绑定手机
 */
public class PmPayBindPhoneDialog {
    private final Context mContext;
    private View mView;
    private final Dialog mDialog;
    private ScaleAnimation mScaleAnima;
    private String mTargetNum;
    private TextView tv_pm_targetPhone;
    private TextView tv_sendVc;
    private TextView tv_confirmP;
    private TextView tv_notReceiveVerifyCode;
    private ImageView iv_close_dialogVc;
    private DelEditView et_verifyCode;
    private CountDownTimer timer;
    private final long expire = 60 *1000;   //倒计时60s

    public PmPayBindPhoneDialog(Context context, boolean isCancelable, boolean iscancelOutside){
        this.mContext = context;
        mDialog = new Dialog(context, R.style.ScaleDialog);
        mDialog.setCancelable(isCancelable);
        mDialog.setCanceledOnTouchOutside(iscancelOutside);
        build();
    }

    public void build(){
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_pm_bind_phone,null);
        mDialog.setContentView(mView);
        WindowManager wManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wManager.getDefaultDisplay();
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        wlp.width = (int) (display.getWidth()*0.8);
        wlp.height = display.getHeight();
        window.setAttributes(wlp);
        initChildView();
        initAnim();
    }

    private void initAnim() {
        mScaleAnima = new ScaleAnimation(1,1.2f,1,1.2f, 0.5f,0.5f);
        mScaleAnima.setDuration(300);
        mScaleAnima.setFillAfter(false);
        mScaleAnima.setRepeatCount(0);
    }

    private void initChildView() {
        tv_pm_targetPhone = mView.findViewById(R.id.tv_pm_targetPhone);
        tv_sendVc = mView.findViewById(R.id.tv_sendVc);
        tv_confirmP = mView.findViewById(R.id.tv_confirmP);
        et_verifyCode = mView.findViewById(R.id.et_verifyCode);
        iv_close_dialogVc = mView.findViewById(R.id.iv_close_dialogVc);
        tv_notReceiveVerifyCode = mView.findViewById(R.id.tv_notReceiveVerifyCode);

        et_verifyCode.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                String inputEdit = editable.toString().trim();
                if(inputEdit.length() >=4){
                    tv_confirmP.setClickable(true);
                    tv_confirmP.setSelected(true);
                }else{
                    tv_confirmP.setSelected(false);
                    tv_confirmP.setClickable(false);
                }
            }
        });

        tv_sendVc.setOnClickListener(view -> {
            tv_sendVc.setClickable(false);
            if(listener != null){
                listener.sendSmsVC();
            }
        });

        tv_confirmP.setOnClickListener(view -> {
            String code = et_verifyCode.editText.getText().toString().trim();
            if(!TextUtils.isEmpty(code)){
                if(listener != null){
                    listener.confirmPay(code);
                }
            }else{
                ToastUtil.showToast(mContext.getResources().getString(R.string.please_enter_verify_code));
            }
        });

        iv_close_dialogVc.setOnClickListener(view -> {
            if(mDialog != null){
                mDialog.dismiss();
            }
        });

        tv_notReceiveVerifyCode.setOnClickListener(view -> {

        });
    }

    public void startTimer(){
        timer = new CountDownTimer(expire, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished/1000 <=45){
                    tv_notReceiveVerifyCode.setVisibility(View.VISIBLE);
                }
                tv_sendVc.setText(millisUntilFinished/1000 + "s");
            }
            @Override
            public void onFinish() {
                tv_sendVc.setText(mContext.getResources().getString(R.string.str_resend));
                tv_sendVc.setClickable(true);
            }
        };
        timer.start();
    }

    public void show(){
        if(mDialog != null){
            tv_sendVc.setClickable(true);
            tv_sendVc.setText(mContext.getResources().getString(R.string.str_resend));
            tv_notReceiveVerifyCode.setVisibility(View.INVISIBLE);
            mDialog.show();
            if(timer != null){
                timer.cancel();
                timer = null;
            }
        }
    }

    public void dismiss() {
        if(mDialog != null){
            mDialog.dismiss();
        }
    }

    public void syncData(String phoneNum) {
        mTargetNum = phoneNum;
        tv_pm_targetPhone.setText(phoneNum);
    }

    public void clear() {
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    public Reject_callBack listener;

    public Reject_callBack getListener() {
        return listener;
    }

    public void setListener(Reject_callBack listener) {
        this.listener = listener;
    }

    public interface Reject_callBack{
        void sendSmsVC();
        void confirmPay(String code);
    }

}
