package com.project.app.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * create
 */
public class ItemSpaceDecoration extends RecyclerView.ItemDecoration {
    private int left = 0;
    private int top = 0;
    private int right = 0 ;
    private int bottom = 0;
    private int size = 0;   //判断水平方向的数量
    private int orizontal = 0;  //判断方向是垂直还是水平 0是垂直 1是水平
    private final Context mContext;
    private boolean isHideTop = false;   //是否隐藏顶部的距离
    private boolean isHideLeft = false;  //是否隐藏左边的边距

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getOrizontal() {
        return orizontal;
    }

    public void setOrizontal(int orizontal) {
        this.orizontal = orizontal;
    }

    public boolean isHideTop() {
        return isHideTop;
    }

    public void setHideTop(boolean hideTop) {
        isHideTop = hideTop;
    }

    public boolean isHideLeft() {
        return isHideLeft;
    }

    public void setHideLeft(boolean hideLeft) {
        isHideLeft = hideLeft;
    }

    public ItemSpaceDecoration(Context context){
        this.mContext = context;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left   = QMUIDisplayHelper.dp2px(mContext,left);
        outRect.top    = QMUIDisplayHelper.dp2px(mContext,top);
        outRect.right  = QMUIDisplayHelper.dp2px(mContext,right);
        outRect.bottom = QMUIDisplayHelper.dp2px(mContext,bottom);

        switch (orizontal){
            case 0:
                if(isHideTop){
                    if(parent.getChildAdapterPosition(view) == 0){
                        outRect.top = 0;
                    }
                }
                break;
            case 1:
                if(isHideTop){
                    if(parent.getChildAdapterPosition(view) < size){
                        outRect.top = 0;
                    }
                }
                break;
        }

        if(parent.getChildAdapterPosition(view) == 0){
            if(isHideLeft){
                outRect.left = 0;
            }
        }
    }
}
