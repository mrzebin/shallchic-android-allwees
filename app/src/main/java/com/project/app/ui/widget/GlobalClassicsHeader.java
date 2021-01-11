package com.project.app.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.project.app.R;
import com.scwang.smart.drawable.ProgressDrawable;
import com.scwang.smart.refresh.classics.ArrowDrawable;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.scwang.smart.refresh.layout.util.SmartUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 全局的加载头部
 */
public class GlobalClassicsHeader extends LinearLayout implements RefreshHeader {
    private TextView mHeaderText;//标题文本
    private ImageView mArrowView;//下拉箭头
    private ImageView mProgressView;//刷新动画视图
    private ProgressDrawable mProgressDrawable;//刷新动画
    private String mHintPull;
    private String mHintRealse;
    private String mLoadding;
    private String mLoadFinish;

    public GlobalClassicsHeader(Context context) {
        this(context,null);
    }

    public GlobalClassicsHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        setGravity(Gravity.CENTER);
        mHeaderText = new TextView(context);
        mProgressDrawable = new ProgressDrawable();
        mArrowView = new ImageView(context);
        mProgressView = new ImageView(context);
        mProgressView.setImageDrawable(mProgressDrawable);
        mArrowView.setImageDrawable(new ArrowDrawable());
//        mArrowView.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_refresh_arrow));
        addView(mProgressView, SmartUtil.dp2px(20), SmartUtil.dp2px(20));
        addView(mArrowView, SmartUtil.dp2px(20), SmartUtil.dp2px(20));
        addView(new Space(context), SmartUtil.dp2px(20), SmartUtil.dp2px(20));
        addView(mHeaderText, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        setMinimumHeight(SmartUtil.dp2px(50));
        mHintPull = context.getResources().getString(R.string.pull_loading_hint);
        mHintRealse = context.getResources().getString(R.string.release_to_refresh);
        mLoadding =  context.getResources().getString(R.string.loadding);
        mLoadFinish = context.getResources().getString(R.string.load_finish);
    }

    @NonNull
    @Override
    public View getView() {
        return this;//真实的视图就是自己，不能返回null
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        mProgressDrawable.start();//开始动画
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        mProgressDrawable.stop();//停止动画
        mProgressView.setVisibility(GONE);//隐藏动画
        if (success){
            mHeaderText.setText(mLoadFinish);
        } else {
            mHeaderText.setText(mLoadFinish);
        }
        return 500;//延迟500毫秒之后再弹回
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                mHeaderText.setText(mHintPull); //下拉开始刷新
                mArrowView.setVisibility(VISIBLE);//显示下拉箭头
                mProgressView.setVisibility(GONE);//隐藏动画
                mArrowView.animate().rotation(0);//还原箭头方向
                break;
            case Refreshing:
                mHeaderText.setText(mLoadding);  //正在刷新
                mProgressView.setVisibility(VISIBLE);//显示加载动画
                mArrowView.setVisibility(GONE);//隐藏箭头
                break;
            case ReleaseToRefresh:
                mHeaderText.setText(mHintRealse);  //释放立即刷新
                mArrowView.animate().rotation(180);//显示箭头改为朝上
                break;
        }
    }
}
