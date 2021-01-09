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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    private ImageView iv_close_dialogVc;
    private EditText et_vrifyCode;
    private CountDownTimer timer;
    private final long expire = 60 *1000;   //倒计时60s

    public PmPayBindPhoneDialog(Context context, boolean isCancelable, boolean iscancelOutside){
        this.mContext = context;
        mDialog = new Dialog(context, R.style.transparentFrameWindowStyle);
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
        et_vrifyCode = mView.findViewById(R.id.et_vrifyCode);
        iv_close_dialogVc = mView.findViewById(R.id.iv_close_dialogVc);

        et_vrifyCode.addTextChangedListener(new TextWatcher() {
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
            String code = et_vrifyCode.getText().toString().trim();
            if(!TextUtils.isEmpty(code)){
                if(listener != null){
                    listener.confirmPay(code);
                }
            }
        });

        iv_close_dialogVc.setOnClickListener(view -> {
            if(mDialog != null){
                mDialog.dismiss();
            }
        });

        timer = new CountDownTimer(expire, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_sendVc.setText(millisUntilFinished/1000 + "s");
            }
            @Override
            public void onFinish() {
                tv_sendVc.setText("Resend");
                tv_sendVc.setClickable(true);
            }
        };
    }


    public void startTimer(){
        timer.start();
    }

    public void show(){
        if(mDialog != null){
            mDialog.show();
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
        timer.cancel();
        timer = null;
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
