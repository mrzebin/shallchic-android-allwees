package com.project.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.LinkedHashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {
    private int current;
    private int height = 0;

    /**
     * 保存position与对于view
     * @param context
     */
    private HashMap<Integer, View> mChildrenViews = new LinkedHashMap<Integer, View>();
    private boolean isScrollable = true;

    public CustomViewPager(@NonNull Context context) {
        super(context);
    }

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mChildrenViews.size() > current){
            View child = mChildrenViews.get(current);
            if(child != null){
                child.measure(widthMeasureSpec,MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));
                height = child.getMeasuredHeight();
            }
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightMeasureSpec,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    public void resetHeight(int current){
        this.current = current;
        if (mChildrenViews.size() > current) {
            LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
            } else {
                layoutParams.height = height;
            }
            setLayoutParams(layoutParams);
        }
    }

    /**
     * 保存position与对于的View
     */
    public void setObjectForPosition(View view, int position) {
        mChildrenViews.put(position, view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(!isScrollable){
            return true;
        }
        return super.onTouchEvent(ev);
    }

    public boolean isScrollable() {
        return isScrollable;
    }

    public void setScrollable(boolean scrollable) {
        isScrollable = scrollable;
    }
}
