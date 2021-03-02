package com.project.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.hb.basemodel.other.UserUtil;
import com.hb.basemodel.uicomponent.roundimageview.RoundedImageView;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.activity.LoginActivity;
import com.project.app.fragment.home.classify.HomeClassifyGiftsFragment;

/**
 * create by hb
 * 新人领取优惠券
 */
public class RunoobCouponUtil {
    private final Context mContext;
    private View mView;
    public  Dialog mDialog;
    private ScaleAnimation mScaleAnima;
    private ImageView  iv_closeDialog;
    private RoundedImageView dialog_riv_runoobGift;

    public RunoobCouponUtil(Context context, boolean isCancelable, boolean iscancelOutside){
        this.mContext = context;
        mDialog = new Dialog(context, R.style.ScaleDialog);
        mDialog.setCancelable(isCancelable);
        mDialog.setCanceledOnTouchOutside(iscancelOutside);
        build();
    }

    public void build(){
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_runoob_coupon,null);
        mDialog.setContentView(mView);
        WindowManager wManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wManager.getDefaultDisplay();
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        wlp.width = (int) (display.getWidth());
        window.setAttributes(wlp);
        initChildView();
        initAnim();

        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(mlistener != null){
                    mlistener.dialogCancel();
                }
            }
        });
    }

    private void initAnim() {
        mScaleAnima = new ScaleAnimation(1,1.2f,1,1.2f, 0.5f,0.5f);
        mScaleAnima.setDuration(300);
        mScaleAnima.setFillAfter(false);
        mScaleAnima.setRepeatCount(0);
    }

    private void initChildView() {
        iv_closeDialog = mView.findViewById(R.id.iv_closeDialog);
        dialog_riv_runoobGift = mView.findViewById(R.id.dialog_riv_runoobGift);

        dialog_riv_runoobGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserUtil.getInstance().isLogin()){
                    HolderActivity.startFragment(mContext,HomeClassifyGiftsFragment.class);
                }else{
                    Intent goLogin = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(goLogin);
                }
                dismiss();
            }
        });

        iv_closeDialog.setOnClickListener(new View.OnClickListener() {
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

    public RunoobObserveCancelListener mlistener;

    public RunoobObserveCancelListener getListener() {
        return mlistener;
    }

    public void setListener(RunoobObserveCancelListener listener) {
        this.mlistener = listener;
    }

    public interface RunoobObserveCancelListener{
        void dialogCancel();
    }

}
