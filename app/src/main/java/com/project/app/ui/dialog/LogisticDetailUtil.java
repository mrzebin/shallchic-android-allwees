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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.basemodel.utils.DataUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.project.app.R;
import com.project.app.adapter.LogisticTrackAdapter;
import com.project.app.bean.LogisticTrackBean;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * create by hb
 * 物流 dialog
 */
public class LogisticDetailUtil {
    private final Context mContext;
    private View mView;
    private final Dialog mDialog;
    private String mTrackNo;
    private TextView tv_logisticNo;
    private RecyclerView rvl_logisticTrack;
    private List<LogisticTrackBean.LogisticTrackItem> mTrackList = new ArrayList<>();
    private LogisticTrackAdapter mAdapter;
    private ImageView iv_closeLogistic;
    private TextView tv_monitor;
    private ImageView iv_copyTrackNo;
    private ScaleAnimation mScaleAnima;

    public LogisticDetailUtil(Context context, boolean isCancelable, boolean iscancelOutside){
        this.mContext = context;
        mDialog = new Dialog(context, R.style.transparentFrameWindowStyle);
        mDialog.setCancelable(isCancelable);
        mDialog.setCanceledOnTouchOutside(iscancelOutside);
        build();
    }

    public void build(){
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_logistic_detail,null);
        mDialog.setContentView(mView);
        WindowManager wManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wManager.getDefaultDisplay();
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        wlp.width = (int) (display.getWidth()*0.8);
        wlp.height = (int) (display.getHeight() *0.8);
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
        tv_logisticNo = mView.findViewById(R.id.tv_logisticNo);
        rvl_logisticTrack = mView.findViewById(R.id.rvl_logisticTrack);
        rvl_logisticTrack.setLayoutManager(new LinearLayoutManager(mContext));
        iv_closeLogistic = mView.findViewById(R.id.iv_closeLogistic);
        tv_monitor = mView.findViewById(R.id.tv_logisticNo);
        iv_copyTrackNo = mView.findViewById(R.id.iv_copyTrackNo);

        iv_closeLogistic.setOnClickListener(view -> {
            if(mDialog != null){
                mDialog.dismiss();
            }
        });

        iv_copyTrackNo.setOnClickListener(view -> {
            iv_copyTrackNo.setAnimation(mScaleAnima);
            iv_copyTrackNo.startAnimation(mScaleAnima);
            String copyNo = tv_logisticNo.getText().toString().trim();
            if(TextUtils.isEmpty(copyNo)){
               return;
            }
            ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label", copyNo);
            cm.setPrimaryClip(mClipData);
        });
    }

    public void dismiss() {
        if(mDialog != null){
            mDialog.dismiss();
        }
    }

    public void syncLogisticDatas(LogisticTrackBean tracks) {
        this.mTrackList = tracks.getData();
        if(mDialog != null){
            if(DataUtil.idNotNull(tracks.getData())){
                mAdapter = new LogisticTrackAdapter(tracks.getData());
                rvl_logisticTrack.setAdapter(mAdapter);
                if(!TextUtils.isEmpty(tracks.getTrackingNumber())){
                    tv_logisticNo.setText(tracks.getTrackingNumber());
                }
            }else{
                mAdapter = new LogisticTrackAdapter(mTrackList);
                mAdapter.setEmptyView(getEmptyLogistic());
                rvl_logisticTrack.setAdapter(mAdapter);
                tv_logisticNo.setText("");
            }
            mDialog.show();
        }
    }

    private View getEmptyLogistic(){
        String noData = mContext.getResources().getString(R.string.str_nd);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_empty_order,null);
        TextView tv_hint = view.findViewById(R.id.tv_emptyOrderHint);
        LinearLayout cl_inflateView = view.findViewById(R.id.cl_refreshEmpty);
        cl_inflateView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, QMUIDisplayHelper.dp2px(mContext,350)));
        QMUIRoundButton qrb_refreshOrder = view.findViewById(R.id.qrb_refreshOrder);
        qrb_refreshOrder.setVisibility(View.GONE);
        tv_hint.setText(noData);
        return view;
    }

    public void syncNilLogisticDatas() {
        mAdapter = new LogisticTrackAdapter(mTrackList);
        mAdapter.setEmptyView(getEmptyLogistic());
        rvl_logisticTrack.setAdapter(mAdapter);
        tv_logisticNo.setText("");
        if(mDialog != null){
            mDialog.show();
        }
    }

    public interface Reject_callBack{
        void cancel();
        void sure();
    }

}
