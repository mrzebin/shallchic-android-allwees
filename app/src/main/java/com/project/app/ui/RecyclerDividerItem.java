package com.project.app.ui;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerDividerItem extends RecyclerView.ItemDecoration {

//    @Override
//    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        //设置分隔线的left和right
//        int left = parent.getPaddingLeft();
//        int right = parent.getWidth() - parent.getPaddingRight();
//
//        for (int i = 0; i < parent.getChildCount(); i++) {
//            View view = parent.getChildAt(i);
//            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
//            //分隔线的top和bottom
//            int top = view.getBottom()+params.bottomMargin;
//            int bottom = top + mDivider.getIntrinsicHeight();
//            //分隔线的绘制区域
//            mDivider.setBounds(left,top,right,bottom);
//            //把分隔线绘制到canvas
//            mDivider.draw(c);
//        }
//    }
}
