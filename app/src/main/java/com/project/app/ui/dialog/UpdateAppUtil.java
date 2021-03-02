package com.project.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.app.R;
import com.project.app.bean.AppUpdateBean;

/**
 * create by hb
 * 新人领取优惠券
 */
public class UpdateAppUtil {
    private final Context mContext;
    private View mView;
    private final Dialog mDialog;
    private ScaleAnimation mScaleAnima;
    private RelativeLayout rl_updateCancal;
    private RelativeLayout rl_updateOk;
    private TextView tv_updateDescribe;
    private AppUpdateBean mUpdateInfo;

    public UpdateAppUtil(Context context, boolean isCancelable, boolean iscancelOutside){
        this.mContext = context;
        mDialog = new Dialog(context, R.style.ScaleDialog);
        mDialog.setCancelable(isCancelable);
        mDialog.setCanceledOnTouchOutside(iscancelOutside);
        build();
    }

    public void build(){
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_update_app,null);
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
    }

    private void initAnim() {
        mScaleAnima = new ScaleAnimation(1,1.2f,1,1.2f, 0.5f,0.5f);
        mScaleAnima.setDuration(300);
        mScaleAnima.setFillAfter(false);
        mScaleAnima.setRepeatCount(0);
    }

    private void initChildView() {
        tv_updateDescribe = mView.findViewById(R.id.tv_updateDescribe);
        rl_updateCancal = mView.findViewById(R.id.rl_updateCancal);
        rl_updateOk     = mView.findViewById(R.id.rl_updateOk);

        rl_updateOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    if(!TextUtils.isEmpty(mUpdateInfo.getDownUrl())){
                        listener.selectUpdateApp(mUpdateInfo.getDownUrl(),mUpdateInfo.getContent());
                    }
                    dismiss();
                }
            }
        });

        rl_updateCancal.setOnClickListener(new View.OnClickListener() {
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

    public void show(AppUpdateBean bean) {
        mUpdateInfo = bean;
        if(mDialog != null && !mDialog.isShowing()){
            if(!TextUtils.isEmpty(bean.getContent())){
                tv_updateDescribe.setText(bean.getContent());
            }
            mDialog.show();
        }
    }

    public CallbackOpreateListener listener;

    public CallbackOpreateListener getListener() {
        return listener;
    }

    public void setListener(CallbackOpreateListener listener) {
        this.listener = listener;
    }

    public interface CallbackOpreateListener{
        void selectUpdateApp(String downUrl,String content);
    }

}
