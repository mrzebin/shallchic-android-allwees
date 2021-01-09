package com.project.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.project.app.R;

/**
 * pay choice method
 */
public class PmChoiceDeliverDialogUtil {
    private final Context mContext;
    private View mView;
    private final Dialog mDialog;
    private ScaleAnimation mScaleAnima;
    private TextView tv_shopAddMore;
    private TextView tv_shopOk;

    public PmChoiceDeliverDialogUtil(Context context, boolean isCancelable, boolean iscancelOutside){
        this.mContext = context;
        mDialog = new Dialog(context, R.style.transparentFrameWindowStyle);
        mDialog.setCancelable(isCancelable);
        mDialog.setCanceledOnTouchOutside(iscancelOutside);
        build();
    }

    public void build(){
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_choice_pay_method,null);
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

    private void initChildView(){
        tv_shopOk = mView.findViewById(R.id.tv_shopOk);
        tv_shopAddMore = mView.findViewById(R.id.tv_shopAddMore);

        tv_shopOk.setOnClickListener(view -> {
            if(mDialog != null){
                mDialog.dismiss();
            }
        });
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


    public interface Reject_callBack{
        void cancel();
        void sure();
    }

}
