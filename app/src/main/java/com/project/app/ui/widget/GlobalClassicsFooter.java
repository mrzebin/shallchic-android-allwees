package com.project.app.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.scwang.smart.drawable.ProgressDrawable;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
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
public class GlobalClassicsFooter extends LinearLayout implements RefreshFooter {
    private ProgressDrawable mProgressDrawable;//刷新动画
    private ImageView mProgressView;//刷新动画视图

    public GlobalClassicsFooter(Context context) {
        this(context,null);
    }

    public GlobalClassicsFooter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        setGravity(Gravity.CENTER);
        mProgressDrawable = new ProgressDrawable();
        mProgressView = new ImageView(context);
        mProgressView.setImageDrawable(mProgressDrawable);
        addView(mProgressView, SmartUtil.dp2px(20), SmartUtil.dp2px(20));
        setMinimumHeight(SmartUtil.dp2px(60));
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return false;
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
            case Refreshing:
                mProgressView.setVisibility(VISIBLE);//显示加载动画
                break;
            case ReleaseToRefresh:
                mProgressView.setVisibility(GONE);//隐藏动画
                break;
        }
    }
}
